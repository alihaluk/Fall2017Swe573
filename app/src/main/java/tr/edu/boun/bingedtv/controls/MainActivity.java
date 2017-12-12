package tr.edu.boun.bingedtv.controls;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.controls.TraktAuthActivity;
import tr.edu.boun.bingedtv.fragments.ExploreFragment;
import tr.edu.boun.bingedtv.fragments.ProfileFragment;
import tr.edu.boun.bingedtv.fragments.ShowsFragment;
import tr.edu.boun.bingedtv.fragments.UpcomingFragment;
import tr.edu.boun.bingedtv.fragments.WatchlistFragment;
import tr.edu.boun.bingedtv.models.standardmediaobjects.Show;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch(item.getItemId())
            {
                case R.id.nav_shows:
                    replaceFragment(ShowsFragment.newInstance(),"Shows");
                    return true;
                case R.id.nav_explore:
                    replaceFragment(ExploreFragment.newInstance(),"Explore");
                    return true;
                case R.id.nav_upcoming:
                    replaceFragment(UpcomingFragment.newInstance(),"Upcoming");
                    return true;
                case R.id.nav_watchlist:
                    replaceFragment(WatchlistFragment.newInstance(),"Watchlist");
                    return true;
                case R.id.nav_profile:
                    replaceFragment(ProfileFragment.newInstance(),"Profile");
                    return true;
            }

            return false;
        }

    };

    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newFragment, tag)
                .commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, ShowsFragment.newInstance(), "Shows")
                .commit();

        Intent i = new Intent(this, TraktAuthActivity.class);
        startActivity(i);

        TraktService.getInstance(this).Authorize();

//        GetTrendingShows();
//        GetPopularShows();
    }

    public void GetPopularShows()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("shows").append("/").append("popular");

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("response", response.toString());

                Gson gson = new Gson();
                Show[] shows = gson.fromJson(response.toString(), Show[].class);

                // populate show list
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

        TraktService.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
