package com.thayumaanavan.popularmovies_stage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thayumaanavan.popularmovies_stage1.models.Movie;
import com.thayumaanavan.popularmovies_stage1.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_backdrop) ImageView imageView;
    @BindView(R.id.tv_synopsis) TextView tvSynopsis;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(AppConstants.MOVIE_DETAILS)){
            movie = intent.getParcelableExtra(AppConstants.MOVIE_DETAILS);
            addMovieDetails();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addMovieDetails() {
        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.get().load(AppConstants.BACKDROP_BASE_URL + movie.getBackdropPath())
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        tvSynopsis.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvRating.setText(movie.getVoteAverage().toString() +"/10");
    }

}
