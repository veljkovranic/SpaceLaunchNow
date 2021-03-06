package me.calebjones.spacelaunchnow.ui.launchdetail.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;
import me.calebjones.spacelaunchnow.R;
import me.calebjones.spacelaunchnow.common.BaseActivity;
import me.calebjones.spacelaunchnow.content.database.ListPreferences;
import me.calebjones.spacelaunchnow.data.models.realm.Launch;
import me.calebjones.spacelaunchnow.data.models.realm.RocketDetails;
import me.calebjones.spacelaunchnow.ui.launchdetail.fragments.AgencyDetailFragment;
import me.calebjones.spacelaunchnow.ui.launchdetail.fragments.MissionDetailFragment;
import me.calebjones.spacelaunchnow.ui.launchdetail.fragments.SummaryDetailFragment;
import me.calebjones.spacelaunchnow.ui.main.MainActivity;
import me.calebjones.spacelaunchnow.utils.customtab.CustomTabActivityHelper;
import timber.log.Timber;


public class LaunchDetailActivity extends BaseActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appBarLayout;
    private ImageView detail_profile_backdrop;
    private CircleImageView detail_profile_image;
    private TextView detail_rocket, detail_mission_location;
    private int mMaxScrollSize;
    private SharedPreferences sharedPref;
    private static ListPreferences sharedPreference;
    private CustomTabActivityHelper customTabActivityHelper;
    private Context context;
    private Calendar rightNow = Calendar.getInstance();

    public String response;
    public Launch launch;

    public LaunchDetailActivity() {
        super("Launch Detail Activity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int m_theme;
        final int statusColor;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        context = getApplicationContext();
        customTabActivityHelper = new CustomTabActivityHelper();
        sharedPreference = ListPreferences.getInstance(context);

        if (sharedPreference.isNightModeActive(this)) {
            statusColor = ContextCompat.getColor(context, R.color.darkPrimary_dark);
        } else {
            statusColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        }
        m_theme = R.style.BaseAppTheme;

        if (getSharedPreferences("theme_changed", 0).getBoolean("recreate", false)) {
            SharedPreferences.Editor editor = getSharedPreferences("theme_changed", 0).edit();
            editor.putBoolean("recreate", false);
            editor.apply();
            recreate();
        }

        setTheme(m_theme);
        setContentView(R.layout.activity_launch_detail);

        //Setup Views
        tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        appBarLayout = (AppBarLayout) findViewById(R.id.detail_appbar);
        detail_profile_image = (CircleImageView) findViewById(R.id.detail_profile_image);
        detail_profile_backdrop = (ImageView) findViewById(R.id.detail_profile_backdrop);
        detail_rocket = (TextView) findViewById(R.id.detail_rocket);
        detail_mission_location = (TextView) findViewById(R.id.detail_mission_location);

        //Grab information from Intent
        Intent mIntent = getIntent();
        String type = mIntent.getStringExtra("TYPE");


        if (type != null && type.equals("launch")) {
            int id = mIntent.getIntExtra("launchID", 0);
            launch = getRealm().where(Launch.class).equalTo("id", id).findFirst();
        }

        if (launch != null && launch.getRocket() != null) {
            Timber.v("Loading launch %s", launch.getId());
            findProfileLogo();
            if (launch.getRocket().getName() != null) {
                if (launch.getRocket().getImageURL() != null && launch.getRocket().getImageURL().length() > 0) {
                    Glide.with(this)
                            .load(launch.getRocket().getImageURL())
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .crossFade()
                            .into(detail_profile_backdrop);
                    getLaunchVehicle(launch, false);
                } else {
                    getLaunchVehicle(launch, true);
                }
            }
        } else {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            Timber.e("Error - Unable to load launch details.");
        }

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss zzz");
        Date date;
        long future;
        date = launch.getNet();
        future = date.getTime();

        Calendar now = rightNow;
        now.setTimeInMillis(System.currentTimeMillis());
        final long timeToFinish = future - now.getTimeInMillis();

        //Assign the title and mission locaiton data
        detail_rocket.setText(launch.getName());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    }

            );
        }

        appBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        viewPager.setAdapter(new

                TabsAdapter(getSupportFragmentManager()

        ));
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.addOnOffsetChangedListener(
                new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        int totalScroll = appBarLayout.getTotalScrollRange();
                        int currentScroll = totalScroll + verticalOffset;

                        int color = statusColor;
                        int r = (color >> 16) & 0xFF;
                        int g = (color >> 8) & 0xFF;
                        int b = (color >> 0) & 0xFF;

                        if ((currentScroll) < 255) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                Window window = getWindow();
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.setStatusBarColor(Color.argb(reverseNumber(currentScroll, 0, 255), r, g, b));
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            }
                        }
                    }
                }

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int reverseNumber(int num, int min, int max) {
        int number = (max + min) - num;
        return number;
    }

    private void findProfileLogo() {

        //Default location, mission is unknown.
        String location = "Unknown Location";
        String mission = "Unknown Mission";
        String locationCountryCode = null;
        String rocketAgency = "";

        if (launch.getRocket().getAgencies().size() > 0) {
            for (int i = 0; i < launch.getRocket().getAgencies().size(); i++) {
                rocketAgency = rocketAgency + launch.getRocket().getAgencies().get(i).getAbbrev() + " ";
            }
        }

        //This checks to see if a location is available
        if (launch.getLocation().getName() != null) {

            //Check to see if a countrycode is available
            if (launch.getLocation().getPads().size() > 0 && launch.getLocation().getPads().
                    get(0).getAgencies().size() > 0) {
                locationCountryCode = launch.getLocation().getPads().
                        get(0).getAgencies().get(0).getCountryCode();

                Timber.v("LaunchDetailActivity - CountryCode length: %s",
                        String.valueOf(locationCountryCode.length()));

                //Go through various CountryCodes and assign flag.
                if (locationCountryCode.length() == 3) {

                    if (locationCountryCode.contains("USA")) {
                        //Check for SpaceX/Boeing/ULA/NASA
                        if (launch.getRocket().getAgencies().size() > 0) {
                            if (launch.getLocation().getPads().
                                    get(0).getAgencies().get(0).getAbbrev().contains("SpX") && launch.getRocket().getAgencies().get(0).getAbbrev().contains("SpX")) {
                                //Apply SpaceX Logo
                                applyProfileLogo(getString(R.string.spacex_logo));
                            }
                        }
                        if (launch.getLocation().getPads().
                                get(0).getAgencies().get(0).getAbbrev() == "BA" && launch.getRocket().getAgencies().get(0).getCountryCode() == "UKR") {
                            //Apply Yuzhnoye Logo
                            applyProfileLogo(getString(R.string.Yuzhnoye_logo));
                        } else if (rocketAgency.contains("ULA")) {
                            //Apply ULA Logo
                            applyProfileLogo(getString(R.string.ula_logo));
                        } else {
                            //Else Apply USA flag
                            applyProfileLogo(getString(R.string.usa_flag));
                        }
                    } else if (locationCountryCode.contains("RUS")) {
                        //Apply Russia Logo
                        applyProfileLogo(getString(R.string.rus_logo));
                    } else if (locationCountryCode.contains("CHN")) {
                        applyProfileLogo(getString(R.string.chn_logo));
                    } else if (locationCountryCode.contains("IND")) {
                        applyProfileLogo(getString(R.string.ind_logo));
                    } else if (locationCountryCode.contains("JPN")) {
                        applyProfileLogo(getString(R.string.jpn_logo));
                    }

                } else if (launch.getLocation().getPads().
                        get(0).getAgencies().get(0).getAbbrev() == "ASA") {
                    //Apply Arianespace Logo
                    applyProfileLogo(getString(R.string.ariane_logo));
                }
                location = (launch.getLocation().getPads().get(0).getName());
            }
        }
        //Assigns the result of the two above checks.
        detail_mission_location.setText(location);
    }

    private void applyProfileLogo(String url) {
        Timber.d("LaunchDetailActivity - Loading Profile Image url: %s ", url);

        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.icon_international)
                .error(R.drawable.icon_international)
                .into(detail_profile_image);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getLaunchVehicle(Launch result, boolean setImage) {
        String query;
        if (result.getRocket().getName().contains("Space Shuttle")) {
            query = "Space Shuttle";
        } else {
            query = result.getRocket().getName();
        }
        RocketDetails launchVehicle = getRealm().where(RocketDetails.class)
                .contains("name", query)
                .findFirst();
        if (setImage) {
            if (launchVehicle != null && launchVehicle.getImageURL().length() > 0) {
                Glide.with(this)
                        .load(launchVehicle
                                .getImageURL())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .crossFade()
                        .into(detail_profile_backdrop);
                Timber.d("Glide Loading: %s %s", launchVehicle.getLV_Name(), launchVehicle.getImageURL());
            }
        }
    }

    public void setData(String data) {
        response = data;
        Timber.v("LaunchDetailActivity - %s", response);
        Scanner scanner = new Scanner(response);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // process the line
            Timber.v("setData - %s ", line);
        }
        scanner.close();
    }

    public Launch getLaunch() {
        return launch;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            detail_profile_image.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            detail_profile_image.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    public void onStart() {
        super.onStart();
        Timber.v("LaunchDetailActivity onStart!");
        customTabActivityHelper.bindCustomTabsService(this);
    }

    public void onStop() {
        super.onStop();
        Timber.v("LaunchDetailActivity onStop!");
        customTabActivityHelper.unbindCustomTabsService(this);
    }

    public void mayLaunchUrl(Uri parse) {
        if (customTabActivityHelper.mayLaunchUrl(parse, null, null)) {
            Timber.v("mayLaunchURL Accepted - %s", parse.toString());
        } else {
            Timber.v("mayLaunchURL Denied - %s", parse.toString());
        }
    }


    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return SummaryDetailFragment.newInstance();
                case 1:
                    return MissionDetailFragment.newInstance();
                case 2:
                    return AgencyDetailFragment.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Mission";
                case 2:
                    return "Agencies";
            }
            return "";
        }
    }

}
