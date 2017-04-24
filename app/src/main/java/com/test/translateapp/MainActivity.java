package com.test.translateapp;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.test.translateapp.fragments.FavoritesFragment;
import com.test.translateapp.fragments.HistoryFragment;
import com.test.translateapp.fragments.TranslateFragment;

public class MainActivity extends AppCompatActivity {

    TranslateFragment tFrag;
    HistoryFragment hFrag;
    FavoritesFragment fFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tFrag = new TranslateFragment();
        hFrag = new HistoryFragment();
        fFrag = new FavoritesFragment();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_translate) {
                   changeContent(tFrag);
                }
                if (tabId == R.id.tab_history) {
                    changeContent(hFrag);
                }
                if (tabId == R.id.tab_favorites) {
                    changeContent(fFrag);
                }
            }
        });
    }

    private void changeContent(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.commit();
    }


}
