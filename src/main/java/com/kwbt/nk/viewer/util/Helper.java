package com.kwbt.nk.viewer.util;

import com.kwbt.nk.common.Range;

public class Helper {

    public static Double toDouble(Object obj) {
        return obj == null ? null : Double.valueOf(obj.toString());
    }

    public static Integer toInteger(Object obj) {
        return obj == null ? null : Integer.valueOf(obj.toString());
    }

    public static boolean contain(Range<Double> r, Double value) {
        return (r == null && value == null)
                || (r != null && value != null && r.isContain(value));
    }

    public static boolean contain(Range<Double> r, Integer value) {
        return (r == null && value == null)
                || (r != null && value != null && r.isContain(Double.valueOf(value)));
    }

}
