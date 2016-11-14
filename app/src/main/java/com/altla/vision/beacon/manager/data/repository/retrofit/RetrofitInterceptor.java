package com.altla.vision.beacon.manager.data.repository.retrofit;

import com.altla.vision.beacon.manager.data.exception.ConflictException;
import com.altla.vision.beacon.manager.data.exception.NotAuthorizedException;
import com.altla.vision.beacon.manager.data.HttpStatus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class RetrofitInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder().build();

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
