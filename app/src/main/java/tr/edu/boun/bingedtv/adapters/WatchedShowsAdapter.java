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
import tr.edu.boun.bingedtv.models.responseobjects.WatchedShowItem;

/**
 * Created by haluks on 25.12.2017.
 */

public class WatchedShowsAdapter extends RecyclerView.Adapter<WatchedShowsAdapter.ViewHolder>
{
    private final List<WatchedShowItem> mValues;
    private Context mContext;

    public WatchedShowsAdapter(List<WatchedShowItem> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_shows, parent, false);
        return new WatchedShowsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).ShowName);
        holder.mInfoView.setText(String.format("%d behind", mValues.get(position).aired - mValues.get(position).completed));

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // open show_overview page
                int traktID = Integer.parseInt(holder.mItem.ShowId);
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

    public void setItems(List<WatchedShowItem> items) {
        mValues.clear();
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public void updateItem(int position, WatchedShowItem item) {
        if (position >= 0 && position < mValues.size()) {
            mValues.set(position, item);
            notifyItemChanged(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mInfoView;
        public WatchedShowItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.showlist_title);
            mInfoView = (TextView) view.findViewById(R.id.showlist_content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
