package com.diegorezm.ratemymusic.reviews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewDTO
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewEntityDTO
import com.diegorezm.ratemymusic.reviews.domain.ReviewRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repository: ReviewRepository,
    private val auth: Auth,
    private val filter: ReviewFilter

) : ViewModel() {
    private val _state = MutableStateFlow(ReviewsScreenState())
    val state =
        _state.onStart {
            getUserId()
            fetchReviews()
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun onAction(action: ReviewsScreenActions) {
        when (action) {
            is ReviewsScreenActions.OnCreateReview -> {
                createReview(action.content, action.rating)
            }

            is ReviewsScreenActions.OnDeleteReview -> {
                viewModelScope.launch {
                    repository.remove(action.reviewId).onSuccess {
                        fetchReviews()
                    }.onError {
                        _state.value = _state.value.copy(error = it)
                    }
                }
            }

            else -> Unit
        }
    }

    private fun fetchReviews() {
        _state.value = _state.value.copy(isLoading = true)
        when (filter) {
            is ReviewFilter.ByAlbum -> {
                viewModelScope.launch {
                    repository.getByEntityId(filter.albumId, ReviewEntityDTO.ALBUM).onSuccess {
                        _state.value = _state.value.copy(reviews = it, isLoading = false)
                    }.onError {
                        _state.value = _state.value.copy(error = it, isLoading = false)
                    }
                }
            }

            is ReviewFilter.ByTrack -> {
                viewModelScope.launch {
                    repository.getByEntityId(filter.trackId, ReviewEntityDTO.TRACK).onSuccess {
                        _state.value = _state.value.copy(reviews = it, isLoading = false)
                    }.onError {
                        _state.value = _state.value.copy(error = it, isLoading = false)
                    }
                }
            }

            is ReviewFilter.ByArtist -> {
                viewModelScope.launch {
                    repository.getByEntityId(filter.artistId, ReviewEntityDTO.ARTIST).onSuccess {
                        _state.value = _state.value.copy(reviews = it, isLoading = false)
                    }.onError {
                        _state.value = _state.value.copy(error = it, isLoading = false)
                    }
                }
            }

            is ReviewFilter.ByUser -> {
                viewModelScope.launch {
                    repository.getByUserId(filter.userId).onSuccess {
                        _state.value = _state.value.copy(reviews = it, isLoading = false)
                    }.onError {
                        _state.value = _state.value.copy(error = it, isLoading = false)
                    }
                }
            }
        }
    }


    private fun createReview(content: String, rating: Int) {
        val currentUserId = _state.value.currentUserId
        if (currentUserId == null) {
            return
        }
        viewModelScope.launch {
            val entityType = when (filter) {
                is ReviewFilter.ByAlbum -> ReviewEntityDTO.ALBUM
                is ReviewFilter.ByTrack -> ReviewEntityDTO.TRACK
                is ReviewFilter.ByUser -> ReviewEntityDTO.TRACK
                is ReviewFilter.ByArtist -> ReviewEntityDTO.ARTIST
            }

            val entityId = when (filter) {
                is ReviewFilter.ByAlbum -> filter.albumId
                is ReviewFilter.ByTrack -> filter.trackId
                is ReviewFilter.ByUser -> filter.userId
                is ReviewFilter.ByArtist -> filter.artistId
            }


            val reviewDTO = ReviewDTO(
                reviewerId = currentUserId,
                entityId = entityId,
                reviewType = entityType,
                content = content,
                rating = rating
            )

            repository.create(reviewDTO).onSuccess {
                fetchReviews()
            }.onError {
                _state.value = _state.value.copy(error = it)
            }
        }

    }

    private fun getUserId() {
        val user = auth.currentUserOrNull()
        if (user != null) {
            _state.value = _state.value.copy(currentUserId = user.id)
        }
    }
}