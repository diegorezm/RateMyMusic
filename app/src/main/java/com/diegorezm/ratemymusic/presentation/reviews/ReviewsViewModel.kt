package com.diegorezm.ratemymusic.presentation.reviews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.createReviewUseCase
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.getReviewsUseCase
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.removeReviewUseCase
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val filter: ReviewFilter,
    private val reviewsRepository: ReviewsRepository,
    private val auth: Auth
) : ViewModel() {
    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Idle)
    val reviewsState = _reviewsState.onStart {
        loadReviews()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ReviewsState.Idle)

    val currentUserId = auth.currentUserOrNull()?.id


    fun loadReviews() {
        _reviewsState.value = ReviewsState.Loading

        viewModelScope.launch {
            getReviewsUseCase(filter, reviewsRepository).onSuccess {
                Log.i("ReviewsViewModel", "Reviews loaded successfully: \n $it")
                _reviewsState.value = ReviewsState.Success(it)
            }.onFailure {
                Log.e("ReviewsViewModel", "Error loading reviews.", it)
                _reviewsState.value = ReviewsState.Error("Something went wrong.")
            }
        }
    }

    fun createReview(content: String, rating: Int = 1) {
        val user = auth.currentUserOrNull() ?: return

        viewModelScope.launch {
            val entityType = when (filter) {
                is ReviewFilter.ByAlbum -> ReviewType.ALBUM
                is ReviewFilter.ByTrack -> ReviewType.TRACK
                is ReviewFilter.ByUser -> ReviewType.TRACK
            }

            val entityId = when (filter) {
                is ReviewFilter.ByAlbum -> filter.albumId
                is ReviewFilter.ByTrack -> filter.trackId
                is ReviewFilter.ByUser -> filter.userId
            }

            val dto = ReviewDTO(
                reviewerId = user.id,
                entityId = entityId,
                reviewType = entityType,
                content = content,
                rating = rating
            )

            createReviewUseCase(dto, reviewsRepository).onSuccess {
                Log.i("ReviewsViewModel", "Review written successfully")
                loadReviews()
            }.onFailure {
                Log.e("ReviewsViewModel", "Error writing review.", it)
            }
        }
    }

    fun removeReview(reviewId: String) {
        viewModelScope.launch {
            removeReviewUseCase(reviewId, reviewsRepository).onSuccess {
                Log.i("ReviewsViewModel", "Review removed successfully")
                loadReviews()
            }.onFailure {
                Log.e("ReviewsViewModel", "Error removing review.", it)
            }
        }
    }
}