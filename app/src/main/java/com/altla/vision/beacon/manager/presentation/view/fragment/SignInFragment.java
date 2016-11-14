package com.altla.vision.beacon.manager.presentation.view.fragment;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.android.SnackBarUtils;
import com.altla.vision.beacon.manager.presentation.presenter.SignInPresenter;
import com.altla.vision.beacon.manager.presentation.view.SignInView;
import com.altla.vision.beacon.manager.presentation.view.activity.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInFragment extends Fragment implements SignInView {

    @Inject
    SignInPresenter mSignInPresenter;

    @BindView(R.id.layout)
    RelativeLayout mRelativeLayout;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInFragment.class);

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1;

    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 2;

    private static final int REQUEST_CODE_GOOGLE_PLAY_SERVICES_ERROR = 3;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        mSignInPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSignInPresenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_ACCOUNT:
                if (resultCode == Activity.RESULT_OK) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    mSignInPresenter.onPikedUpAccount(getActivity(), accountName);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    SnackBarUtils.showShort(mRelativeLayout, R.string.message_pick_account);
                }
                break;
            case REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR:
                if (resultCode == Activity.RESULT_OK) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    mSignInPresenter.onPikedUpAccount(getActivity(), accountName);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    SnackBarUtils.showShort(mRelativeLayout, R.string.message_pick_account);
                }
                break;
            default:
                LOGGER.warn("Unknown activity result");
                break;
        }
    }

    @OnClick(R.id.button_sign_in)
    public void onSignIn() {
        Intent intent =
                AccountPicker.newChooseAccountIntent(
                        null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    public void showTitle(int resId) {
        getActivity().setTitle(resId);
    }

    @Override
    public void showGooglePlayErrorDialog(int statusCode) {
        GooglePlayServicesUtil.showErrorDialogFragment(statusCode, getActivity(), SignInFragment.this, REQUEST_CODE_GOOGLE_PLAY_SERVICES_ERROR,
                dialogInterface -> {
                    SnackBarUtils.showShort(mRelativeLayout, R.string.error_google_play_service_unavailable);
                    dialogInterface.dismiss();
                });
    }

    @Override
    public void showOAuthActivity(Intent intent) {
        startActivityForResult(
                intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
    }

    @Override
    public void showBeaconScanFragment() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.showBeaconScanFragment();
    }

    @Override
    public void showSnackBar(int resId) {
        SnackBarUtils.showShort(mRelativeLayout, resId);
    }
}
