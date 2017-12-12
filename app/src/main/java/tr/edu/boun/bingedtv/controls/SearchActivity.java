package tr.edu.boun.bingedtv.controls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.SearchShowResultAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.SearchResultShow;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class SearchActivity extends AppCompatActivity
{
    private Context context;
    private RecyclerView recyclerView;

    int TRIGGER_SERACH = 900;
    String searchQuery = "";

    Handler searchMessageHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == TRIGGER_SERACH)
            {
                triggerSearch();
            }
        }
    };

    public void triggerSearch()
    {
        Filter(searchQuery);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        final SearchView sv = findViewById(R.id.search_view);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                sv.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                searchQuery = newText;
                searchMessageHandler.removeMessages(TRIGGER_SERACH);
                searchMessageHandler.sendEmptyMessageDelayed(TRIGGER_SERACH, 1000);
                return false;
            }
        });

        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose()
            {
                sv.setPressed(true);
                sv.setQuery("", false);
                searchQuery = "";
                Filter(searchQuery);
                return false;
            }
        });

        recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void Filter(String searchText)
    {
        if (!searchText.isEmpty())
        {
            // search text on trakt.tv api
            StringBuilder url = new StringBuilder();
            url.append(RestConstants.baseServiceAddress).append("search").append("/").append("show").append("?query=").append(searchText);

            JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
            {

                @Override
                public void onResponse(JSONArray response)
                {
                    Log.d("response", response.toString());

                    Gson gson = new Gson();
                    SearchResultShow[] shows = gson.fromJson(response.toString(), SearchResultShow[].class);

                    // populate show list
                    recyclerView.setAdapter(new SearchShowResultAdapter(Arrays.asList(shows)));
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
}
