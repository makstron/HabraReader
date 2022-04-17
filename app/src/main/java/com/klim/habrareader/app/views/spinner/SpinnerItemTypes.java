package com.klim.habrareader.app.views.spinner;

public enum SpinnerItemTypes {
    HEADER(1),
    ITEM(0);

    int id = 0;

    SpinnerItemTypes(int id) {
        this.id = id;
    }
}