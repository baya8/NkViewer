package com.kwbt.nk.scraiper.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * スクレイピングした結果の内、レースヘッダ情報を格納するクラス
 *
 * @author baya
 */
public class RaceInfoModel {

	private String raceTitle;

	private String surface;
	private String distance;
	private String weather;
	private String course;

	private boolean isMissingHweight;
	private boolean isMissingDhweight;
	private boolean isMissingDsl;
	private boolean isMissingOdds;

	private List<HorseModel> houseList = new ArrayList<>();

	public String getRaceTitle() {
		return raceTitle;
	}

	public void setRaceTitle(String raceTitle) {
		this.raceTitle = raceTitle;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public List<HorseModel> getHouseList() {
		return houseList;
	}

	public void setHouseList(List<HorseModel> houseList) {
		this.houseList = houseList;
	}

	public boolean isMissingHweight() {
		return isMissingHweight;
	}

	public void setMissingHweight(boolean isMissingHweight) {
		this.isMissingHweight = isMissingHweight;
	}

	public boolean isMissingDhweight() {
		return isMissingDhweight;
	}

	public void setMissingDhweight(boolean isMissingDhweight) {
		this.isMissingDhweight = isMissingDhweight;
	}

	public boolean isMissingDsl() {
		return isMissingDsl;
	}

	public void setMissingDsl(boolean isMissingDsl) {
		this.isMissingDsl = isMissingDsl;
	}

	public boolean isMissingOdds() {
		return isMissingOdds;
	}

	public void setMissingOdds(boolean isMissingOdds) {
		this.isMissingOdds = isMissingOdds;
	}

	public boolean hasMissingValue() {
		return isMissingHweight || isMissingDhweight || isMissingDsl || isMissingOdds;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
