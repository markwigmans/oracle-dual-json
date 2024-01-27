package com.btb.odj.util;

public enum Provider {
    ElasticSearch("es"),
    JPA("jpa"),
    MongoDB("mongo");

    public final String label;

    Provider(String label) {
        this.label = label;
    }
}
