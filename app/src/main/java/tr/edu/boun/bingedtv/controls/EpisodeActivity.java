package tr.edu.boun.bingedtv.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.requestobjects.HistoryRequest;
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedEpisodeInfo;
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
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.episodeView_progressBar);

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

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext).setTitle("Did you watch the episode?");
            alert.setMessage("Mark this episode as watched?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    setWatchedEpisode();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(mCurrentShow.ids.trakt)
                .append("/").append("seasons").append("/").append(mCurrentSeasonNumber)
                .append("/").append("episodes").append("/").append(mCurrentEpisodeNumber)
                .append("?extended=full");

        TraktApiClient.TraktJsonObjectRequest jsObjRequest = TraktApiClient.getRequest(mContext, url.toString(), new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.d("response", response.toString());

                Gson gson = new Gson();
                mCurrentEpisode = gson.fromJson(response.toString(), ExtendedEpisodeInfo.class);

                updateUI(mCurrentEpisode);
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("VolleyError", error.getMessage());
            }
        });

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void setWatchedEpisode()
    {
        if (mCurrentEpisode == null || mCurrentEpisode.ids == null) {
            Toast.makeText(mContext, "Episode info not loaded yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync/history");

        HistoryRequest requestObj = new HistoryRequest();
        requestObj.episodes = new ArrayList<>();
        requestObj.episodes.add(new HistoryRequest.HistoryEpisode(mCurrentEpisode.ids));

        JSONObject jsonRequest = null;
        try {
            Gson gson = new Gson();
            jsonRequest = new JSONObject(gson.toJson(requestObj));
        } catch (JSONException e) {
            e.printStackTrace();
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            return;
        }

        TraktApiClient.TraktJsonObjectRequest jsObjRequest = TraktApiClient.postRequest(mContext, url.toString(), jsonRequest, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
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
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e("VolleyError", error.toString());
                Toast.makeText(mContext, "Failed to mark episode as watched.", Toast.LENGTH_SHORT).show();
            }
        });

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);

    }
}
