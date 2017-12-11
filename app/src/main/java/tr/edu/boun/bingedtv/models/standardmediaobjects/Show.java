package tr.edu.boun.bingedtv.models.standardmediaobjects;

/**
 * Created by haluks on 26.11.2017.
 */

public class Show
{
    /**
     * {
     "title": "Breaking Bad",
     "year": 2008,
     "ids": {
     "trakt": 1,
     "slug": "breaking-bad",
     "tvdb": 81189,
     "imdb": "tt0903747",
     "tmdb": 1396,
     "tvrage": 18164
     }
     }
     */

    public String title;
    public Integer year;
    public ShowIDs ids;

    public class ShowIDs
    {
        public Integer trakt;
        public String slug;
        public Integer tvdb;
        public String imdb;
        public Integer tmdb;
        public Integer tvrage;
    }
}
