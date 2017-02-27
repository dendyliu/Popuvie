package com.example.android.popuvie.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popuvie.R;
import com.example.android.popuvie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Asus X550ZE on 12/6/2016.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private final static String MOVIE_IMG_URL = "http://image.tmdb.org/t/p/";

    public MoviesAdapter(Activity context, List<Movie> movie) {
        super(context, 0, movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView imageMovieView = (ImageView) convertView.findViewById(R.id.movie_image);
        String picassoUrl = MOVIE_IMG_URL + "w342/" + movie.getPosterPath();
        Picasso.with(getContext()).load(picassoUrl).into(imageMovieView);

        return convertView;
    }
}
