package com.kwbt.nk.viewer.model;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 左テーブルのデータを格納するクラス
 *
 * @author baya
 */
public class LeftTable {

    private Integer no;
    private Double hweight;
    private Double dhweight;
    private Integer dsl;
    private Double odds;

    public LeftTable() {
    }

    public LeftTable(Integer no, Double hweight, Double dhweight, Integer dsl, Double odds) {
        this.no = no;
        this.hweight = hweight;
        this.dhweight = dhweight;
        this.dsl = dsl;
        this.odds = odds;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Double getHweight() {
        return hweight;
    }

    public void setHweight(Double hweight) {
        this.hweight = hweight;
    }

    public Double getDhweight() {
        return dhweight;
    }

    public void setDhweight(Double dhweight) {
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

    public boolean isEmpty() {
        return Objects.isNull(hweight) && Objects.isNull(dhweight) && Objects.isNull(dsl) && Objects.isNull(odds);
    }

}
