package tr.edu.boun.bingedtv.models.requestobjects;

import java.util.List;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Ids;

public class HistoryRequest {
    public List<HistoryEpisode> episodes;

    public static class HistoryEpisode {
        public Ids ids;

        public HistoryEpisode(Ids ids) {
            this.ids = ids;
        }
    }
}
