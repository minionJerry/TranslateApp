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
import android.widget.Toast;

import com.test.translateapp.DatabaseHelper;
import com.test.translateapp.R;
import com.test.translateapp.adapters.WordRecyclerViewAdapter;
import com.test.translateapp.models.TextModel;

import java.util.List;

public class FavoritesFragment extends Fragment {

    DatabaseHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.ItemDecoration mDividerItemDecoration;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorites_list, container, false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorFavoritesStatus));
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarFavorites);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFavorites));

        db = new DatabaseHelper(getActivity());
        mRecyclerView = (RecyclerView)view.findViewById(R.id.favoritesList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        Log.d("Reading: ", "Reading all contacts..");

        List<TextModel> favorites = db.getAllFavoriteTexts();
        if (favorites==null) Toast.makeText(getContext(),"There is no favorites here",Toast.LENGTH_SHORT).show();
        else {
            mAdapter = new WordRecyclerViewAdapter(favorites);
            mRecyclerView.setAdapter(mAdapter);

            for (TextModel item : favorites) {
                String log = "Id: " + item.getId() + " ,Text: " + item.getText() + " ,TranslatedText: " + item.getTranslateText() + " , Lang" + item.getLang();
                Log.d("Record: ", log);
            }
        }
        db.close();
        return view;
    }
}
