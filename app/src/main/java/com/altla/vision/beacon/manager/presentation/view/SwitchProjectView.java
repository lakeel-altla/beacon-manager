package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.StringRes;

public interface SwitchProjectView extends AuthView {

    void showTitle(@StringRes int resId);

    void showSnackBar(@StringRes int resId);

    void showCurrentProject(String projectId);

    void updateItems();
}
