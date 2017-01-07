package com.altla.vision.beacon.manager.data.repository.retrofit;

import android.accounts.Account;
import android.content.Context;

import com.altla.vision.beacon.manager.data.HttpStatus;
import com.altla.vision.beacon.manager.data.exception.ConflictException;
import com.altla.vision.beacon.manager.data.exception.RefreshTokenException;
import com.altla.vision.beacon.manager.data.repository.EncryptedPreferences;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.altla.vision.beacon.manager.data.HttpStatus.CONFLICT;
import static com.altla.vision.beacon.manager.data.HttpStatus.NOT_AUTHORIZED;

public final class RetrofitInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";

    private static final String PROJECT_ID = "projectId";

    private static final String BEARER = "Bearer ";

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitInterceptor.class);

    private Context context;

    private EncryptedPreferences preferences;

    public RetrofitInterceptor(Context context, EncryptedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl url = originalRequest
                .url()
                .newBuilder()
                .addQueryParameter(PROJECT_ID, preferences.getProjectId())
                .build();

        String token = preferences.getToken();
        Request request = originalRequest
                .newBuilder()
                .addHeader(AUTHORIZATION, BEARER + token)
                .url(url)
                .build();

        Response response = chain.proceed(request);

        HttpStatus status = HttpStatus.toHttpStatus(response.code());
        if (NOT_AUTHORIZED == status) {
            //
            // Refresh token.
            //
            String accountName = preferences.getAccountName();
            try {
                GoogleAuthUtil.clearToken(context, token);

                Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

                String newToken = GoogleAuthUtil.getToken(context, account, SCOPE);
                preferences.saveToken(newToken);

                url = originalRequest
                        .url()
                        .newBuilder()
                        .addQueryParameter(PROJECT_ID, preferences.getProjectId())
                        .build();

                request = originalRequest
                        .newBuilder()
                        .addHeader(AUTHORIZATION, BEARER + newToken)
                        .url(url)
                        .build();

                response = chain.proceed(request);
                status = HttpStatus.toHttpStatus(response.code());
            } catch (GoogleAuthException e) {
                LOGGER.error("Failed to refresh token.", e);
                throw new RefreshTokenException(e);
            }
        }

        if (CONFLICT == status) {
            throw new ConflictException();
        }

        return response;
    }
}
