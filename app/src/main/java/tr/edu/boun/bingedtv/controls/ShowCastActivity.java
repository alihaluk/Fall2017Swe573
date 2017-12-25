package tr.edu.boun.bingedtv.controls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import tr.edu.boun.bingedtv.adapters.ShowCastAdapter;
import tr.edu.boun.bingedtv.adapters.TrendingShowListAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.ShowPeople;
import tr.edu.boun.bingedtv.models.responseobjects.TrendingShow;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class ShowCastActivity extends AppCompatActivity
{
    private Context context;
    private RecyclerView recyclerView;

    private int showTraktID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_castctivity);

        context = this;
        showTraktID = getIntent().getIntExtra("traktID",-1);

        recyclerView = findViewById(R.id.cast_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        GetShowPeople();
    }

    public void GetShowPeople()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(showTraktID).append("/").append("people");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                ShowPeople.Cast[] people = null;
                try
                {
                    people = gson.fromJson(response.getJSONArray("cast").toString(), ShowPeople.Cast[].class);
                } catch(JSONException e)
                {
                    e.printStackTrace();
                }

                // populate show list
                recyclerView.setAdapter(new ShowCastAdapter(Arrays.asList(people)));
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

        TraktService.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
