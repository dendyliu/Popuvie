package com.example.android.popuvie.rest;

/**
 * Created by Asus X550ZE on 12/6/2016.
 */

import com.example.android.popuvie.model.GenresResponse;
import com.example.android.popuvie.model.MovieDetailResponse;
import com.example.android.popuvie.model.MoviesResponse;
import com.example.android.popuvie.model.ReviewResponse;
import com.example.android.popuvie.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<GenresResponse> getMovieListGenres(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetailResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
