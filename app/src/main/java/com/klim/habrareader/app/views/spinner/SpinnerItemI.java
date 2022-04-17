package com.klim.habrareader.app.views.spinner;

public interface SpinnerItemI {
    SpinnerItemTypes getType();

    String getName();

    int getNameResId();

    long getId();
}