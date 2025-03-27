package com.diegorezm.ratemymusic.presentation.reviews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewEntityType
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.repositories.ReviewsRepositoryImpl
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.createReviewUseCase
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.getReviewsUseCase
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.removeReviewUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val filter: ReviewFilter,
    private val reviewsRepository: ReviewsRepository = ReviewsRepositoryImpl()
) : ViewModel() {
    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Idle)
    val reviewsState = _reviewsState.onStart {
        loadReviews()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ReviewsState.Idle)

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
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) return

        viewModelScope.launch {
            val entityType = when (filter) {
                is ReviewFilter.ByAlbum -> ReviewEntityType.ALBUM
                is ReviewFilter.ByTrack -> ReviewEntityType.TRACK
                is ReviewFilter.ByUser -> ReviewEntityType.TRACK
            }

            val entityId = when (filter) {
                is ReviewFilter.ByAlbum -> filter.albumId
                is ReviewFilter.ByTrack -> filter.trackId
                is ReviewFilter.ByUser -> filter.userId
            }

            val dto = ReviewDTO(
                reviewerId = user.uid,
                entityId = entityId,
                entityType = entityType,
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