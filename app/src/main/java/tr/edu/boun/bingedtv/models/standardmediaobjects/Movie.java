package tr.edu.boun.bingedtv.models.standardmediaobjects;

/**
 * Created by haluks on 26.11.2017.
 */

public class Movie
{
    /**
     * {
     "title": "Batman Begins",
     "year": 2005,
     "ids": {
     "trakt": 1,
     "slug": "batman-begins-2005",
     "imdb": "tt0372784",
     "tmdb": 272
     }
     }
     */

    public String title;
    public int year;
    public MovieIDs ids;

    public class MovieIDs
    {
        public int trakt;
        public String slug;
        public String imdb;
        public int tmdb;
    }
}
