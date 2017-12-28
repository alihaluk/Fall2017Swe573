package tr.edu.boun.bingedtv.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedShowSeasonInfo;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Episode;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class SeasonOverviewActivity extends AppCompatActivity
{
    private Context mContext;
    private Show mCurrentShow;
    private Integer mCurrentSeason;

    private TextView showTitle;
    private TextView seasonDscription;
    private RecyclerView recyclerView;

    private Button btnSeasonComments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_overview);

        mContext = this;

        Gson gson = new Gson();
        mCurrentShow = gson.fromJson(getIntent().getStringExtra("currentShow"), Show.class);
        mCurrentSeason = getIntent().getIntExtra("seasonNumber", -1);

        showTitle = findViewById(R.id.seasonView_showtitle);
        showTitle.setText(mCurrentShow.title);
        seasonDscription = findViewById(R.id.seasonView_description);
        seasonDscription.setText("SEASON " + mCurrentSeason.toString());

        btnSeasonComments = findViewById(R.id.seasonView_btn_comments);
        btnSeasonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext.getApplicationContext(), ShowCommentsActivity.class);
                i.putExtra("traktID", mCurrentShow.ids.trakt);
                i.putExtra("seasonNumber", mCurrentSeason);
                mContext.getApplicationContext().startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.episode_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        GetEpisodeList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.seasonoverview_menu, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.season_overview_watched_action) {
            // dialog, ask, did you binged! all episodes ? (means set watched all espidoes of the show)

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext).setTitle("Did you binged the season?");
            alert.setMessage("marks as watched all episodes of the season");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    bingedAllSeasonEpisodes();
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

    public void GetEpisodeList()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(mCurrentShow.ids.trakt.toString()).append("/").append("seasons").append("/").append(mCurrentSeason.toString());

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                Episode[] seasonEpisodes = gson.fromJson(response.toString(), Episode[].class);

                // populate show list
                recyclerView.setAdapter(new EpisodeListAdapter(Arrays.asList(seasonEpisodes)));
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("VolleyError", error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("trakt-api-version", "2");
                params.put("trakt-api-key", RestConstants.clientID);

                return params;
            }
        };

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void bingedAllSeasonEpisodes()
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

        ShowWithSeasons[] param = new ShowWithSeasons[1];
        param[0] = new ShowWithSeasons(mCurrentShow, mCurrentSeason);

        Gson gson = new Gson();
        String showParam = gson.toJson(param);

        JSONObject jsonRequest = null;
        try
        {
            jsonRequest = new JSONObject("{shows:" + showParam + "}");
        } catch(JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonRequest, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                try
                {
                    if (response.getJSONObject("added") != null)
                    {
                        Toast.makeText(mContext, "Season binged successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Something wrong, try again later.", Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("credentials", MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("trakt-api-version", "2");
                params.put("trakt-api-key", RestConstants.clientID);
                params.put("Authorization", "Bearer " + sp.getString("access_token",""));

                return params;
            }
        };

        TraktService.getInstance(mContext).addToRequestQueue(jsObjRequest);

    }

    public class EpisodeListAdapter extends RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>
    {
        private final List<Episode> mValues;
        private Context mContext;

        public EpisodeListAdapter(List<Episode> items)
        {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_episodelist, parent, false);
            return new EpisodeListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).title);
            holder.mNumberView.setText("Episode " + String.valueOf(mValues.get(position).number));

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // open season page
                    Gson gson = new Gson();
                    Intent i = new Intent(mContext.getApplicationContext(), EpisodeActivity.class);
                    i.putExtra("currentShow", gson.toJson(mCurrentShow));
                    i.putExtra("seasonNumber", mCurrentSeason);
                    i.putExtra("episodeNumber", mValues.get(position).number);
                    mContext.getApplicationContext().startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mNumberView;
            public Episode mItem;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.episodelist_title);
                mNumberView = (TextView) view.findViewById(R.id.episodelist_number);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }

    public class ShowWithSeasons extends Show
    {
        List<WatchedSeason> seasons;

        public ShowWithSeasons(Show baseShow, Integer seasonNumber)
        {
            super();
            this.title = baseShow.title;
            this.year = baseShow.year;
            this.ids = baseShow.ids;
            this.seasons = new ArrayList<>();
            this.seasons.add(new WatchedSeason(seasonNumber));
        }

        public class WatchedSeason
        {
            public Integer number;

            public WatchedSeason(Integer number)
            {
                this.number = number;
            }
        }
    }
}
