package tr.edu.boun.bingedtv.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.controls.ShowOverviewActivity;
import tr.edu.boun.bingedtv.models.responseobjects.WatchlistShow;

/**
 * Created by haluks on 13.12.2017.
 */

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder>
{
    private final List<WatchlistShow> mValues;
    private Context mContext;

    public WatchlistAdapter(List<WatchlistShow> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_watchlist, parent, false);
        return new WatchlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).show.title);
        holder.mInfoView.setText(mValues.get(position).show.year.toString());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // open show_overview page
                int traktID = holder.mItem.show.ids.trakt;
                Intent i = new Intent(mContext.getApplicationContext(), ShowOverviewActivity.class);
                i.putExtra("traktID", traktID);
                mContext.getApplicationContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mInfoView;
        public WatchlistShow mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.watchlist_title);
            mInfoView = (TextView) view.findViewById(R.id.watchlist_content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
