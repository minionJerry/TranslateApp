package com.test.translateapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.translateapp.DatabaseHelper;
import com.test.translateapp.R;
import com.test.translateapp.adapters.ItemClickListener;
import com.test.translateapp.adapters.WordRecyclerViewAdapter;
import com.test.translateapp.models.TextModel;

import java.util.List;


public class HistoryFragment extends Fragment implements ItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.ItemDecoration mDividerItemDecoration;
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorHistoryStatus));
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarHistory);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHistory));
        db = new DatabaseHelper(getActivity());
        mRecyclerView = (RecyclerView)view.findViewById(R.id.historyList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        Log.d("Reading: ", "Reading all texts..");

        List<TextModel> historyItems = db.getAllTexts();
        mAdapter = new WordRecyclerViewAdapter(historyItems);
        mRecyclerView.setAdapter(mAdapter);

        for (TextModel item : historyItems) {
            String log = "Id: "+item.getId()+" ,Text: " + item.getText() + " ,TranslatedText: " + item.getTranslateText() + " , Lang" + item.getLang();
            // Writing items to log
            Log.d("Record: ", log);
        }
        db.close();
        return view;
    }

    @Override
    public void onItemClick(TextModel item) {
        // тут удаляем элемент из БД
            db.changeToNegativeStatus(item);

        //тут удаляем из адаптера.
       //   mAdapter.removeItem(item);
    }
}

