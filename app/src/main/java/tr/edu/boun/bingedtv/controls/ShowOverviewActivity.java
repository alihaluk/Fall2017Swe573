package tr.edu.boun.bingedtv.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedShowInfo;
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedShowSeasonInfo;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class ShowOverviewActivity extends AppCompatActivity
{
    private Context mContext;
    private int showTraktID;
    private Show currentShow;

    private TextView showTitle;
    private TextView showYear;
    private TextView showDescription;
    private TextView showGenres;
    private RecyclerView recyclerView;

    private Button btnShowCast;
    private Button btnShowComments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_overview);

        mContext = this;
        showTraktID = getIntent().getIntExtra("traktID",-1);

        showTitle = findViewById(R.id.overview_title);
        showYear = findViewById(R.id.overview_year);
        showDescription = findViewById(R.id.overview_description);
        showGenres = findViewById(R.id.overview_genres);

        btnShowCast = findViewById(R.id.overview_btn_cast);
        btnShowCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext.getApplicationContext(), ShowCastActivity.class);
                i.putExtra("traktID", showTraktID);
                mContext.getApplicationContext().startActivity(i);
            }
        });

        btnShowComments = findViewById(R.id.overview_btn_comments);
        btnShowComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(mContext.getApplicationContext(), ShowCommentsActivity.class);
                i.putExtra("traktID", showTraktID);
                mContext.getApplicationContext().startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.season_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        getExtendedShowInfo();
        GetShowSeasonInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.showoverview_menu, menu);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.overview_addWatchlist_action) {
            addToWatchlist();
        } else if (item.getItemId() == R.id.overview_watched_action) {
            // dialog, ask, did you binged! all episodes ? (means set watched all espidoes of the show)

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext).setTitle("Did you binged the show?");
            alert.setMessage("marks as watched all episodes of the show");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    bingedAllShowEpisodes();
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

    public void updateUI(ExtendedShowInfo show)
    {
        showTitle.setText(show.title);
        showYear.setText(String.valueOf(show.year));
        showDescription.setText(show.overview);
        showGenres.setText(Arrays.toString(show.genres));
    }

    public void getExtendedShowInfo ()
    {
        // search text on trakt.tv api
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(showTraktID).append("?extended=full");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                ExtendedShowInfo showInfo = gson.fromJson(response.toString(), ExtendedShowInfo.class);

                currentShow = new Show();
                currentShow.title = showInfo.title;
                currentShow.year = showInfo.year;
                currentShow.ids = showInfo.ids;

                // update UI
                updateUI(showInfo);
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

    public void bingedAllShowEpisodes()
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

        Show[] param = new Show[1];
        param[0] = currentShow;

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
                        Toast.makeText(mContext, "Show binged successfully!", Toast.LENGTH_SHORT).show();
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

    public void checkinShow()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("checkin");

        /**
         * {
         "show": {
         "title": "Breaking Bad"
         },
         "episode": {
         "season": 1,
         "number": 1
         },
         "sharing": {
         "facebook": true,
         "twitter": true,
         "tumblr": false
         },
         "message": "I'm the one who knocks!",
         "app_version": "1.0",
         "app_date": "2014-09-22"
         }
         */

//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("show", "{\"title\": \"" + currentShow.title + "\"}");
//        params.put("episode", "{\"season\": 1, \"number\": 1}");
//        params.put("app_version", "\"1.0\"");
//        params.put("app_date", "\"25-12-2017\"");

        JSONObject jsonRequest = null;
        try
        {
            jsonRequest = new JSONObject(
                    "{ \"show\": " + "{\"title\": \"" + currentShow.title + "\"},"
                            + "\"episode\": " + "{\"season\": 1, \"number\": 1},"
                            + "\"app_version\": \"1.0\","
                            + "\"app_date\": \"25-12-2017\""
                            + "}");
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
                    if (response.getString("watched_at") != null)
                    {
                        Toast.makeText(mContext, "Show checked successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Show did not checked!", Toast.LENGTH_SHORT).show();
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
                if (error.networkResponse.statusCode == 409)
                {
                    Toast.makeText(mContext, "There is already a checkin in progress.", Toast.LENGTH_SHORT).show();
                }
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

    public void addToWatchlist()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync").append("/").append("watchlist");

        Show[] param = new Show[1];
        param[0] = currentShow;

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
                    if (response.getJSONObject("added").getInt("shows") > 0)
                    {
                        // added !
                        Toast.makeText(mContext, "Show added to watchlist successfully!", Toast.LENGTH_SHORT).show();
                    }
                    if (response.getJSONObject("existing").getInt("shows") > 0)
                    {
                        // already added !
                        Toast.makeText(mContext, "Show has already added to watchlist!", Toast.LENGTH_SHORT).show();
                    }
                    if (response.getJSONObject("not_found").getJSONArray("shows").length() > 0)
                    {
                        // not found !
                        Toast.makeText(mContext, "Show is unexpectedly not found, things are getting stranger here.", Toast.LENGTH_SHORT).show();
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
                Log.e("VolleyError", error.getMessage());
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

    public void GetShowSeasonInfo()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(showTraktID).append("/").append("seasons").append("?extended=full");

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                ExtendedShowSeasonInfo[] showSeasons = gson.fromJson(response.toString(), ExtendedShowSeasonInfo[].class);

                // populate show list
                recyclerView.setAdapter(new SeasonListAdapter(Arrays.asList(showSeasons)));
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

    public class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.ViewHolder>
    {
        private final List<ExtendedShowSeasonInfo> mValues;
        private Context mContext;

        public SeasonListAdapter(List<ExtendedShowSeasonInfo> items)
        {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_watchlist, parent, false);
            return new SeasonListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).title);
            holder.mInfoView.setText(String.valueOf(mValues.get(position).episode_count) + " episodes");

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // open season page
                    Gson gson = new Gson();
                    Intent i = new Intent(mContext.getApplicationContext(), SeasonOverviewActivity.class);
                    i.putExtra("currentShow", gson.toJson(currentShow));
                    i.putExtra("seasonNumber", mValues.get(position).number);
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
            public final TextView mInfoView;
            public ExtendedShowSeasonInfo mItem;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.watchlist_title);
                mInfoView = (TextView) view.findViewById(R.id.watchlist_content);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }
}
