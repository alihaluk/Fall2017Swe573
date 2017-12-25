package tr.edu.boun.bingedtv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tr.edu.boun.bingedtv.R;
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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).ShowName);
        holder.mInfoView.setText(String.format("%d behind", mValues.get(position).aired - mValues.get(position).completed));
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public void updateShowItem(WatchedShowItem updatedShow)
    {
        int showIndex = -1;
        for(int i=0; i<mValues.size(); i++)
        {
            if (mValues.get(i).ShowId.equals(updatedShow.ShowId))
            {
                showIndex = i;
                break;
            }
        }

        if (showIndex < 0)
        {
            mValues.add(updatedShow);
        }
        else
        {
            mValues.get(showIndex).aired = updatedShow.aired;
            mValues.get(showIndex).completed = updatedShow.completed;
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
