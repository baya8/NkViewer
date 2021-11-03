package com.kwbt.nk.scraip;

import java.util.LinkedList;
import java.util.List;

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

}
