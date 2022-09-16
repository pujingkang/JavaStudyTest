package com.souls.pojo;

public enum Season {

    SPRING("春天","阳光明媚"),
    SUMMER("夏天","艳阳高照"),
    AUTUMN("秋天","秋高气爽"),
    WINTER("冬天","寒风凛冽");

    private final String seasonName;
    private final String seasonDesc;

    Season(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }

    @Override
    public String toString() {
        return "Season{" +
                "seasonName='" + seasonName + '\'' +
                ", seasonDesc='" + seasonDesc + '\'' +
                '}';
    }
}
