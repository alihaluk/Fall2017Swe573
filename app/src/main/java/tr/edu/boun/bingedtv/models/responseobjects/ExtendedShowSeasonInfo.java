package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Ids;

/**
 * Created by haluks on 14.12.2017.
 */

public class ExtendedShowSeasonInfo
{
    /**
     *   {
     "number": 0,
     "ids": {
     "trakt": 1,
     "tvdb": 137481,
     "tmdb": 3627,
     "tvrage": null
     },
     "rating": 9,
     "votes": 111,
     "episode_count": 10,
     "aired_episodes": 10,
     "title": "Specials",
     "overview": null,
     "first_aired": "2010-12-06T02:00:00.000Z",
     "network": "HBO"
     },
     */

    public int number;
    public Ids ids;
    public Double rating;
    public int votes;
    public int episode_count;
    public int aired_epsidoes;
    public String title;
    public String overview;
    public String first_aired;
    public String network;

}
