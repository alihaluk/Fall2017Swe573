package tr.edu.boun.bingedtv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.ShowPeople;
import tr.edu.boun.bingedtv.models.responseobjects.TrendingShow;

/**
 * Created by haluks on 14.12.2017.
 */

public class ShowCastAdapter extends RecyclerView.Adapter<ShowCastAdapter.ViewHolder>
{
    private final List<ShowPeople.Cast> mValues;

    public ShowCastAdapter(List<ShowPeople.Cast> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_trendingshow, parent, false);
        return new ShowCastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).person.name);
        holder.mWatcherCountView.setText(mValues.get(position).character);
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
        public ShowPeople.Cast mItem;

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
