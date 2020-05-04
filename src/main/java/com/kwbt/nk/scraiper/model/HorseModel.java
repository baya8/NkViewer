package com.kwbt.nk.scraiper.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * スクレイピングした結果の内、馬情報を格納するクラス
 *
 * @author baya
 */
public class HorseModel {

	private Integer hweight = null;
	private Integer dhweight = null;
	private Integer dsl = null;
	private Double odds = null;
	private boolean isFirstRun = false;

	public Integer getHweight() {
		return hweight;
	}

	public void setHweight(Integer hweight) {
		this.hweight = hweight;
	}

	public Integer getDhweight() {
		return dhweight;
	}

	public void setDhweight(Integer dhweight) {
		this.dhweight = dhweight;
	}

	public Integer getDsl() {
		return dsl;
	}

	public void setDsl(Integer dsl) {
		this.dsl = dsl;
	}

	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}

	public boolean isFirstRun() {
		return isFirstRun;
	}

	public void setFirstRun(boolean isFirstRun) {
		this.isFirstRun = isFirstRun;
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
