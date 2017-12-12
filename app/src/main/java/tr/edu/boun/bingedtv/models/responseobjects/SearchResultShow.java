package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 12.12.2017.
 */

public class SearchResultShow
{
    /**
     *   {
     "type": "show",
     "score": 19.533358,
     "show": {
     "title": "Tron: Uprising",
     "year": 2012,
     "ids": {
     "trakt": 34209,
     "slug": "tron-uprising",
     "tvdb": 258480,
     "imdb": "tt1812523",
     "tmdb": 34356,
     "tvrage": null
     }
     }
     */

    public String type;
    public Double score;
    public Show show;
}
