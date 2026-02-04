package tr.edu.boun.bingedtv.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.WatchedShowsAdapter;
import tr.edu.boun.bingedtv.controls.SearchActivity;
import tr.edu.boun.bingedtv.models.responseobjects.WatchedShow;
import tr.edu.boun.bingedtv.models.responseobjects.WatchedShowItem;
import tr.edu.boun.bingedtv.services.TraktApiClient;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class ShowsFragment extends Fragment
{
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public ShowsFragment()
    {
    }

    public static ShowsFragment newInstance()
    {
        ShowsFragment fragment = new ShowsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_shows, container, false);

        context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);

        if (recyclerView != null)
        {
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
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        // Do not reset adapter here with empty list.

        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("sync").append("/").append("watched").append("/").append("shows?extended=noseasons");

        TraktApiClient.TraktJsonArrayRequest jsObjRequest = TraktApiClient.getArrayRequest(context, url.toString(), new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.d("response", response.toString());

                Gson gson = new Gson();
                WatchedShow[] shows = gson.fromJson(response.toString(), WatchedShow[].class);

                // Initialize list with items having 0 progress
                List<WatchedShowItem> items = new ArrayList<>();
                for (WatchedShow show : shows) {
                    WatchedShowItem item = new WatchedShowItem();
                    item.ShowId = show.show.ids.trakt.toString();
                    item.ShowName = show.show.title;
                    item.aired = 0;
                    item.completed = 0;
                    items.add(item);
                }

                WatchedShowsAdapter adapter = (WatchedShowsAdapter) recyclerView.getAdapter();
                if (adapter == null) {
                    adapter = new WatchedShowsAdapter(items);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setItems(items);
                }


                for (int i = 0; i < shows.length; i++)
                {
                    final WatchedShow show = shows[i];
                    final int position = i; // capture index

                    StringBuilder url = new StringBuilder();
                    url.append(RestConstants.baseServiceAddress)
                            .append("shows").append("/")
                            .append(show.show.ids.trakt.toString())
                            .append("/").append("progress")
                            .append("/").append("watched?hidden=false&specials=false&count_specials=false");

                    TraktApiClient.TraktJsonObjectRequest jsObjRequest = TraktApiClient.getRequest(context, url.toString(), new Response.Listener<JSONObject>()
                    {

                        @Override
                        public void onResponse(JSONObject response)
                        {
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
                                if (wsa != null) {
                                    wsa.updateItem(position, item);
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
                    });

                    TraktService.getInstance(context).addToRequestQueue(jsObjRequest);
                }

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

        TraktService.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
