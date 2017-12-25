package tr.edu.boun.bingedtv.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.WatchedShowsAdapter;
import tr.edu.boun.bingedtv.controls.SearchActivity;
import tr.edu.boun.bingedtv.models.responseobjects.WatchedShow;
import tr.edu.boun.bingedtv.models.responseobjects.WatchedShowItem;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

import static android.content.Context.MODE_PRIVATE;

public class ShowsFragment extends Fragment
{
    private Context context;
    private RecyclerView recyclerView;

    public ShowsFragment()
    {
    }

    public static ShowsFragment newInstance()
    {
        ShowsFragment fragment = new ShowsFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_shows, container, false);

        // Set the adapter
        if(view instanceof RecyclerView)
        {
            context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            GetWatchedShowsProgress();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.shows_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.showMenu_search) {
            // search activity
            Intent i = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
            startActivity(i);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    public void GetWatchedShowsProgress()
    {
        recyclerView.setAdapter(new WatchedShowsAdapter(new ArrayList<WatchedShowItem>()));

        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync").append("/").append("watched").append("/").append("shows?extended=noseasons");

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                WatchedShow[] shows = gson.fromJson(response.toString(), WatchedShow[].class);

                for (final WatchedShow show : shows)
                {
                    StringBuilder url = new StringBuilder();
                    url.append(RestConstants.baseServiceAddress)
                            .append("shows").append("/")
                            .append(show.show.ids.trakt.toString())
                            .append("/").append("progress")
                            .append("/").append("watched?hidden=false&specials=false&count_specials=false");

                    final MyShowJsonObjectRequest jsObjRequest = new MyShowJsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            Log.d("response", response.toString());

                            try
                            {
                                int aired = response.getInt("aired");
                                int completed = response.getInt("completed");

                                WatchedShowItem item = new WatchedShowItem();
                                item.aired = aired;
                                item.completed = completed;
                                item.ShowId = show.show.ids.trakt.toString();
                                item.ShowName = show.show.title;

                                WatchedShowsAdapter wsa = (WatchedShowsAdapter) recyclerView.getAdapter();
                                wsa.updateShowItem(item);

                                wsa.notifyDataSetChanged();

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
                            SharedPreferences sp = context.getSharedPreferences("credentials", MODE_PRIVATE);

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("trakt-api-version", "2");
                            params.put("trakt-api-key", RestConstants.clientID);
                            params.put("Authorization", "Bearer " + sp.getString("access_token",""));

                            return params;
                        }
                    };
                    jsObjRequest.SetShowInfo(show.show.ids.trakt.toString(), show.show.title);

                    TraktService.getInstance(context).addToRequestQueue(jsObjRequest);

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

    public class MyShowJsonObjectRequest extends JsonObjectRequest
    {
        String ShowID;
        String ShowName;

        public void SetShowInfo(String showID, String name)
        {
            ShowID = showID;
            ShowName = name;
        }

        public MyShowJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
        {
            super(method, url, jsonRequest, listener, errorListener);
        }
    }
}
