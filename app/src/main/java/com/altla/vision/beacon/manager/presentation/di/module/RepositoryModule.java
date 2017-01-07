package com.altla.vision.beacon.manager.presentation.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.altla.vision.beacon.manager.data.repository.BeaconRepository;
import com.altla.vision.beacon.manager.data.repository.BeaconRepositoryImpl;
import com.altla.vision.beacon.manager.data.repository.EncryptedPreferences;
import com.altla.vision.beacon.manager.data.repository.PreferenceRepository;
import com.altla.vision.beacon.manager.data.repository.PreferenceRepositoryImpl;
import com.altla.vision.beacon.manager.data.repository.retrofit.RetrofitInterceptor;
import com.altla.vision.beacon.manager.presentation.di.ActivityScope;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RepositoryModule {

    @ActivityScope
    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @ActivityScope
    @Provides
    PreferenceRepository providePreferenceRepository(PreferenceRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    BeaconRepository provideBeaconRepository(BeaconRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    EncryptedPreferences provideEncryptedPreferences(Context context) {
        return new EncryptedPreferences(context);
    }

    @ActivityScope
    @Provides
    OkHttpClient provideOkHttpClient(@Named("connectTimeout") long connectTimeout, @Named("readTimeout") long readTimeout, EncryptedPreferences preferences) {
        return new OkHttpClient()
                .newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .addInterceptor(new RetrofitInterceptor(preferences))
                .build();
    }

    @ActivityScope
    @Provides
    Retrofit provideRetrofit(@Named("proximityBeaconBaseUri") String baseUri, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUri)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
