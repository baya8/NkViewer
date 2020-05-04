package com.kwbt.nk.scraiper.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.kwbt.nk.scraiper.constant.ScraiperConst.Closeby;

/**
 * ダイアログから、メインフレームに入力情報を渡すためのモデル<br>
 *
 * @param raceTitle レースタイトル名
 * @param raceLocation 開催場所
 * @param raceRound ラウンド数
 * @param raceWeek 土曜か日曜かとか
 *
 * @author baya
 */
public class CollectRaceFromJRAInputModel {

    private String raceTitle = new String();
    private Integer raceLocation = null;
    private Integer raceRound = null;
    private Integer raceWeek = null;

    private Closeby closeAction = Closeby.other;

    public Integer getRaceLocation() {
        return raceLocation;
    }

    public void setRaceLocation(Integer raceLocation) {
        this.raceLocation = raceLocation;
    }

    public Integer getRaceRound() {
        return raceRound;
    }

    public void setRaceRound(Integer raceRound) {
        this.raceRound = raceRound;
    }

    public Integer getRaceWeek() {
        return raceWeek;
    }

    public void setRaceWeek(Integer raceWeek) {
        this.raceWeek = raceWeek;
    }

    public String getRaceTitle() {
        return raceTitle;
    }

    public void setRaceTitle(String raceTitle) {
        this.raceTitle = raceTitle;
    }

    public Closeby getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(Closeby closeAction) {
        this.closeAction = closeAction;
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
