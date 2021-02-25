package com.klim.habrareader.app.windows.postDetails.entities;

public enum PostDetailType {
    UNKNOWN(0),
    TITLE(1),
    TEXT(2),
    IMAGE(3),
    LIST(4),
    EMBEDDED(5),
    QUOTE(6),
    CODE(7),
    EMPTY(8),

    SPOILER(9),
    DIVIDER(10),
    ;

    public int id = 0;

    PostDetailType(int id) {
        this.id = id;
    }
}
