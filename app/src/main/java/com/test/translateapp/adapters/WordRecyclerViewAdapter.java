package com.test.translateapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.translateapp.R;
import com.test.translateapp.models.TextModel;

import java.util.List;


public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.ViewHolder> {

    private final List<TextModel> mValues;

    public WordRecyclerViewAdapter(List<TextModel> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_word_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mText.setText(mValues.get(position).getText());
        holder.mTranslatedText.setText(mValues.get(position).getTranslateText());
        if (mValues.get(position).isFavorite()==1) holder.mIsFavorite.setImageResource(R.drawable.ic_favorite);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public TextView mTranslatedText;
        public ImageView mIsFavorite;

        public ViewHolder(View view) {
            super(view);
            mText = (TextView)view.findViewById(R.id.text);
            mTranslatedText = (TextView)view.findViewById(R.id.translatedText);
            mIsFavorite = (ImageView)view.findViewById(R.id.isFavorite);
        }
    }
}
