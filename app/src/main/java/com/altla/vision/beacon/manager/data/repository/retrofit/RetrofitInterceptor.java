package com.altla.vision.beacon.manager.data.repository.retrofit;

import com.altla.vision.beacon.manager.data.Header;
import com.altla.vision.beacon.manager.data.HttpStatus;
import com.altla.vision.beacon.manager.data.exception.ConflictException;
import com.altla.vision.beacon.manager.data.exception.NotAuthorizedException;
import com.altla.vision.beacon.manager.data.repository.EncryptedPreferences;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class RetrofitInterceptor implements Interceptor {

    private static final String PROJECT_ID = "projectId";

    private static final String BEARER = "Bearer ";

    private EncryptedPreferences preferences;

    public RetrofitInterceptor(EncryptedPreferences preferences) {
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

        Request request = originalRequest
                .newBuilder()
                .addHeader(Header.AUTHORIZATION.getValue(), BEARER + preferences.getToken())
                .url(url)
                .build();

        Response response = chain.proceed(request);
        HttpStatus status = HttpStatus.toHttpStatus(response.code());
        switch (status) {
            case NOT_AUTHORIZED:
                throw new NotAuthorizedException();
            case CONFLICT:
                throw new ConflictException();
            default:
                break;
        }
        return response;
    }
}
