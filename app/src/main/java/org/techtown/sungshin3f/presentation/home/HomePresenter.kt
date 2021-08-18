package org.techtown.sungshin3f.presentation.home

import org.techtown.sungshin3f.domain.usecase.GetAllMoviesUseCase
import org.techtown.sungshin3f.domain.usecase.GetRandomFeaturedMovieUseCase
import org.techtown.sungshin3f.presentation.home.HomeContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeContract.View,
    private val getRandomFeaturedMovie: GetRandomFeaturedMovieUseCase,
    private val getAllMovies: GetAllMoviesUseCase
) : HomeContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchMovies()
    }

    override fun onDestroyView() {}

    private fun fetchMovies() = scope.launch {
        try {
            view.showLoadingIndicator()
            val featuredMovie = getRandomFeaturedMovie()
            val movies = getAllMovies()
            view.showMovies(featuredMovie, movies)
        } catch (exception: Exception) {
            exception.printStackTrace()
            view.showErrorDescription("에러가 발생했어요 😢")
        } finally {
            view.hideLoadingIndicator()
        }
    }
}
