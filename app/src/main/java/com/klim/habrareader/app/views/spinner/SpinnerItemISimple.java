package com.klim.habrareader.app.views.spinner;

import static com.klim.habrareader.app.views.spinner.BaseSpinnerAdapter.EMPTY_LABEL_RES_ID;

public class SpinnerItemISimple implements SpinnerItemI {

    private int id;
    private String name;
    private int nameResId = EMPTY_LABEL_RES_ID;
    private SpinnerItemTypes type = SpinnerItemTypes.ITEM;

    public SpinnerItemISimple(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SpinnerItemISimple(int id, String name, SpinnerItemTypes type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameResId(int nameResId) {
        this.nameResId = nameResId;
    }

    public void setType(SpinnerItemTypes type) {
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNameResId() {
        return 0;
    }

    @Override
    public SpinnerItemTypes getType() {
        return type;
    }
}