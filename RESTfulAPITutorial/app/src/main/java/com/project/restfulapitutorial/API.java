package com.project.restfulapitutorial;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    // Base url of API
    String BASE_API_URL = "http://www.omdbapi.com/";
    // Query parameter (required, otherwise api won't allow requests)
    String API_KEY_QUERY = "apikey";

    // Method that queries the api endpoint with passed parameters
    @GET(".")
    Call<ApiResponse> getMovies(
            @Query(API_KEY_QUERY) String apiKey,
            @Query("s") String searchQuery,
            @Query("type") String searchType
    );
}
