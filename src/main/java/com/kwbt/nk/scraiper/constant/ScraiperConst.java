package com.kwbt.nk.scraiper.constant;

import java.util.Collection;

import com.kwbt.nk.common.MatcherConst;

public class ScraiperConst {

    /** 東京 */
    public final static int tokyo_num = 1;
    /** 京都 */
    public final static int kyoto_num = 2;
    /** 新潟 */
    public final static int nigata_num = 3;

    /** 土曜日 */
    public final static int week_sat = 4;
    /** 日曜日 */
    public final static int week_sun = 5;

    /** ウィンドウクローズ時のアクション */
    public enum Closeby {
        ok, cancel, other
    }

    public final static String[] courseArray = toArray(MatcherConst.courseMap.values());
    public final static String[] weatherArray = toArray(MatcherConst.weatherMap.values());
    public final static String[] surfaceArray = toArray(MatcherConst.surfaceMap.values());

    private static <T> String[] toArray(Collection<T> collection) {
        return collection.toArray(new String[collection.size()]);
    }
}
