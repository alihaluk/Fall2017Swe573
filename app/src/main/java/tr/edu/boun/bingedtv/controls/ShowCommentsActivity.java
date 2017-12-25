package tr.edu.boun.bingedtv.controls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.ShowCastAdapter;
import tr.edu.boun.bingedtv.adapters.ShowCommentAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.ShowComment;
import tr.edu.boun.bingedtv.models.responseobjects.ShowPeople;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class ShowCommentsActivity extends AppCompatActivity
{
    private Context context;
    private RecyclerView recyclerView;

    private int showTraktID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        context = this;
        showTraktID = getIntent().getIntExtra("traktID",-1);

        recyclerView = findViewById(R.id.comment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetShowComments();
    }

    public void GetShowComments()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append(showTraktID).append("/").append("comments/newest");

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();

                ShowComment[] people = gson.fromJson(response.toString(), ShowComment[].class);

                // populate show list
                recyclerView.setAdapter(new ShowCommentAdapter(Arrays.asList(people)));
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
