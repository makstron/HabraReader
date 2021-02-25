package com.klim.habrareader.domain.enums;

public enum PostDetailType {
    UNKNOWN(0),
    TITLE(1),
    TEXT(2),
    IMAGE(3),
    LIST(4),
    EMBEDDED(5);

    public int id = 0;

    PostDetailType(int id) {
        this.id = id;
    }
}
