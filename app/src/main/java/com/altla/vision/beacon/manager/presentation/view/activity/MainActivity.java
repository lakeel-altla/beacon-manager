package com.altla.vision.beacon.manager.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.application.App;
import com.altla.vision.beacon.manager.presentation.di.component.UserComponent;
import com.altla.vision.beacon.manager.presentation.di.module.ActivityModule;
import com.altla.vision.beacon.manager.presentation.presenter.ActivityPresenter;
import com.altla.vision.beacon.manager.presentation.view.ActivityView;
import com.altla.vision.beacon.manager.presentation.view.FragmentController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityView {

    @Inject
    ActivityPresenter activityPresenter;

    @BindView(R.id.fragment_place_holder)
    RelativeLayout mainLayout;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 1;

    private static final int REQUEST_CODE_RECOVERABLE_AUTH = 2;

    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawerLayout;

    private UserComponent userComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Dagger。
        userComponent = App.getApplicationComponent(this)
                .userComponent(new ActivityModule(this));
        userComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        activityPresenter.onCreateView(this);
        activityPresenter.checkAuthentication(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        activityPresenter.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECOVERABLE_AUTH) {
            activityPresenter.saveToken(getApplicationContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // TODO: Android 6 Permission
        if (requestCode != REQUEST_CODE_ACCESS_FINE_LOCATION) {
            LOGGER.warn("Permission denied");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_nearby_beacon:
                new FragmentController(getSupportFragmentManager()).showBeaconScanFragment();
                break;
            case R.id.nav_registered:
                new FragmentController(getSupportFragmentManager()).showBeaconListFragment();
                break;
            case R.id.nav_switch_project:
                new FragmentController(getSupportFragmentManager()).showProjectSwitchFragment();
                break;
            case R.id.nav_sign_out:
                activityPresenter.onSignOut(MainActivity.this);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showSignInFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentController controller = new FragmentController(manager);
        controller.showSignInFragment();
    }

    @Override
    public void showBeaconScanFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        FragmentController fragmentController = new FragmentController(getSupportFragmentManager());
        fragmentController.showBeaconScanFragment();
    }

    @Override
    public void showUserRecoverableAuthDialog(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_RECOVERABLE_AUTH);
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, resId, Snackbar.LENGTH_SHORT).show();
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    public static UserComponent getUserComponent(@NonNull Fragment fragment) {
        return ((MainActivity) fragment.getActivity()).userComponent;
    }
}
