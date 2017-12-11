package tr.edu.boun.bingedtv.models.responseobjects;

import java.util.List;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 27.11.2017.
 */

public class TrendingShow
{
    /**
     *
     * [{
     "watchers": 250,
     "show": {
     "title": "Marvel's The Punisher",
     "year": 2017,
     "ids": {
     "trakt": 112663,
     "slug": "marvel-s-the-punisher",
     "tvdb": 331980,
     "imdb": "tt5675620",
     "tmdb": 67178,
     "tvrage": 52104
     }
     }
     },
     */

    public Integer watchers;
    public Show show;
}
