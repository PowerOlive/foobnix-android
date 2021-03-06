package com.foobnix.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.foobnix.activity.auth.LastfmLoginActivity;
import com.foobnix.activity.auth.VkLoginActivity;
import com.foobnix.activity.hierarchy.GeneralListActivity;
import com.foobnix.adapter.ModelListAdapter;
import com.foobnix.adapter.PrefItemAdapter;
import com.foobnix.model.PrefItem;
import com.foobnix.R;

public class SettingsActivity extends GeneralListActivity<PrefItem> {
    private List<PrefItem> prefs = new ArrayList<PrefItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list);

        prefs.add(new PrefItem(R.drawable.lastfm, "Last.Fm", "User and Audioscrobbler", new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LastfmLoginActivity.class));

            }
        }));
        prefs.add(new PrefItem(R.drawable.vk, "Vkontakte", "Social Network User", new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
            }
        }));

        onActivate(this);
        disableSecondTopLine();
        disablePlayerMenu();
    }

    @Override
    public Class<? extends ModelListAdapter<PrefItem>> getAdapter() {
        return PrefItemAdapter.class;
    }

    @Override
    public List<PrefItem> getInitItems() {
        return prefs;
    }

    @Override
    public void onModelItemClickListener(PrefItem model) {
        model.getAction().run();
    }

}