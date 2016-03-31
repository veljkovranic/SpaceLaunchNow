package me.calebjones.spacelaunchnow.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.onesignal.OneSignal;

import java.util.Date;

import me.calebjones.spacelaunchnow.R;
import me.calebjones.spacelaunchnow.content.database.ListPreferences;
import me.calebjones.spacelaunchnow.content.database.SwitchPreferences;
import me.calebjones.spacelaunchnow.utils.TimeRangePickerDialogCustom;
import timber.log.Timber;

public class NestedPreferenceFragment extends PreferenceFragmentCompat implements TimeRangePickerDialogCustom.OnTimeRangeSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    // Give your color picker dialog unique IDs if you
    // have multiple dialog. This will make it possible
    // for you to distinguish between them when you
    // get a result back in your ColorPickerDialogListener.
    private static final int PRIMARY_ID = 0;
    private static final int ACCENT_ID = 1;

    public static final int NESTED_SCREEN_1_KEY = 1;
    public static final int NESTED_SCREEN_2_KEY = 2;
    public static final int NESTED_SCREEN_3_KEY = 3;
    public static final int NESTED_SCREEN_4_KEY = 4;
    private static final String TAG_KEY = "NESTED_KEY";
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private TextView toolbarTitle;
    private static ListPreferences listPreferences;
    private SwitchPreferences switchPreferences;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private static final String HOUR_KEY = "me.calebjones.spacelaunchnow.wear.hourmode";

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class SharedPreferenceListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        final /* synthetic */ SharedPreferences valprefs;

        SharedPreferenceListener(SharedPreferences sharedPreferences) {
            this.valprefs = sharedPreferences;
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Timber.d("onSharedPreferenceChanged:  %s ", key);
            try {
                if (key.equals("notifications")) {
                    if (this.valprefs.getBoolean(key, false)) {
                    }
                }
                if (key.equals("theme") && NestedPreferenceFragment.this.getActivity() != null) {
                    Editor themeEditor = NestedPreferenceFragment.this.getActivity().getSharedPreferences("theme_changed", 0).edit();
                    themeEditor.putBoolean("recreate", true);
                    themeEditor.apply();
                    NestedPreferenceFragment.this.getActivity().recreate();
                }
                if (key.equals("auto_theme") && NestedPreferenceFragment.this.getActivity() != null) {
                    Editor themeEditor = NestedPreferenceFragment.this.getActivity().getSharedPreferences("theme_changed", 0).edit();
                    themeEditor.putBoolean("recreate", true);
                    themeEditor.apply();
                    NestedPreferenceFragment.this.getActivity().recreate();
                }
                if (key.equals("notifications_launch_imminent_updates")){
                    OneSignal.setSubscription(this.valprefs.getBoolean(key, false));
                }
                if (key.equals("wear_hour_mode")){
                    PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/config");
                    putDataMapReq.getDataMap().putBoolean(HOUR_KEY, this.valprefs.getBoolean(key, false));
                    putDataMapReq.getDataMap().putLong("time", new Date().getTime());

                    PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                }
            } catch (NullPointerException e) {

            }
        }
    }

    public static NestedPreferenceFragment newInstance(int key) {
        NestedPreferenceFragment fragment = new NestedPreferenceFragment();
        Bundle args = new Bundle();
        args.putInt(TAG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getActivity() != null) {
            this.toolbarTitle = (TextView) getActivity().findViewById(R.id.title_text);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferenceListener(prefs));
        checkPreferenceResource();

    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    public void onResume() {
        listPreferences = ListPreferences.getInstance(this.context);
        switchPreferences = SwitchPreferences.getInstance(this.context);
        listPreferences.getNightMode();
        super.onResume();
    }

    private void checkPreferenceResource() {
        mGoogleApiClient.connect();
        switch (getArguments().getInt(TAG_KEY)) {
            case NESTED_SCREEN_1_KEY:
                addPreferencesFromResource(R.xml.nested_notification_preferences);
                if (this.toolbarTitle != null) {
                    this.toolbarTitle.setText("Notification");
                }
                break;
            case NESTED_SCREEN_2_KEY:
                addPreferencesFromResource(R.xml.nested_loader_preferences);
                if (this.toolbarTitle != null) {
                    this.toolbarTitle.setText("Launch Tracking Options");
                }
                break;
            case NESTED_SCREEN_3_KEY:
                addPreferencesFromResource(R.xml.nested_appearance_preferences);
                if (this.toolbarTitle != null) {
                    this.toolbarTitle.setText("Appearance");
                }
                break;
            case NESTED_SCREEN_4_KEY:
                addPreferencesFromResource(R.xml.nested_wear_preferences);
                if (this.toolbarTitle != null) {
                    this.toolbarTitle.setText("Wear");
                }
                break;
            default:
        }
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        this.context = getContext();
        String startH, startM, endH, endM;

        //Format Values to something more extensible.
        if (startHour < 10){
            startH = "0" + startHour;
        } else {
            startH = String.valueOf(startHour);
        }

        if (startMin < 10){
            startM = "0" + startMin;
        } else {
            startM = String.valueOf(startMin);
        }

        if (endHour < 10){
            endH = "0" + endHour;
        } else {
            endH = String.valueOf(endHour);
        }

        if (endMin < 10){
            endM = "0" + endMin;
        } else {
            endM = String.valueOf(endMin);
        }

        listPreferences = ListPreferences.getInstance(this.context);
        switchPreferences.setNightModeStart(startH + ":" + startM);
        switchPreferences.setNightModeEnd(endH + ":" + endM);
        listPreferences.getNightMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Timber.d("Google Client Disconnect");
            mGoogleApiClient.disconnect();
        }
    }

}