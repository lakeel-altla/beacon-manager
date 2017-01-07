package com.altla.vision.beacon.manager.data.repository.retrofit.api;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface ProximityApi {

    @Headers({"Content-Type: application/json"})
    @GET("beacons?pageSize=20")
    Single<BeaconsEntity> find(@Query("pageToken") String pageToken);

    // Memo:
    // @Path arguments must be set first than @Query arguments.
    @Headers({"Content-Type: application/json"})
    @GET("{beaconName}")
    Single<BeaconEntity> findBeacon(@Path(value = "beaconName", encoded = true) String beaconName);

    @Headers({"Content-Type: application/json"})
    @GET("namespaces")
    Single<NamespacesEntity> findNamespaces();

    @Headers({"Content-Type: application/json"})
    @GET("{beaconName}/attachments")
    Single<BeaconAttachmentsEntity> findAttachments(@Path(value = "beaconName", encoded = true) String beaconName);

    // Memo:
    // Retrofit parse the request URI.
    // But, If ':' is included in the request URI, it is regarded as a schema prefix,
    // an exception will be thrown.
    // In order to avoid that parsing process, designate the path from the base URI.
    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/beacons:register")
    Single<BeaconEntity> registerBeacon(@Body BeaconEntity entity);

    @Headers({"Content-Type: application/json"})
    @PUT("{beaconName}")
    Single<BeaconEntity> updateBeacon(@Path(value = "beaconName", encoded = true) String beaconName, @Body BeaconEntity entity);

    @Headers({"Content-Type: application/json"})
    @POST("{beaconName}/attachments")
    Single<BeaconAttachmentEntity> createAttachment(@Path(value = "beaconName", encoded = true) String beaconName, @Body BeaconAttachmentEntity entity);

    @Headers({"Content-Type: application/json"})
    @DELETE("{attachmentName}")
    Single<Object> removeAttachment(@Path(value = "attachmentName", encoded = true) String attachmentName);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:deactivate")
    Single<Object> deactivateBeacon(@Path(value = "attachmentName", encoded = true) String attachmentName);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:activate")
    Single<Object> activateBeacon(@Path(value = "attachmentName", encoded = true) String attachmentName);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:decommission")
    Single<Object> decommissionBeacon(@Path(value = "attachmentName", encoded = true) String attachmentName);
}
