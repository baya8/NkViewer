package com.kwbt.nk.scraip;

import java.util.Objects;

import com.kwbt.nk.scraip.NkInput.曜日;
import com.kwbt.nk.scraip.NkInput.開催地;

/**
 * 
 * @author gskwb
 *
 */
class Selector {

    private 曜日 day;
    private 開催地 location;
    private String css;

    Selector(曜日 day, 開催地 loc, String css) {
        this.day = day;
        this.location = loc;
        this.css = css;
    }

    曜日 getDay() {
        return day;
    }

    void setDay(曜日 day) {
        this.day = day;
    }

    開催地 getLocation() {
        return location;
    }

    void setLocation(開催地 location) {
        this.location = location;
    }

    String getCss() {
        return css;
    }

    void setCss(String css) {
        this.css = css;
    }

    /**
     * 
     * @param input
     * @return
     */
    boolean equals(NkInput input) {

        if (Objects.isNull(input)) {
            return false;
        }

        return Objects.equals(this.day, input.get選択曜日())
                && Objects.equals(this.location, input.get選択開催地());
    }

    @Override
    public boolean equals(Object obj) {

        if (Objects.isNull(obj)
                || !(obj instanceof Selector)) {
            return false;
        }

        Selector sel = (Selector) obj;

        return Objects.equals(this.day, sel.day)
                && Objects.equals(this.location, sel.location);

    }
}
