package tr.edu.boun.bingedtv.models.responseobjects;

import java.util.List;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;

/**
 * Created by haluks on 12.12.2017.
 */

public class ExtendedShowInfo
{
    /**
     * {
     "title": "Game of Thrones",
     "year": 2011,
     "ids": {
     "  trakt": 353,
        "slug": "game-of-thrones",
        "tvdb": 121361,
        "imdb": "tt0944947",
        "tmdb": 1399,
        "tvrage": 24493
     },
     "overview": "Game of Thrones is an American fantasy drama television series created for HBO by David Benioff and D. B. Weiss. It is an adaptation of A Song of Ice and Fire, George R. R. Martin's series of fantasy novels, the first of which is titled A Game of Thrones.\n\nThe series, set on the fictional continents of Westeros and Essos at the end of a decade-long summer, interweaves several plot lines. The first follows the members of several noble houses in a civil war for the Iron Throne of the Seven Kingdoms; the second covers the rising threat of the impending winter and the mythical creatures of the North; the third chronicles the attempts of the exiled last scion of the realm's deposed dynasty to reclaim the throne. Through its morally ambiguous characters, the series explores the issues of social hierarchy, religion, loyalty, corruption, sexuality, civil war, crime, and punishment.",
     "first_aired": "2011-04-18T01:00:00.000Z",
     "airs": {
        "day": "Sunday",
        "time": "21:00",
        "timezone": "America/New_York"
     },
     "runtime": 60,
     "certification": "TV-MA",
     "network": "HBO",
     "country": "us",
     "updated_at": "2014-08-22T08:32:06.000Z",
     "trailer": null,
     "homepage": "http://www.hbo.com/game-of-thrones/index.html",
     "status": "returning series",
     "rating": 9,
     "votes": 111,
     "comment_count": 92,
     "language": "en",
     "available_translations": [
     "en",
     "tr",
     "sk",
     "de",
     "ru",
     "fr",
     "hu",
     "zh",
     "el",
     "pt",
     "es",
     "bg",
     "ro",
     "it",
     "ko",
     "he",
     "nl",
     "pl"
     ],
     "genres": [
     "drama",
     "fantasy"
     ],
     "aired_episodes": 50
     }
     */

    public String title;
    public int year;
    public Show.ShowIDs ids;
    public String overview;
    public String first_aired;
    public Air airs;
    public int runtime;
    public String certification;
    public String network;
    public String country;
    public String updated_at;
    public String trailer;
    public String homepage;
    public String status;
    public Double rating;
    public int votes;
    public int comment_count;
    public String language;
    public String[] available_translations;
    public String[] genres;
    public int aired_episodes;

    public class Air {
        public String day;
        public String time;
        public String timezone;
    }
}
