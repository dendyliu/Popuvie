package com.example.android.popuvie.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popuvie.R;
import com.example.android.popuvie.adapter.MoviesAdapter;
import com.example.android.popuvie.data.MoviesContract;
import com.example.android.popuvie.data.MoviesDBHandler;
import com.example.android.popuvie.model.Genre;
import com.example.android.popuvie.model.GenresResponse;
import com.example.android.popuvie.model.Movie;
import com.example.android.popuvie.model.MovieDetailResponse;
import com.example.android.popuvie.model.MoviesResponse;
import com.example.android.popuvie.rest.ApiClient;
import com.example.android.popuvie.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();
    private final static String API_KEY = "cbf4929027ea106b582c9b966db9a85f";
    private MoviesAdapter movieAdapter ;
    List<Integer> listMovieId = new ArrayList<Integer>();
    private int orderMovieby = 0;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            orderMovieby = savedInstanceState.getInt("order_by");
        } else {
            orderMovieby = 0;
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            orderMovieby = 1;
            updateTopRatedMovie();
            return true;
        }
        if (id == R.id.action_popular) {
            orderMovieby = 0;
            updatePopularMovie();
            return true;
        }
        if (id == R.id.action_collection) {
            orderMovieby = 2;
            addMovieCollection();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("order_by", orderMovieby);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        final GridView movieGrid  = (GridView) rootView.findViewById(R.id.movies_grid);
        if (API_KEY.isEmpty()) {
            Toast.makeText(rootView.getContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return rootView;
        }
        movieAdapter = new MoviesAdapter(getActivity(),new ArrayList<Movie>());
        if(isOnline(rootView.getContext())){
            if(orderMovieby==0)
                updatePopularMovie();
            if(orderMovieby==1)
                updateTopRatedMovie();
            if(orderMovieby==2)
                addMovieCollection();
        }else{
            Toast.makeText(rootView.getContext(), "Please connect to internet first!", Toast.LENGTH_LONG).show();
            return rootView;
        }
        movieGrid.setAdapter(movieAdapter);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int movieId = movieAdapter.getItem(position).getId();
                String movieTitle = movieAdapter.getItem(position).getOriginalTitle();
                List<Integer> genresId = movieAdapter.getItem(position).getGenreIds();
                ArrayList<Integer> genresListId = new ArrayList<Integer>();
                genresListId.addAll(genresId);
                String movieImgUrl = movieAdapter.getItem(position).getPosterPath();
                String movieRate =  movieAdapter.getItem(position).getVoteAverage().toString();
                String movieDesc =  movieAdapter.getItem(position).getOverview();
                String movieReleaseDate =  movieAdapter.getItem(position).getReleaseDate();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("id",movieId)
                        .putIntegerArrayListExtra("genres_id",genresListId)
                        .putExtra("title", movieTitle)
                        .putExtra("img", movieImgUrl)
                        .putExtra("desc",movieDesc)
                        .putExtra("rate",movieRate)
                        .putExtra("date",movieReleaseDate);
                startActivity(intent);
            }
        });

        return rootView;
    }

    void updatePopularMovie(){
        movieAdapter.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;
        call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                int totalPages = response.body().getTotalPages();
                movieAdapter.addAll(movies);
                Log.d(TAG, "Number of movies received: " + movies.size()+","+totalPages);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString() + " ini Errornya");
            }
        });
    }

    void updateTopRatedMovie(){
        movieAdapter.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;
        call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                int totalPages = response.body().getTotalPages();
                movieAdapter.addAll(movies);
                Log.d(TAG, "Number of movies received: " + movies.size()+","+totalPages);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString() + " ini Errornya");
            }
        });
    }

    void addMovieCollection() {
        movieAdapter.clear();
        MoviesDBHandler dbHandler = new MoviesDBHandler(getContext());
        dbHandler.open();
        Cursor cAll = dbHandler.fetchAllMovie();
        if (cAll != null) {
            for(int i=1;i<cAll.getCount();i++){
                Cursor c = dbHandler.fetchMovie(i);
                int movieId = c.getInt(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
                if(!listMovieId.contains(movieId)) {
                    listMovieId.add(movieId);
                }
            }
                for(int i=0;i<listMovieId.size();i++){
                    updateCollection(listMovieId.get(i));
                }
            } else
                Toast.makeText(getContext(), "Database empty", Toast.LENGTH_LONG).show();
        dbHandler.close();
    }

    void updateCollection(final int movie_id){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieDetailResponse> call = apiService.getMovieDetails(movie_id,API_KEY);
        call.enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                int movies_id= response.body().getId();
                String img_path = response.body().getPosterPath();
                String title = response.body().getTitle();
                String desc = response.body().getOverview();
                Double rate = response.body().getVoteAverage();
                String date = response.body().getReleaseDate();
                List<Genre> listGenre = response.body().getGenreList();
                List<Integer> listIdGenre = new ArrayList<Integer>();
                for(int i=0;i<listGenre.size();i++){
                    listIdGenre.add(listGenre.get(i).getId());
                }
                Movie movie = new Movie(img_path,false,desc,date,listIdGenre,movie_id,title,"en",title,img_path,12312.0,1231,true,rate);
                movieAdapter.add(movie);
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString() + " ini Errornya");
            }
        });
    }


    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
