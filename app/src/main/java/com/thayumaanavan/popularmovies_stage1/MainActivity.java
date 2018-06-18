package com.thayumaanavan.popularmovies_stage1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thayumaanavan.popularmovies_stage1.models.Movie;
import com.thayumaanavan.popularmovies_stage1.models.Movies;
import com.thayumaanavan.popularmovies_stage1.network.ApiClient;
import com.thayumaanavan.popularmovies_stage1.network.MovieService;
import com.thayumaanavan.popularmovies_stage1.utils.AppConstants;
import com.thayumaanavan.popularmovies_stage1.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thayumaanavan.popularmovies_stage1.utils.AppConstants.MOVIE_LIST;
import static com.thayumaanavan.popularmovies_stage1.utils.AppConstants.SORT_BY;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.no_network_layout) ConstraintLayout constraintLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private MoviesAdapter moviesAdapter;
    private ArrayList<Movie> movieList;
    private Call<Movies> moviesCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new GridLayoutManager(this,
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE?5:2));
        recyclerView.setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_LIST)){
            movieList = new ArrayList<>();
            showTopRatedMovies();
        }else if (savedInstanceState.containsKey(MOVIE_LIST)){
            if(!NetworkUtils.isOnline(this)){
                hideList();
            }else {
                movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            }
            if(savedInstanceState.containsKey(SORT_BY)){
                    getSupportActionBar().setTitle(savedInstanceState.getString(SORT_BY));
            }
        }

        if(movieList == null)
            movieList = new ArrayList<>();

        moviesAdapter = new MoviesAdapter(this, movieList, new MovieItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(AppConstants.MOVIE_DETAILS, movie);
                startActivity(intent);
            }
        });
        moviesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(moviesAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST, movieList);
        outState.putString(SORT_BY, getSupportActionBar().getTitle().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.top_rated) {
            showTopRatedMovies();
            return true;
        }else if ( id == R.id.most_popular){
            showMostPopularMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bn_retry)
    public void retry(){
        if (getSupportActionBar().getTitle().toString().equals(getString(R.string.top_rated)))
            showTopRatedMovies();
        else
            showMostPopularMovies();
    }

    private void showList(){
        constraintLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideList(){
        constraintLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showTopRatedMovies(){
        getSupportActionBar().setTitle(R.string.top_rated);
        if(!NetworkUtils.isOnline(this)){
            hideList();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        MovieService movieService = ApiClient.getMovieService();
        moviesCall= movieService.getTopRatedMovies();
        moviesCall.enqueue(callback);
    }

    private void showMostPopularMovies(){
        getSupportActionBar().setTitle(R.string.most_popular);
        if(!NetworkUtils.isOnline(this)){
            hideList();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        MovieService movieService = ApiClient.getMovieService();
        moviesCall = movieService.getMostPopularMovies();
        moviesCall.enqueue(callback);
    }

    private Callback callback = new Callback<Movies>() {
        @Override
        public void onResponse(Call<Movies> call, Response<Movies> response) {
            Movies movies = response.body();

            if(movies != null){
                movieList.clear();
                movieList.addAll(movies.getMovies());
                moviesAdapter.notifyDataSetChanged();
            }
            showList();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<Movies> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), R.string.list_loading_error, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesCall !=null){
            if(moviesCall.isExecuted()){
                moviesCall.cancel();
            }
        }
    }
}
