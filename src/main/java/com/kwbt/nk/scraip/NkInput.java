package com.kwbt.nk.scraip;

import java.util.Objects;

/**
 * 
 * @author gskwb
 *
 */
public class NkInput {

    /**
     * 
     */
    public enum 曜日 {

        土曜(1),
        日曜(2),
        ;

        private final int id;

        private 曜日(final int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }

        public static 曜日 get(int id) {

            for (曜日 type : 曜日.values()) {

                if (type.getInt() == id) {
                    return type;
                }
            }

            return null;
        }
    }

    /**
     * 
     */
    public enum 開催地 {

        東京(1),
        阪神(2),
        新潟(3),
        ;

        private final int id;

        private 開催地(final int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }

        public static 開催地 get(int id) {

            for (開催地 type : 開催地.values()) {

                if (type.getInt() == id) {
                    return type;
                }
            }

            return null;
        }
    }

    private 曜日 選択曜日;
    private 開催地 選択開催地;
    private int ラウンド = 0;

    public 曜日 get選択曜日() {
        return 選択曜日;
    }

    public void set選択曜日(曜日 選択曜日) {
        this.選択曜日 = 選択曜日;
    }

    public 開催地 get選択開催地() {
        return 選択開催地;
    }

    public void set選択開催地(開催地 選択開催地) {
        this.選択開催地 = 選択開催地;
    }

    public int getラウンド() {
        return ラウンド;
    }

    public void setラウンド(int ラウンド) {
        this.ラウンド = ラウンド;
    }

    /**
     * 
     * @return
     */
    public boolean validate() {

        if (Objects.isNull(選択曜日)) {
            return false;
        }

        if (Objects.isNull(選択開催地)) {
            return false;
        }

        if (ラウンド <= 0) {
            return false;
        }

        return true;
    }

}
