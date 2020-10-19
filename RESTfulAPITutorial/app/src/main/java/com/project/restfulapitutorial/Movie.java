package com.project.restfulapitutorial;


/* Movie objects are objects returned from API
/  Make sure that fields have same names as
/  in API responses, otherwise you have to
/  bind field names that are in response
/  with names in your created class */
public class Movie {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    public Movie(String title, String year, String imdbID, String type, String poster) {
        Title = title;
        Year = year;
        this.imdbID = imdbID;
        Type = type;
        Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }
}
