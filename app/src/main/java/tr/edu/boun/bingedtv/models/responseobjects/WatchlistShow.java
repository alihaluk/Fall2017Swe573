package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 13.12.2017.
 */

public class WatchlistShow
{
    /**
     *   {
     "listed_at": "2014-09-01T09:10:11.000Z",
     "type": "show",
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
     }
     */

    public String listed_at;
    public String type;
    public Show show;
}
