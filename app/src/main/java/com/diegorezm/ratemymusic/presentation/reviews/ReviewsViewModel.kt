package com.diegorezm.ratemymusic.presentation.reviews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.repositories.ReviewsRepositoryImpl
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.getReviewsUseCase
import com.diegorezm.ratemymusic.modules.reviews.domain.use_cases.removeReviewUseCase
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
                _reviewsState.value = ReviewsState.Error(it.message ?: "Something went wrong.")
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