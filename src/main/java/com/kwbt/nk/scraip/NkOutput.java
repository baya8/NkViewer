package com.kwbt.nk.scraip;

import java.util.LinkedList;
import java.util.List;

import com.kwbt.nk.common.MatcherConst;

/**
 * 
 * @author gskwb
 *
 */
public class NkOutput {

    private String raceTitle;
    private String distance;
    private String surface;
    private String course;
    private String weather;

    private List<HorseModel> horses = new LinkedList<>();

    public String getRaceTitle() {
        return raceTitle;
    }

    void setRaceTitle(String raceTitle) {
        this.raceTitle = raceTitle;
    }

    public String getDistance() {
        return distance;
    }

    void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSurface() {
        return surface;
    }

    void setSurface(String surface) {
        this.surface = surface;
    }

    public String getCourse() {
        return course;
    }

    void setCourse(String course) {
        this.course = course;
    }

    public String getWeather() {
        return weather;
    }

    void setWeather(String weather) {
        this.weather = weather;
    }

    public List<HorseModel> getHorses() {
        return horses;
    }

    public void setHorses(List<HorseModel> horses) {
        this.horses = horses;
    }

    /**
     * スクレイプした情報（日本語）を<br>
     * 解析用キー（数値）に変換する
     * 
     * @return
     */
    public int getCourseInt() {
        return MatcherConst.courseMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(this.course))
                .findFirst()
                .get()
                .getKey();
    }

    /**
     * スクレイプした情報（日本語）を<br>
     * 解析用キー（数値）に変換する
     * 
     * @return
     */
    public int getSurfaceInt() {
        return MatcherConst.surfaceMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(this.surface.replaceAll("ダート", "ダ")))
                .findFirst()
                .get()
                .getKey();
    }

    /**
     * スクレイプした情報（日本語）を<br>
     * 解析用キー（数値）に変換する
     * 
     * @return
     */
    public int getWeatherInt() {
        return MatcherConst.weatherMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(this.weather))
                .findFirst()
                .get()
                .getKey();
    }

    public int getDistanceInt() {
        return Integer.valueOf(this.distance);
    }
}
