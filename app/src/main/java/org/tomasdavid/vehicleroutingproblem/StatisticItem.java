package org.tomasdavid.vehicleroutingproblem;

public class StatisticItem {

    private int imageResId;

    private String name;

    private String value;

    public StatisticItem(int imageResId, String name, String value) {
        this.imageResId = imageResId;
        this.name = name;
        this.value = value;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
