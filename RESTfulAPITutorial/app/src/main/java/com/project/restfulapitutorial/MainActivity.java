package com.project.restfulapitutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Query param value for type returned
    public static final String TYPE_MOVIE = "movie";
    // List of movies we have for each search
    List<Movie> movies;
    // Adapter for RecyclerView
    MovieAdapter adapter;

    // REPLACE WITH YOUR API KEY
    String API_KEY_VALUE = "f3cebe11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Just an empty list
        movies = new ArrayList<>();
        // Get views
        final RecyclerView recyclerView = findViewById(R.id.recyclerVIew);
        final EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);

        // Declaring adapter for RecyclerView
        adapter = new MovieAdapter(movies, this);
        // Setting adapter to RV
        recyclerView.setAdapter(adapter);
        // Setting the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create Retrofit instance with API's base URL and required converter (GSON in our case)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // API object to perform queries and any types of requests on
        final API api = retrofit.create(API.class);

        // On click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To hide Virtual Keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get text from EditText
                String searchQuery = searchEditText.getText().toString().trim();
                // Check if it is empty
                if (searchQuery.equals("")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please Enter a Search Query",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    // Call method to perform query
                    // Sends GET request to the API endpoint
                    getMovies(api, searchQuery);
                }
            }
        });
    }

    /**
     * Method to perform a GET request to the API endpoint
     * Will return an object with some data
     * The contents of 'movies' list is updated
     * without losing the reference to previous list object in memory;
     * Otherwise RV will not display anything at all
     * Appropriate messages are displayed in case of failure
     * API object is passed to work on
     * along with the search query defined by user-input.
     */
    private void getMovies(API api, String searchQuery) {
        // Make a call to the API
        Call<ApiResponse> call = api.getMovies(API_KEY_VALUE, searchQuery, TYPE_MOVIE);
        // Add a callback to the call
        call.enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                // We need to check if response is successful
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get response
                ApiResponse apiResponse = response.body();
                // Get movies from response
                // Clear the list first
                movies.clear();
                // If user makes a typo or in some other case
                // getSearch() might produce nullPointerException
                // So better check for it
                if (apiResponse.getSearch() != null) {
                    // Add data without losing
                    // reference to the object in memory
                    movies.addAll(apiResponse.getSearch());
                    // Notify the adapter that data has changed
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong\nMake sure your spelling is correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}