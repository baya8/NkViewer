package com.kwbt.nk.viewer.util;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.analyzer.step.model.JsonData;
import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.common.Range;
import com.kwbt.nk.viewer.constant.Const;

public class GetSheetData {

    private final static Logger logger = LoggerFactory.getLogger(GetSheetData.class);

    /**
     * 入力されたレース距離から、どのシートのデータを使うかを判断するキーを取得
     *
     * @param inputDistance
     * @return
     */
    public static Integer getDistanceKey(Object inputDistance) {

        logger.info(String.format("input key: %s", inputDistance));

        Double value = Double.valueOf(inputDistance.toString());

        Entry<Integer, Range<Double>> asdf = MatcherConst.distanceMap
                .entrySet()
                .parallelStream()
                .filter(e -> e.getValue().isContain(value))
                .findFirst()
                .orElse(null);

        return Objects.isNull(asdf)
                ? null
                : asdf.getKey();
    }

    /**
     * エクセルデータから、該当のレース距離に分類されたレースデータを取得する。
     *
     * @param inputDistance
     *            レース距離
     * @return final宣言されたSheetPackクラス
     */
    public List<JsonData> getSheetPack(Object inputDistance) {
        Integer key = getDistanceKey(inputDistance);
        return Const.dataSheetMap.get(key);
    }

}
