package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.data.entity.NamespaceEntity;
import com.altla.vision.beacon.manager.presentation.presenter.model.ProjectIdModel;

public final class ProjectIdModelMapper {

    private static final String NAMESPACE_PREFIX = "namespaces/";

    public ProjectIdModel map(NamespaceEntity entity) {
        ProjectIdModel model = new ProjectIdModel();
        model.projectId = entity.namespaceName.replace(NAMESPACE_PREFIX, "");
        return model;
    }
}
