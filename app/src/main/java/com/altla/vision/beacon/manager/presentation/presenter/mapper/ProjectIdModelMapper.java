package com.altla.vision.beacon.manager.presentation.presenter.mapper;

import com.altla.vision.beacon.manager.data.entity.NamespaceEntity;
import com.altla.vision.beacon.manager.presentation.GoogleProject;
import com.altla.vision.beacon.manager.presentation.presenter.model.ProjectIdModel;

public final class ProjectIdModelMapper {

    public ProjectIdModel map(NamespaceEntity entity) {
        ProjectIdModel model = new ProjectIdModel();

        GoogleProject googleProject = new GoogleProject(entity.namespaceName);
        model.projectId = googleProject.getId();

        return model;
    }
}
