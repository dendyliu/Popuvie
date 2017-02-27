package com.example.android.popuvie.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popuvie.R;
import com.example.android.popuvie.adapter.MoviesAdapter;
import com.example.android.popuvie.adapter.ReviewAdapter;
import com.example.android.popuvie.adapter.TrailerAdapter;
import com.example.android.popuvie.data.MoviesDBHandler;
import com.example.android.popuvie.model.Genre;
import com.example.android.popuvie.model.GenresResponse;
import com.example.android.popuvie.model.Movie;
import com.example.android.popuvie.model.MoviesResponse;
import com.example.android.popuvie.model.Review;
import com.example.android.popuvie.model.ReviewResponse;
import com.example.android.popuvie.model.Trailer;
import com.example.android.popuvie.model.TrailerResponse;
import com.example.android.popuvie.rest.ApiClient;
import com.example.android.popuvie.rest.ApiInterface;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    public static class DetailFragment extends Fragment {
        private final static String API_KEY = "cbf4929027ea106b582c9b966db9a85f";
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private static final String MOVIE_SHARE_HASHTAG = " #PopuvieApp";
        private TrailerAdapter trailerAdapter ;
        private ReviewAdapter  reviewAdapter;
        private int  movieId;
        private List<Integer> movieGenresId;
        private String sMovieGenres;
        private String movieTitle;
        private String imageMovie;
        private String movieDesc;
        private String movieReleaseDate;
        private String movieRate;
        private String allGenre;

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            movieGenresId =  new ArrayList<Integer>();
            trailerAdapter = new TrailerAdapter(getActivity(),new ArrayList<Trailer>());
            reviewAdapter = new ReviewAdapter(getActivity(),new ArrayList<Review>());
            Intent intent = getActivity().getIntent();
            fillIntent(rootView,intent);
            ExpandableHeightListView listTrailer  = (ExpandableHeightListView) rootView.findViewById(R.id.list_trailer);
            ExpandableHeightListView listReview  = (ExpandableHeightListView) rootView.findViewById(R.id.list_review);
            listTrailer.setAdapter(trailerAdapter);
            listReview.setAdapter(reviewAdapter);
            listTrailer.setExpanded(true);
            listReview.setExpanded(true);
            listTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String key = trailerAdapter.getItem(position).getKey();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });
            Button bookmarkBtn = (Button) rootView.findViewById(R.id.bookmark_button);
            bookmarkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoviesDBHandler dbHandler = new MoviesDBHandler(v.getContext());
                    dbHandler.open();
                    dbHandler.createMovie(movieId,movieTitle,imageMovie,movieDesc,movieRate,movieReleaseDate,sMovieGenres);
                    dbHandler.close();
                    dbHandler.open();
                    Cursor c= dbHandler.fetchMovie(1);
                    Toast.makeText(v.getContext(), "Bookmark Success!", Toast.LENGTH_LONG).show();
                    dbHandler.close();
                }
            });
            return rootView;
        }

        private void fillIntent(View rootView, Intent intent){
            if (intent != null) {
                movieId = intent.getIntExtra("id",0);
                movieGenresId = intent.getIntegerArrayListExtra("genres_id");
                for(int i=0;i<movieGenresId.size();i++){
                    sMovieGenres = sMovieGenres+"|"+movieGenresId.get(i);
                }
                Log.d(LOG_TAG, "MOVIE GENRE ID: " + movieGenresId.size());
                movieTitle = intent.getStringExtra("title");
                imageMovie = intent.getStringExtra("img");
                movieDesc = intent.getStringExtra("desc");
                movieRate = intent.getStringExtra("rate");
                movieReleaseDate = " ("+getYear(intent.getStringExtra("date"))+")";
                String movieTitleDate = "<font color=#B0BEC5><bold>"+movieTitle+"</bold></font><font color=#9E9E9E>"+movieReleaseDate+"</font>";
                ((TextView) rootView.findViewById(R.id.movie_title))
                        .setText(Html.fromHtml(movieTitleDate));
                ImageView movieThumbnail =(ImageView)  rootView.findViewById(R.id.movie_image_thumbnail);
                String picassoUrl = "http://image.tmdb.org/t/p/" + "w500/" + imageMovie;
                Picasso.with(getContext()).load(picassoUrl).into(movieThumbnail);
                ((TextView) rootView.findViewById(R.id.movie_synopsis))
                        .setText(movieDesc);
                ((TextView) rootView.findViewById(R.id.movie_user_rating))
                        .setText(movieRate);
                getListGenre(movieGenresId,rootView);
                updateTrailer(movieId);
                updateReview(movieId);
            }
        }

        private String getYear(String date){
            String [] parts  = date.split("-");
            return parts[0];
        }

        void updateTrailer(int movie_id){
            trailerAdapter.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<TrailerResponse> call = apiService.getMovieVideos(movie_id,API_KEY);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailers = response.body().getResults();
                    trailerAdapter.addAll(trailers);
                    Log.d(LOG_TAG, "Number of movies received: " + trailers.size());
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.toString() + " ini Error trailer");
                }
            });
        }

        void updateReview(int movie_id){
            reviewAdapter.clear();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ReviewResponse> call = apiService.getMovieReviews(movie_id,API_KEY);
            call.enqueue(new Callback<ReviewResponse>(){
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    List<Review> reviews = response.body().getResults();
                    reviewAdapter.addAll(reviews);
                    Log.d(LOG_TAG, "Number of reviews received: " + reviews.size());
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.toString() + " ini Error review");
                }
            });

        }

        public void getListGenre(final List<Integer> movieGenresId,final View rootView){
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<GenresResponse> call = apiService.getMovieListGenres(API_KEY);
            call.enqueue(new Callback<GenresResponse>() {
                @Override
                public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                    List<Genre> genres = response.body().getGenres();
                    allGenre = getGenreMovie(movieGenresId,genres);
                    ((TextView) rootView.findViewById(R.id.genres))
                            .setText(allGenre);
                    Log.d(LOG_TAG, "Number of genres received: " + genres.size());
                    Log.d(LOG_TAG, "listGenre: " + genres.get(18).getName());
                    Log.d(LOG_TAG, "ALLGenre: " + allGenre);
                }

                @Override
                public void onFailure(Call<GenresResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(LOG_TAG, t.toString() + " ini Errornya list genre");
                }
            });
        }

        String getGenreMovie(List<Integer> genresId,List<Genre> listGenres){
            String genreStr = "";
            for(int i=0;i<genresId.size();i++){
                if(genreStr.isEmpty()){
                    genreStr=findGenre(genresId.get(i),listGenres);
                }else
                    genreStr=genreStr+", "+findGenre(genresId.get(i),listGenres);
            }
            return genreStr;
        }

        String findGenre(int id,List<Genre> listGenres){
            String genreName="";
            Log.d(LOG_TAG, "LIST GENRE NOW: " + listGenres.size());
            for(int i=0;i<listGenres.size();i++){
                if(listGenres.get(i).getId()==id){
                    genreName = listGenres.get(i).getName();
                    Log.d(LOG_TAG, "genre achieved: " + genreName);
                }
            }
            return genreName;
        }
    }
}
