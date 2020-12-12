package com.movie.random

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import java.text.SimpleDateFormat
import java.util.*

// tuto https://www.arthlimchiu.com/2019/10/02/make-a-movies-app-part-1.html

class MainActivity : AppCompatActivity() {

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private var topRatedMoviesPage = 1

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager
    private var popularMoviesPage = 1

    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager
    private var upcomingMoviesPage = 1

    private lateinit var nowPlayingMovies: RecyclerView
    private lateinit var nowPlayingMoviesAdapter: MoviesAdapter
    private lateinit var nowPlayingMoviesLayoutMgr: LinearLayoutManager
    private var nowPlayingMoviesPage = 1

    private lateinit var latestPoster: ImageView
    private lateinit var latestTitle: TextView
    private lateinit var latestRating: RatingBar
    private lateinit var latestReleaseDate: TextView
    private lateinit var latestOverview: TextView
    private lateinit var latestRefreshText: TextView


    ////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /////////////////////

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie)}
        popularMovies.adapter = popularMoviesAdapter

        /////////////////////

        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMoviesLayoutMgr = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr
        topRatedMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        topRatedMovies.adapter = topRatedMoviesAdapter

        /////////////////////

        upcomingMovies = findViewById(R.id.upcoming_movies)
        upcomingMoviesLayoutMgr = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        upcomingMovies.layoutManager = upcomingMoviesLayoutMgr
        upcomingMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        upcomingMovies.adapter = upcomingMoviesAdapter

        /////////////////

        nowPlayingMovies = findViewById(R.id.nowPlaying_movies)
        nowPlayingMoviesLayoutMgr = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        nowPlayingMovies.layoutManager = nowPlayingMoviesLayoutMgr
        nowPlayingMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        nowPlayingMovies.adapter = nowPlayingMoviesAdapter

        /////////////////

         latestPoster= findViewById(R.id.latest_movie_poster)
         latestTitle= findViewById(R.id.latest_movie_title)
         latestRating= findViewById(R.id.latest_movie_rating)
         latestReleaseDate= findViewById(R.id.latest_movie_release_date)
         latestOverview= findViewById(R.id.latest_movie_overview)
        latestRefreshText= findViewById(R.id.refresh_text)

        latestPoster.setOnClickListener { refreshLatest()}

        /////////////////

        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
        getNowPlayingMovies()
        getLatestMovie()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        intent.putExtra(MOVIE_ID, movie.id)
        startActivity(intent)
    }

    //////////////////////////// popular

    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
                popularMoviesPage,
                ::onPopularMoviesFetched,
                ::onError
        )
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    private fun attachPopularMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    //////////////////////////// top

    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
                topRatedMoviesPage,
                ::onTopRatedMoviesFetched,
                ::onError
        )
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }


    //////////////////////////// upcoming

    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
                upcomingMoviesPage,
                ::onUpcomingMoviesFetched,
                ::onError
        )
    }

    private fun attachUpcomingMoviesOnScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }

    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }


    //////////////////////////// nowPlaying

    private fun getNowPlayingMovies() {
        MoviesRepository.getNowPlayingMovies(
                nowPlayingMoviesPage,
                ::onNowPlayingMoviesFetched,
                ::onError
        )
    }

    private fun onNowPlayingMoviesFetched(movies: List<Movie>) {
        nowPlayingMoviesAdapter.appendMovies(movies)
        attachNowPlayingMoviesOnScrollListener()
    }

    private fun attachNowPlayingMoviesOnScrollListener() {
        nowPlayingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = nowPlayingMoviesLayoutMgr.itemCount
                val visibleItemCount = nowPlayingMoviesLayoutMgr.childCount
                val firstVisibleItem = nowPlayingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    nowPlayingMovies.removeOnScrollListener(this)
                    nowPlayingMoviesPage++
                    getNowPlayingMovies()
                }
            }
        })
    }

    //////////////////////////// latest

    private fun getLatestMovie() {
        MoviesRepository.getLatestMovie(
                ::onLatestMoviesFetched,
                ::onError
        )
    }

    private fun refreshLatest() {
        getLatestMovie()
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun onLatestMoviesFetched(movie: Movie) {

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime: String = sdf.format(Date())
        latestRefreshText.text = "Click on picture to refresh (last $currentTime)"

        latestTitle.text = movie.title
        latestRating.rating =  movie.rating/2
        latestReleaseDate.text= movie.releaseDate
        latestOverview.text= movie.overview

        if (movie.posterPath.isNullOrEmpty()) {
            Glide.with(this)
                    .load("https://www.labaleine.fr/sites/default/files/image-not-found.jpg")
                    .transform(CenterCrop())
                    .into(latestPoster)
        } else {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                    .transform(CenterCrop())
                    .into(latestPoster)
        }
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }
}