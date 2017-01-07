package com.altla.vision.beacon.manager.presentation.view;

import android.support.annotation.StringRes;

public interface ProjectSwitchView {

    void showTitle(@StringRes int resId);

    void showSnackBar(@StringRes int resId);

    void showCurrentProject(String projectId);

    void updateItems();
}
