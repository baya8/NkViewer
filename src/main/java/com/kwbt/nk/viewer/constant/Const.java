package com.kwbt.nk.viewer.constant;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kwbt.nk.common.JsonData;
import com.kwbt.nk.common.MatcherConst;

public class Const {

    public final static String[] courseArray = toArray(MatcherConst.courseMap.values());
    public final static String[] weatherArray = toArray(MatcherConst.weatherMap.values());
    public final static String[] surfaceArray = toArray(MatcherConst.surfaceMap.values());

    private static <T> String[] toArray(Collection<T> collection) {
        return collection.toArray(new String[collection.size()]);
    }

    public final static File dataFile = new File("datasource.json");
    public static Map<Integer, List<JsonData>> dataSheetMap = null;

    public final static int decimalDivideRoundNum = 3;

}
