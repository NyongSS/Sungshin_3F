package org.techtown.sungshin3f.presentation.reviews

import org.techtown.sungshin3f.domain.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.techtown.sungshin3f.domain.model.MovieReviews
import org.techtown.sungshin3f.domain.model.Review
import org.techtown.sungshin3f.domain.usecase.DeleteReviewUseCase
import org.techtown.sungshin3f.domain.usecase.GetAllMovieReviewsUseCase
import org.techtown.sungshin3f.domain.usecase.SubmitReviewUseCase

class MovieReviewsPresenter(
    override val movie: Movie,
    private val view: MovieReviewsContract.View,
    private val getAllReviews: GetAllMovieReviewsUseCase,
    private val submitReview: SubmitReviewUseCase,
    private val deleteReview: DeleteReviewUseCase
) : MovieReviewsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    private var movieReviews: MovieReviews = MovieReviews(null, emptyList())

    override fun onViewCreated() {
        view.showMovieInformation(movie)
        fetchReviews()
    }

    override fun onDestroyView() {}

    override fun requestAddReview(content: String, score: Float) {
        scope.launch {
            try {
                view.showLoadingIndicator()
                val submittedReview = submitReview(movie, content, score)
                view.showReviews(movieReviews.copy(myReview = submittedReview))
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorToast("리뷰 등록을 실패했어요 😢")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    override fun requestRemoveReview(review: Review) {
        scope.launch {
            try {
                view.showLoadingIndicator()
                deleteReview(review)
                view.showReviews(movieReviews.copy(myReview = null))
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorToast("리뷰 삭제를 실패했어요 😢")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    private fun fetchReviews() = scope.launch {
        try {
            view.showLoadingIndicator()
            movieReviews = getAllReviews(movie.id!!)
            view.showReviews(movieReviews)
        } catch (exception: Exception) {
            exception.printStackTrace()
            view.showErrorDescription("에러가 발생했어요 😢")
        } finally {
            view.hideLoadingIndicator()
        }
    }
}
