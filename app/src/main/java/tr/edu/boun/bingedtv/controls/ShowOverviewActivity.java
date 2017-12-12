package tr.edu.boun.bingedtv.controls;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.SearchShowResultAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.ExtendedShowInfo;
import tr.edu.boun.bingedtv.models.responseobjects.SearchResultShow;
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

    private Button btnAddWatchlist;
    private Button btnBinged;

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

        btnAddWatchlist = findViewById(R.id.overview_btn_add2watchlist);
        btnAddWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // call add to watchlist
                addToWatchlist();
            }
        });

        btnBinged = findViewById(R.id.overview_btn_binged);
        btnBinged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // set watched all episodes
            }
        });

        getExtendedShowInfo();
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

    public void addToWatchlist()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync").append("/").append("watchlist");

        Show[] param = new Show[1];
        param[0] = currentShow;

        Gson gson = new Gson();
        String showParam = gson.toJson(param);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("shows", showParam);
        params.put("movies", "[]");
        params.put("episodes", "[]");

        JSONObject jsonRequest =  new JSONObject();
        try
        {
            jsonRequest.put("shows", showParam.replaceAll("\\\\", ""));
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
}
