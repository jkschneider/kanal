package io.pivotal.kanal2.stage;

public enum CloudProvider {
    CloudFoundry, AWS, GoogleCloud, Kubernetes;

    String code() {
        return name().toLowerCase();
    }
}
