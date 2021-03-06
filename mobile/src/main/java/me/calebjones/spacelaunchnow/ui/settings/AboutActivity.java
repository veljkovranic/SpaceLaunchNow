package me.calebjones.spacelaunchnow.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.LibsConfiguration;
import com.mikepenz.aboutlibraries.entity.Library;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import me.calebjones.spacelaunchnow.R;
import me.calebjones.spacelaunchnow.ui.debug.DebugActivity;
import me.calebjones.spacelaunchnow.ui.supporter.SupporterActivity;
import me.calebjones.spacelaunchnow.utils.Analytics;
import me.calebjones.spacelaunchnow.utils.Utils;

public class AboutActivity extends LibsActivity {

    private int clickCounter = 0;
    private String name = "About Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int m_theme;

        m_theme = R.style.AboutLibraries;

        LibsConfiguration.LibsListener libsListener = new LibsConfiguration.LibsListener() {
            @Override
            public void onIconClicked(View v) {
                clickCounter = clickCounter + 1;
            }

            @Override
            public boolean onLibraryAuthorClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryContentClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryBottomClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onExtraClicked(View v, Libs.SpecialButton specialButton) {
                if(specialButton.ordinal() == 1){
                    specialButtonOne();
                } else if (specialButton.ordinal() == 2) {
                    customTabCallback();
                }
                return false;
            }

            @Override
            public boolean onIconLongClicked(View v) {
                if (clickCounter > 5) {
                    showInputDialog();
                }
                return false;
            }

            @Override
            public boolean onLibraryAuthorLongClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryContentLongClicked(View v, Library library) {
                return false;
            }

            @Override
            public boolean onLibraryBottomLongClicked(View v, Library library) {
                return false;
            }
        };

        setIntent(new LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withVersionShown(true)
                .withListener(libsListener)
                .withActivityTitle("About")
                .withActivityTheme(m_theme)
                .withAboutSpecial2("Support")
                .withAboutSpecial3("Change Log")
                .withFields(R.string.class.getFields())
                .intent(this.getApplicationContext()));

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Analytics.from(this).sendScreenView(name, "Activity destroyed.");
    }

    @Override
    public void onStart() {
        super.onStart();
        Analytics.from(this).sendScreenView(name, name + " started.");
    }

    @Override
    public void onResume() {
        super.onResume();
        Analytics.from(this).sendScreenView(name, name + " resumed.");
    }

    @Override
    public void onPause() {
        super.onPause();
        Analytics.from(this).notifyGoneBackground();
    }

    private void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title("Debug Password")
                .content("Enter password from support.")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (DebugAuthManager.getAuthResult(input)){
                            goToDebug();
                        }
                    }
                }).show();
    }

    private void goToDebug(){
        Analytics.from(this).sendScreenView(name, "Debug Menu activated.");
        Intent debugIntent = new Intent(this, DebugActivity.class);
        startActivity(debugIntent);
    }

    private void specialButtonOne() {
        Analytics.from(this).sendScreenView(name, "Go to Supporter Activity");
        Intent intent = new Intent(this, SupporterActivity.class);
        startActivity(intent);
    }

    public void customTabCallback(){
        Analytics.from(this).sendScreenView(name, "Go to github release notes.");
        Utils.openCustomTab(this, this, "https://github.com/caman9119/SpaceLaunchNow/releases");
    }

}
