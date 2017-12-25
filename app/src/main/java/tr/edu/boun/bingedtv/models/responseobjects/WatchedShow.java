package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 25.12.2017.
 */

public class WatchedShow
{
    /**
     *   {
     "plays": 56,
     "last_watched_at": "2014-10-11T17:00:54.000Z",
     "show": {
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
     },
     */

    public int plays;
    public String last_watched_at;
    public Show show;
}
