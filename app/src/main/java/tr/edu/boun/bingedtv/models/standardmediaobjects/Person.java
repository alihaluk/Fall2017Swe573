package tr.edu.boun.bingedtv.models.standardmediaobjects;

/**
 * Created by haluks on 26.11.2017.
 */

public class Person
{
    /**
     * {
     "name": "Bryan Cranston",
     "ids": {
     "trakt": 142,
     "slug": "bryan-cranston",
     "imdb": "nm0186505",
     "tmdb": 17419,
     "tvrage": 1797
     }
     }
     */

    public String name;
    public Ids ids;

    public class PersonIDs
    {
        public int trakt;
        public String slug;
        public String imdb;
        public int tmdb;
        public int tvrage;
    }
}
