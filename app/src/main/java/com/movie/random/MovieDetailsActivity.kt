package com.movie.random

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

const val MOVIE_BACKDROP = "extra_movie_backdrop"
const val MOVIE_POSTER = "extra_movie_poster"
const val MOVIE_TITLE = "extra_movie_title"
const val MOVIE_RATING = "extra_movie_rating"
const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
const val MOVIE_OVERVIEW = "extra_movie_overview"
const val MOVIE_ID = "extra_movie_id"

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView

    private lateinit var recommendationsMovies: RecyclerView
    private lateinit var recommendationsMoviesAdapter: MoviesAdapter
    private lateinit var recommendationsMoviesLayoutMgr: LinearLayoutManager
    private var recommendationsMoviesPage = 1
    private var movieId  : Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        backdrop = findViewById(R.id.movie_backdrop)
        poster = findViewById(R.id.movie_poster)
        title = findViewById(R.id.movie_title)
        rating = findViewById(R.id.movie_rating)
        releaseDate = findViewById(R.id.movie_release_date)
        overview = findViewById(R.id.movie_overview)


        recommendationsMovies = findViewById(R.id.recommendations_movies)
        recommendationsMoviesLayoutMgr = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        recommendationsMovies.layoutManager = recommendationsMoviesLayoutMgr
        recommendationsMoviesAdapter = MoviesAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        recommendationsMovies.adapter = recommendationsMoviesAdapter

        val extras = intent.extras

        if (extras != null) {
            populateDetails(extras)
        } else {
            finish()
        }
    }

    private fun populateDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                    .transform(CenterCrop())
                    .into(backdrop)
        }

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342$posterPath")
                    .transform(CenterCrop())
                    .into(poster)
        }

        title.text = extras.getString(MOVIE_TITLE, "")
        rating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        releaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        overview.text = extras.getString(MOVIE_OVERVIEW, "")

        movieId = extras.getLong(MOVIE_ID, 1)
        getRecommendationsMovies()
    }

    private fun getRecommendationsMovies( ) {
        MoviesRepository.getRecommendationsMovies(
                movieId,
                recommendationsMoviesPage,
                ::onRecommendationsMoviesFetched,
                ::onError
        )
    }

    private fun attachRecommendationsMoviesOnScrollListener() {
        recommendationsMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = recommendationsMoviesLayoutMgr.itemCount
                val visibleItemCount = recommendationsMoviesLayoutMgr.childCount
                val firstVisibleItem = recommendationsMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    recommendationsMovies.removeOnScrollListener(this)
                    recommendationsMoviesPage++
                    getRecommendationsMovies()
                }
            }
        })
    }

    private fun onRecommendationsMoviesFetched(movies: List<Movie>) {
        Log.d("Recommendation", "_______________________$movies")
       recommendationsMoviesAdapter.appendMovies(movies)
       attachRecommendationsMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
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
}
