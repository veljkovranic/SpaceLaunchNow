package me.calebjones.spacelaunchnow.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import me.calebjones.spacelaunchnow.utils.Analytics;

public class BaseFragment extends Fragment {
    private Realm realm;
    private String screenName = "Unknown (Name not set)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        Analytics.from(this).sendScreenView(screenName, screenName + " started.");
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.removeAllChangeListeners();
        realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        Analytics.from(this).sendScreenView(screenName, screenName + " resumed.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Analytics.from(this).notifyGoneBackground();
    }

    public Realm getRealm() {
        return realm;
    }

    public void setScreenName(String screenName){
        this.screenName = screenName;
    }
}
