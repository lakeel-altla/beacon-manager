package com.altla.vision.beacon.manager.data.repository.retrofit.api;

import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconAttachmentsEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconEntity;
import com.altla.vision.beacon.manager.data.entity.BeaconsEntity;
import com.altla.vision.beacon.manager.data.entity.NamespacesEntity;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface ProximityApi {

    @Headers({"Content-Type: application/json"})
    @GET("beacons?pageSize=20")
    Single<BeaconsEntity> find(@Header("Authorization") String authorization, @Query("projectId") String projectId, @Query("pageToken") String pageToken);

    // Memo:
    // @Path arguments must be set first than @Query arguments.
    @Headers({"Content-Type: application/json"})
    @GET("{beaconName}")
    Single<BeaconEntity> findBeacon(@Header("Authorization") String authorization, @Path(value = "beaconName", encoded = true) String beaconName, @Query("projectId") String projectId);

    @Headers({"Content-Type: application/json"})
    @GET("namespaces")
    Single<NamespacesEntity> findNamespaces(@Header("Authorization") String authorization);

    @Headers({"Content-Type: application/json"})
    @GET("{beaconName}/attachments")
    Single<BeaconAttachmentsEntity> findAttachments(@Header("Authorization") String authorization, @Path(value = "beaconName", encoded = true) String beaconName);

    // Memo:
    // Retrofit parse the request URI.
    // But, If ':' is included in the request URI, it is regarded as a schema prefix,
    // an exception will be thrown.
    // In order to avoid that parsing process, designate the path from the base URI.
    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/beacons:register")
    Single<BeaconEntity> registerBeacon(@Header("Authorization") String authorization, @Query("projectId") String projectId, @Body BeaconEntity entity);

    @Headers({"Content-Type: application/json"})
    @PUT("{beaconName}")
    Single<BeaconEntity> updateBeacon(@Header("Authorization") String authorization, @Path(value = "beaconName", encoded = true) String beaconName, @Query("projectId") String projectId, @Body BeaconEntity entity);

    @Headers({"Content-Type: application/json"})
    @POST("{beaconName}/attachments")
    Single<BeaconAttachmentEntity> createAttachment(@Header("Authorization") String authorization, @Path(value = "beaconName", encoded = true) String beaconName, @Query("projectId") String projectId, @Body BeaconAttachmentEntity entity);

    @Headers({"Content-Type: application/json"})
    @DELETE("{attachmentName}")
    Single<Object> removeAttachment(@Header("Authorization") String authorization, @Path(value = "attachmentName", encoded = true) String attachmentName, @Query("projectId") String projectId);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:deactivate")
    Single<Object> deactivateBeacon(@Header("Authorization") String authorization, @Path(value = "attachmentName", encoded = true) String attachmentName, @Query("projectId") String projectId);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:activate")
    Single<Object> activateBeacon(@Header("Authorization") String authorization, @Path(value = "attachmentName", encoded = true) String attachmentName, @Query("projectId") String projectId);

    @Headers({"Content-Type: application/json"})
    @POST("/v1beta1/{beaconName}:decommission")
    Single<Object> decommissionBeacon(@Header("Authorization") String authorization, @Path(value = "attachmentName", encoded = true) String attachmentName, @Query("projectId") String projectId);
}
