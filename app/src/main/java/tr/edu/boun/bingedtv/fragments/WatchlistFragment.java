package tr.edu.boun.bingedtv.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import tr.edu.boun.bingedtv.adapters.TrendingShowListAdapter;
import tr.edu.boun.bingedtv.adapters.WatchlistAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.TrendingShow;
import tr.edu.boun.bingedtv.models.responseobjects.WatchlistShow;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

import static android.content.Context.MODE_PRIVATE;

public class WatchlistFragment extends Fragment
{
    private Context context;
    private RecyclerView recyclerView;


    public WatchlistFragment()
    {
        // Required empty public constructor
    }

    public static WatchlistFragment newInstance()
    {
        WatchlistFragment fragment = new WatchlistFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        // Set the adapter
        if(view instanceof RecyclerView)
        {
            context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            // get trending show list
            GetWatchlist();
        }
        return view;
    }

    public void GetWatchlist()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync").append("/").append("watchlist").append("/").append("shows");

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                WatchlistShow[] shows = gson.fromJson(response.toString(), WatchlistShow[].class);

                // populate show list
                recyclerView.setAdapter(new WatchlistAdapter(Arrays.asList(shows)));
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
                SharedPreferences sp = context.getSharedPreferences("credentials", MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("trakt-api-version", "2");
                params.put("trakt-api-key", RestConstants.clientID);
                params.put("Authorization", "Bearer " + sp.getString("access_token",""));

                return params;
            }
        };

        TraktService.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
