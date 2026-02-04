package tr.edu.boun.bingedtv.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.adapters.UpcomingListAdapter;
import tr.edu.boun.bingedtv.adapters.WatchlistAdapter;
import tr.edu.boun.bingedtv.models.responseobjects.MyUpcomingShow;
import tr.edu.boun.bingedtv.models.responseobjects.WatchlistShow;
import tr.edu.boun.bingedtv.services.TraktApiClient;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

import static android.content.Context.MODE_PRIVATE;

public class UpcomingFragment extends Fragment
{
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public UpcomingFragment()
    {
        // Required empty public constructor
    }

    public static UpcomingFragment newInstance()
    {
        UpcomingFragment fragment = new UpcomingFragment();
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
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);

        if(recyclerView != null)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            GetUpcomingShows();
        }
        return view;
    }

    public void GetUpcomingShows()
    {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        java.util.Date cal = Calendar.getInstance().getTime();

        String start_date = dateFormat.format(cal);

        String days = "15";

        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("calendars").append("/").append("my").append("/").append("shows").append("/").append(start_date).append("/").append(days);

        TraktApiClient.TraktJsonArrayRequest jsObjRequest = new TraktApiClient.TraktJsonArrayRequest(context, Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.d("response", response.toString());

                Gson gson = new Gson();
                MyUpcomingShow[] shows = gson.fromJson(response.toString(), MyUpcomingShow[].class);

                // populate show list
                recyclerView.setAdapter(new UpcomingListAdapter(Arrays.asList(shows)));
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
