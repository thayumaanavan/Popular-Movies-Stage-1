package com.thayumaanavan.popularmovies_stage1.network;

import com.thayumaanavan.popularmovies_stage1.models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieService {

    @GET("movie/popular")
    Call<Movies> getMostPopularMovies();

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies();
}
