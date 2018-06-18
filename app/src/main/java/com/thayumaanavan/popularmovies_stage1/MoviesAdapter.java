package com.thayumaanavan.popularmovies_stage1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thayumaanavan.popularmovies_stage1.models.Movie;
import com.thayumaanavan.popularmovies_stage1.utils.AppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movieList;
    private Context context;

    public MoviesAdapter(Context context, List<Movie> movieList){
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.get().load(AppConstants.POSTER_BASE_URL+movieList.get(position).getPosterPath())
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_list_item) ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.iv_list_item)
        public void onClick(){
            Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
            intent.putExtra(AppConstants.MOVIE_DETAILS, movieList.get(getAdapterPosition()));
            itemView.getContext().startActivity(intent);
        }
    }
}
