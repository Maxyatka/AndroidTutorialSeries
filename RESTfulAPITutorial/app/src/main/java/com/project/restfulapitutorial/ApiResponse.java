package com.project.restfulapitutorial;

import java.util.List;

/* Response from API consists of array with desired objects
/  total number of results and string value true/false for response */
public class ApiResponse {
    private List<Movie> Search;
    private String totalResults;
    private String Response;

    public ApiResponse(List<Movie> search, String totalResults, String response) {
        Search = search;
        this.totalResults = totalResults;
        Response = response;
    }

    public List<Movie> getSearch() {
        return Search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return Response;
    }
}
