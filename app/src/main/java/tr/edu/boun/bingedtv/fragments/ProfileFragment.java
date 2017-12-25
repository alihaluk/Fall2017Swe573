package tr.edu.boun.bingedtv.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.controls.LoginActivity;
import tr.edu.boun.bingedtv.controls.SearchActivity;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment
{
    private Context mContext;

    private ImageView profileImage;
    private TextView profileNameText;
    private TextView profileAboutText;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.profile_logout) {

            // clear credentials and nav to login activity.
            SharedPreferences sp = mContext.getSharedPreferences("credentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("access_token", "");
            editor.putString("refresh_token", "");
            editor.apply();

            Intent i = new Intent(mContext.getApplicationContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();

            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = view.getContext();

        profileImage = view.findViewById(R.id.profile_img);
        profileNameText = view.findViewById(R.id.profile_name);
        profileAboutText = view.findViewById(R.id.profile_about);

        GetProfileInformation();

        return view;
    }

    public void GetProfileInformation()
    {
        StringBuilder url = new StringBuilder();
        url.append(RestConstants.baseServiceAddress).append("users/me?extended=full");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("response", response.toString());

                try
                {
                    String name = response.getString("name");
                    String about = response.getString("about");

                    String avatar_link = response.getJSONObject("images").getJSONObject("avatar").getString("full");

                    Picasso.with(mContext).load(avatar_link).into(profileImage);
                    profileNameText.setText(name);
                    profileAboutText.setText(about);

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
                SharedPreferences sp = mContext.getSharedPreferences("credentials", MODE_PRIVATE);

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
