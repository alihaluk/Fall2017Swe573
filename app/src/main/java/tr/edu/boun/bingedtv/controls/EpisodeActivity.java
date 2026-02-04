package tr.edu.boun.bingedtv.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedShowInfo;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Episode;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Ids;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;
import tr.edu.boun.bingedtv.services.TraktApiClient;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class EpisodeActivity extends AppCompatActivity
{
    private Context mContext;
    private Show mCurrentShow;
    private Integer mCurrentSeasonNumber;
    private Integer mCurrentEpisodeNumber;
    private ExtendedEpisodeInfo mCurrentEpisode;

    private TextView episodeTitle;
    private TextView episodeAirTime;
    private TextView episodeDescription;

    private Button btnEpisodeComments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        mContext = this;

        Gson gson = new Gson();
        mCurrentShow = gson.fromJson(getIntent().getStringExtra("currentShow"), Show.class);
        mCurrentSeasonNumber = getIntent().getIntExtra("seasonNumber", -1);
        mCurrentEpisodeNumber = getIntent().getIntExtra("episodeNumber", -1);

        episodeTitle = findViewById(R.id.episodeView_title);
        episodeAirTime = findViewById(R.id.episodeView_airtime);
        episodeDescription = findViewById(R.id.episodeView_description);

        btnEpisodeComments = findViewById(R.id.episodeView_btn_comments);
        btnEpisodeComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext.getApplicationContext(), ShowCommentsActivity.class);
                i.putExtra("traktID", mCurrentShow.ids.trakt);
                i.putExtra("seasonNumber", mCurrentSeasonNumber);
                i.putExtra("episodeNumber", mCurrentEpisodeNumber);
                mContext.getApplicationContext().startActivity(i);
            }
        });

        GetEpisodeInfo();
    }

    public void updateUI(ExtendedEpisodeInfo ep)
    {
        if (ep != null)
        {
            episodeTitle.setText(ep.title);
            episodeAirTime.setText("SEASON " + ep.season + " EPISODE " + ep.number);
            episodeDescription.setText(ep.overview);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.episodeview_menu, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.episode_watched_action) {

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext).setTitle("Did you watched the episode?");
            alert.setMessage("marks as watched the episode");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    setWatchedEpisode();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            });
            alert.create().show();


        }
        return false;
    }

    public void GetEpisodeInfo()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(mCurrentShow.ids.trakt)
                .append("/").append("seasons").append("/").append(mCurrentSeasonNumber)
                .append("/").append("episodes").append("/").append(mCurrentEpisodeNumber)
                .append("?extended=full");

        TraktApiClient.TraktJsonObjectRequest jsObjRequest = new TraktApiClient.TraktJsonObjectRequest(mContext, Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                try
                {
                    Gson gson = new Gson();
                    mCurrentEpisode = new ExtendedEpisodeInfo();
                    mCurrentEpisode.season = response.getInt("season");
                    mCurrentEpisode.number = response.getInt("number");
                    mCurrentEpisode.title = response.getString("title");
                    mCurrentEpisode.ids = gson.fromJson(response.getString("ids"), Ids.class);
                    mCurrentEpisode.first_aired = response.getString("first_aired");
                    mCurrentEpisode.overview = response.getString("overview");

                } catch(JSONException e)
                {
                    e.printStackTrace();
                }

                updateUI(mCurrentEpisode);
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("VolleyError", error.getMessage());
            }
        });

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void setWatchedEpisode()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync/history");
        /**
         * {
         "shows": [
         {
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
         ]
         }
         */

        JSONObject jsonRequest = new JSONObject();
        try
        {
            Gson gson = new Gson();
            JSONObject ids = new JSONObject(gson.toJson(mCurrentEpisode.ids));
            JSONObject episode = new JSONObject();
            episode.put("ids", ids);

            org.json.JSONArray episodes = new org.json.JSONArray();
            episodes.put(episode);

            jsonRequest.put("episodes", episodes);
        } catch(JSONException e)
        {
            e.printStackTrace();
            return;
        }

        TraktApiClient.TraktJsonObjectRequest jsObjRequest = new TraktApiClient.TraktJsonObjectRequest(mContext, Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                try
                {
                    if (response.has("added"))
                    {
                        Toast.makeText(mContext, "Episode marked as watched!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Something wrong, try again later.", Toast.LENGTH_SHORT).show();
                    }


                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("VolleyError", error.toString());
                Toast.makeText(mContext, "Failed to mark episode as watched.", Toast.LENGTH_SHORT).show();
            }
        });

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);

    }

    public class ExtendedEpisodeInfo extends Episode
    {
        /**
         * {
         "season": 1,
         "number": 1,
         "title": "Winter Is Coming",
         "ids": {
         "trakt": 36440,
         "tvdb": 3254641,
         "imdb": "tt1480055",
         "tmdb": 63056,
         "tvrage": null
         },
         "number_abs": null,
         "overview": "Ned Stark, Lord of Winterfell learns that his mentor, Jon Arryn, has died and that King Robert is on his way north to offer Ned Arryn’s position as the King’s Hand. Across the Narrow Sea in Pentos, Viserys Targaryen plans to wed his sister Daenerys to the nomadic Dothraki warrior leader, Khal Drogo to forge an alliance to take the throne.",
         "first_aired": "2011-04-18T01:00:00.000Z",
         "updated_at": "2014-08-29T23:16:39.000Z",
         "rating": 9,
         "votes": 111,
         "comment_count": 92,
         "available_translations": [
         "en"
         ],
         "runtime": 58
         }
         */

        public String overview;
        public String first_aired;
    }
}
