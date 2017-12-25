package tr.edu.boun.bingedtv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.models.responseobjects.ShowComment;
import tr.edu.boun.bingedtv.models.responseobjects.ShowPeople;

/**
 * Created by haluks on 14.12.2017.
 */

public class ShowCommentAdapter extends RecyclerView.Adapter<ShowCommentAdapter.ViewHolder>
{
    private final List<ShowComment> mValues;

    public ShowCommentAdapter(List<ShowComment> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_showcomment, parent, false);
        return new ShowCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mUserNameView.setText(mValues.get(position).user.username);
        holder.mCommentDateView.setText(mValues.get(position).updated_at);
        holder.mCommentView.setText(mValues.get(position).comment);
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mUserNameView;
        public final TextView mCommentDateView;
        public final TextView mCommentView;
        public ShowComment mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mUserNameView = (TextView) view.findViewById(R.id.comment_username);
            mCommentDateView = (TextView) view.findViewById(R.id.comment_date);
            mCommentView = (TextView) view.findViewById(R.id.comment_text);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mCommentView.getText() + "'";
        }
    }
}
