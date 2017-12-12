package tr.edu.boun.bingedtv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.TrendingShow;

public class TrendingShowListAdapter extends RecyclerView.Adapter<TrendingShowListAdapter.ViewHolder>
{

    private final List<TrendingShow> mValues;

    public TrendingShowListAdapter(List<TrendingShow> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_trendingshow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).show.title);
        holder.mWatcherCountView.setText(mValues.get(position).watchers.toString());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

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
        public final TextView mWatcherCountView;
        public TrendingShow mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.trendingShow_title);
            mWatcherCountView = (TextView) view.findViewById(R.id.trendingShow_content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
