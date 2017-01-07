package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.presentation.constants.BeaconStatus;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconEditFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconListFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconRegisterFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.NearbyBeaconFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.ProjectFragmentSwitch;
import com.altla.vision.beacon.manager.presentation.view.fragment.SignInFragment;

public final class FragmentController {

    private static final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private static final String BEACON_REGISTER_FRAGMENT_TAG = BeaconRegisterFragment.class.getSimpleName();

    private static final String NEARBY_BEACON_FRAGMENT_TAG = NearbyBeaconFragment.class.getSimpleName();

    private static final String BEACON_LIST_FRAGMENT_TAG = BeaconListFragment.class.getSimpleName();

    private static final String BEACON_EDIT_FRAGMENT_TAG = BeaconListFragment.class.getSimpleName();

    private static final String PROJECT_SWITCH_FRAGMENT_TAG = ProjectFragmentSwitch.class.getSimpleName();

    private FragmentManager mFragmentManager;

    public FragmentController(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        replaceFragment(R.id.fragment_place_holder, fragment, SIGN_IN_FRAGMENT_TAG);
    }

    public void showNearbyBeaconFragment() {
        NearbyBeaconFragment fragment = NearbyBeaconFragment.newInstance();
        replaceFragment(R.id.fragment_place_holder, fragment, NEARBY_BEACON_FRAGMENT_TAG);
    }

    public void showBeaconRegisterFragment(String type, String hexId, String base64EncodedId) {
        BeaconRegisterFragment fragment = BeaconRegisterFragment.newInstance(type, hexId, base64EncodedId);
        replaceFragment(R.id.fragment_place_holder, fragment, BEACON_REGISTER_FRAGMENT_TAG);
    }

    public void showBeaconListFragment() {
        BeaconListFragment fragment = BeaconListFragment.newInstance();
        replaceFragment(R.id.fragment_place_holder, fragment, BEACON_LIST_FRAGMENT_TAG);
    }

    public void showBeaconEditFragment(String name, BeaconStatus beaconStatus) {
        BeaconEditFragment beaconEditFragment = BeaconEditFragment.newInstance(name, beaconStatus);
        replaceFragment(R.id.fragment_place_holder, beaconEditFragment, BEACON_EDIT_FRAGMENT_TAG);
    }

    public void showProjectSwitchFragment() {
        ProjectFragmentSwitch fragment = ProjectFragmentSwitch.newInstance();
        replaceFragment(R.id.fragment_place_holder, fragment, PROJECT_SWITCH_FRAGMENT_TAG);
    }

    public void replaceFragment(@IdRes int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
