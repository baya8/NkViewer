package com.kwbt.nk.viewer.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 右テーブル計算時に用いるデータクラス
 *
 * @author baya
 */
public class CalcModel {

	private Integer selectedCourse = null;
	private Integer selectedWeather = null;
	private Integer selectedSurface = null;
	private Integer inputedDistance = null;
	private List<LeftTable> tableList = null;

	public Integer getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(Integer selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public Integer getSelectedWeather() {
		return selectedWeather;
	}

	public void setSelectedWeather(Integer selectedWeather) {
		this.selectedWeather = selectedWeather;
	}

	public Integer getSelectedSurface() {
		return selectedSurface;
	}

	public void setSelectedSurface(Integer selectedSurface) {
		this.selectedSurface = selectedSurface;
	}

	public Integer getInputedDistance() {
		return inputedDistance;
	}

	public void setInputedDistance(Integer inputedDistance) {
		this.inputedDistance = inputedDistance;
	}

	public List<LeftTable> getTableList() {
		return tableList;
	}

	public void setTableList(List<LeftTable> tableList) {
		this.tableList = tableList;
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
