package tr.edu.boun.bingedtv.models.responseobjects;

import tr.edu.boun.bingedtv.models.standardmediaobjects.Ids;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Person;

/**
 * Created by haluks on 14.12.2017.
 */

public class ShowPeople
{
    public Cast[] cast;
    public Crew[] crew;

    public class Cast
    {
        public String character;
        public Person person;
    }

    public class Crew
    {
        public String job;
        public Person person;
    }
}
