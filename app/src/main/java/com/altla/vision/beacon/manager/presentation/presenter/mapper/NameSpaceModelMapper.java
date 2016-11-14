package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.data.entity.NamespaceEntity;
import com.altla.vision.beacon.manager.presentation.presenter.model.NameSpaceModel;

public final class NameSpaceModelMapper {

    private static final String NAMESPACE_PREFIX = "namespaces/";

    public NameSpaceModel map(NamespaceEntity entity) {
        NameSpaceModel model = new NameSpaceModel();
        model.mProjectId = entity.namespaceName.replace(NAMESPACE_PREFIX, "");
        return model;
    }
}
