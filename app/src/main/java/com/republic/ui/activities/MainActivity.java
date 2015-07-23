package com.republic.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.republic.domain.Session;
import com.republic.domain.UserController;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.fragments.NavigationDrawerFragment;
import com.republic.ui.fragments.ReportFragment;
import com.republic.ui.support.Utils;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private UserController userController;
    private boolean loginRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        RepublicFactory.getDomain().initialize(this);

        userController = RepublicFactory.getUserController();
        loadUser();

        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        redirectIfNotLoggedIn();

        if (!loginRequested) {
            redirectIfNotNumberConfirmed();
        }
    }

    private void loadUser() {
        final Session session = RepublicFactory.getSession();
        if (session != null && session.getCurrentUser() == null) {
            userController.findUser(Utils.getDeviceId(this), new OperationCallback<User>() {
                @Override
                public void performOperation(User user) {
                    session.setUser(user);
                }
            });
        }
    }

    private void redirectIfNotLoggedIn() {

        if (userController != null) {
            String token = Utils.readFromPref(this, Utils.Constants.USER_TOKEN);
            if (Utils.isEmptyString(token)) {
                loginRequested = true;
                launchRedirectActivity(LoginActivity.class);
            }
        }
    }

    private void redirectIfNotNumberConfirmed() {
        String userConfirmedPrefValue = Utils.readFromPref(this, Utils.Constants.USER_CONFIRMED);
        if (!Boolean.parseBoolean(userConfirmedPrefValue)) {
            launchRedirectActivity(ConfirmActivity.class);
        }
    }

    private void launchRedirectActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.container, ReportFragment.newInstance())
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
