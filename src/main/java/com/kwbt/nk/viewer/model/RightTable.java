package com.kwbt.nk.viewer.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 右テーブルのデータを格納するクラス
 *
 * @author baya
 */
public class RightTable {

    private Integer no;
    private Double kaisyu;
    private Double winper;
    private Integer countSum;
    private Integer boughtBakenNum;
    private Double payoffSumAvg;

    public RightTable() {
    }

    public RightTable(Double kaisyu, Double winper, Integer countSum, Integer boughtBakenNum, Double payoffSumAvg) {
        this.kaisyu = kaisyu;
        this.winper = winper;
        this.countSum = countSum;
        this.boughtBakenNum = boughtBakenNum;
        this.payoffSumAvg = payoffSumAvg;
    }

    public RightTable(Integer no, Double kaisyu, Double winper, Integer countSum, Integer boughtBakenNum, Double payoffSumAvg) {
        this.no = no;
        this.kaisyu = kaisyu;
        this.winper = winper;
        this.countSum = countSum;
        this.boughtBakenNum = boughtBakenNum;
        this.payoffSumAvg = payoffSumAvg;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Double getKaisyu() {
        return kaisyu;
    }

    public void setKaisyu(Double kaisyu) {
        this.kaisyu = kaisyu;
    }

    public Double getWinper() {
        return winper;
    }

    public void setWinper(Double winper) {
        this.winper = winper;
    }

    public Integer getCountSum() {
        return countSum;
    }

    public void setCountSum(Integer countSum) {
        this.countSum = countSum;
    }

    public Integer getBoughtBakenNum() {
        return boughtBakenNum;
    }

    public void setBoughtBakenNum(Integer boughtBakenNum) {
        this.boughtBakenNum = boughtBakenNum;
    }

    public Double getPayoffAvg() {
        return payoffSumAvg;
    }

    public void setPayoffAvg(Double payoffSum) {
        this.payoffSumAvg = payoffSum;
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
