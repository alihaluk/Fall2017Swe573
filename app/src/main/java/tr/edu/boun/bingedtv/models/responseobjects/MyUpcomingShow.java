package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Episode;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 11.12.2017.
 */

public class MyUpcomingShow
{
    /**
     *
     [
     {
        "first_aired": "2014-07-14T01:00:00.000Z",
        "episode": {
            "season": 7,
            "number": 4,
            "title": "Death is Not the End",
            "ids": {
                "trakt": 443,
                "tvdb": 4851180,
                "imdb": "tt3500614",
                "tmdb": 988123,
                "tvrage": null
            }
        },
        "show": {
            "title": "True Blood",
            "year": 2008,
            "ids": {
                "trakt": 5,
                "slug": "true-blood",
                "tvdb": 82283,
                "imdb5": "tt0844441",
                "tmdb": 10545,
                "tvrage": 12662
            }
        }
     }
     */

    public String first_aired;
    public Episode episode;
    public Show show;

}
