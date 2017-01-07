package com.altla.vision.beacon.manager.presentation;

public final class GoogleProject {

    private static final String NAMESPACE_PREFIX = "namespaces/";

    private String namespaceName;

    public GoogleProject(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getId() {
        return namespaceName.replace(NAMESPACE_PREFIX, "");
    }
}
