package com.test.translateapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.translateapp.DatabaseHelper;
import com.test.translateapp.R;
import com.test.translateapp.adapters.WordRecyclerViewAdapter;
import com.test.translateapp.models.TextModel;

import java.util.List;


public class HistoryFragment extends Fragment {

    DatabaseHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.ItemDecoration mDividerItemDecoration;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        db = new DatabaseHelper(getActivity());
        mRecyclerView = (RecyclerView)view.findViewById(R.id.historyList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        // Reading all favorites
        Log.d("Reading: ", "Reading all contacts..");
        List<TextModel> historyItems = db.getAllTexts();
        mAdapter = new WordRecyclerViewAdapter(historyItems);
        mRecyclerView.setAdapter(mAdapter);

        for (TextModel item : historyItems) {
            String log = "Id: "+item.getId()+" ,Text: " + item.getText() + " ,TranslatedText: " + item.getTranslateText() + " , Lang" + item.getLang();
            // Writing items to log
            Log.d("Name: ", log);
        }
        db.close();
        return view;
    }
}
