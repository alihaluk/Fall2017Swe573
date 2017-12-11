package tr.edu.boun.bingedtv.models.standardmediaobjects;

/**
 * Created by haluks on 26.11.2017.
 */

public class Episode
{
    /**
     * {
     "season": 1,
     "number": 1,
     "title": "Pilot",
     "ids": {
     "trakt": 16,
     "tvdb": 349232,
     "imdb": "tt0959621",
     "tmdb": 62085,
     "tvrage": 637041
     }
     }
     */

    public int season;
    public int number;
    public String title;
    public Ids ids;
}
