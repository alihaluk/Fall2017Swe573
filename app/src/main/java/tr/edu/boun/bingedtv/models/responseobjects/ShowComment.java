package tr.edu.boun.bingedtv.models.responseobjects;

/**
 * Created by haluks on 14.12.2017.
 */

public class ShowComment
{
    /**
     * [
     {
     "id": 8,
     "parent_id": 0,
     "created_at": "2011-03-25T22:35:17.000Z",
     "updated_at": "2011-03-25T22:35:17.000Z",
     "comment": "Great show!",
     "spoiler": false,
     "review": false,
     "replies": 1,
     "likes": 0,
     "user_rating": 8,
     "user": {
     "username": "sean",
     "private": false,
     "name": "Sean Rudford",
     "vip": true,
     "vip_ep": false,
     "ids": {
     "slug": "sean"
     }
     }
     }
     ]
     */

    public int id;
    public int parent_id;
    public String created_at;
    public String updated_at;
    public String comment;
    public Boolean spoiler;
    public Boolean review;
    public int replies;
    public int likes;
    public Double user_rating;
    public TraktUser user;

    public class TraktUser
    {
        public String username;
        public Boolean Private;
        public String name;
        public Boolean vip;
        public Boolean vip_ep;
        public UserIDs ids;

        public class UserIDs
        {
            public String slug;
        }

    }
}
