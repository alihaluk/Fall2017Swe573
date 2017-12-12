package tr.edu.boun.bingedtv.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.controls.SearchActivity;
import tr.edu.boun.bingedtv.controls.TraktAuthActivity;

public class ShowsFragment extends Fragment
{

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
        return inflater.inflate(R.layout.fragment_shows, container, false);
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
}
