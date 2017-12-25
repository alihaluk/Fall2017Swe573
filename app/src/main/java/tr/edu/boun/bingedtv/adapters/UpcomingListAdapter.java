package tr.edu.boun.bingedtv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.MyUpcomingShow;

/**
 * Created by haluks on 13.12.2017.
 */

public class UpcomingListAdapter extends RecyclerView.Adapter<UpcomingListAdapter.ViewHolder>
{
    private final List<MyUpcomingShow> mValues;
    private Context mContext;

    public UpcomingListAdapter(List<MyUpcomingShow> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_upcomingshow, parent, false);
        return new UpcomingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mShowTitleView.setText(mValues.get(position).show.title);
        holder.mEpisodeTitleView.setText(mValues.get(position).episode.title);

        DateFormat m_ISO8601Local = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try
        {
            Date dt = m_ISO8601Local.parse(mValues.get(position).first_aired);

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);

            holder.mEpisodeAirTimeView.setText(dateFormat.format(dt));
        } catch(ParseException e)
        {
            e.printStackTrace();
        }

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
        public final TextView mShowTitleView;
        public final TextView mEpisodeTitleView;
        public final TextView mEpisodeAirTimeView;
        public MyUpcomingShow mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mShowTitleView = (TextView) view.findViewById(R.id.upcoming_showTitle);
            mEpisodeTitleView = (TextView) view.findViewById(R.id.upcoming_episodeTitle);
            mEpisodeAirTimeView = (TextView) view.findViewById(R.id.upcoming_episodeAirTime);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mEpisodeTitleView.getText() + "'";
        }
    }
}
