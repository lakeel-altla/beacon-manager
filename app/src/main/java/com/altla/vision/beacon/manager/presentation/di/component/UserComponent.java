package com.altla.vision.beacon.manager.presentation.di.component;


import com.altla.vision.beacon.manager.presentation.di.ActivityScope;
import com.altla.vision.beacon.manager.presentation.di.module.ActivityModule;
import com.altla.vision.beacon.manager.presentation.di.module.ConfigModule;
import com.altla.vision.beacon.manager.presentation.di.module.PresenterModule;
import com.altla.vision.beacon.manager.presentation.di.module.RepositoryModule;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconEditFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconListFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.BeaconRegisterFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.NearbyBeaconFragment;
import com.altla.vision.beacon.manager.presentation.view.fragment.ProjectFragmentSwitch;
import com.altla.vision.beacon.manager.presentation.view.fragment.SignInFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class, PresenterModule.class, RepositoryModule.class, ConfigModule.class})
public interface UserComponent {

    void inject(MainActivity activity);

    void inject(SignInFragment fragment);

    void inject(NearbyBeaconFragment fragment);

    void inject(BeaconListFragment fragment);

    void inject(BeaconEditFragment fragment);

    void inject(BeaconRegisterFragment fragment);

    void inject(ProjectFragmentSwitch fragment);
}
