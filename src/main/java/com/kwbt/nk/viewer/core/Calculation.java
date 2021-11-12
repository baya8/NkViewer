package com.kwbt.nk.viewer.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.common.JsonData;
import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.common.Range;
import com.kwbt.nk.common.Result;
import com.kwbt.nk.scraip.HorseModel;
import com.kwbt.nk.scraip.NkOutput;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.GetSheetData;

/**
 * シミュレーション計算ロジック
 *
 * @author baya
 */
public class Calculation {

    @SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(Calculation.class);

    private final GetSheetData getSheetData = new GetSheetData();

    /**
     * 
     * @param output
     * @return
     */
    public List<RightTable> calcRight(NkOutput output) {

        final List<RightTable> rightTable = new LinkedList<>();

        // レース距離から、該当する過去データのみに絞る
        final List<JsonData> dataSheet = getSheetData.getSheetPack(output.getDistanceInt());

        // コース、地面、天気は共通なのでここでフィルター
        final List<Result> filtered = dataSheet
                .parallelStream()
                .map(e -> e.getResultModel())
                .filter(r -> {
                    return r.course == output.getCourseInt()
                            && r.surface == output.getSurfaceInt()
                            && r.weather == output.getWeatherInt();
                })
                .collect(Collectors.toList());

        // 馬Noをそろえるため、拡張for文は使わない
        for (int index = 0; index < output.getHorses().size(); index++) {

            HorseModel horse = output.getHorses().get(index);
            List<Result> machedList = findMatchResultModel(filtered, horse);
            RightTable rightRow = calcValue(machedList);
            rightRow.setNo(index + 1);
            rightTable.add(rightRow);
        }

        return rightTable;
    }

    /**
     * 左テーブルの入力値から、右テーブルの値を算出
     * 
     * @param filtered
     * @param horse
     * @return
     */
    private List<Result> findMatchResultModel(List<Result> filtered, HorseModel horse) {

        if (filtered.isEmpty()) {
            return filtered;
        }

        return filtered
                .parallelStream()
                .filter(e -> {
                    if (Objects.nonNull(horse.getHweight())) {
                        Range<Double> hweightRange = MatcherConst.hweightMap.get(e.hweight);
                        return rangeContain(hweightRange, horse.getHweight());
                    }
                    return true;
                })
                .filter(e -> {

                    // 初走の時は、フィルターしない
                    if (Objects.nonNull(horse.getDhweight())
                            && !horse.isFirstRun()) {
                        Range<Double> dhweightRange = MatcherConst.dhweightMap.get(e.dhweight);
                        return rangeContain(dhweightRange, horse.getDhweight());
                    }
                    return true;
                })
                .filter(e -> {

                    // 初走の時は、フィルターしない
                    if (Objects.nonNull(horse.getDsl())
                            && !horse.isFirstRun()) {
                        Range<Double> dslRange = MatcherConst.dslMap.get(e.dsl);
                        return rangeContain(dslRange, horse.getDsl());
                    }
                    return true;
                })
                .filter(e -> {
                    if (Objects.nonNull(horse.getOdds())) {
                        Range<Double> oddsRange = MatcherConst.oddsMap.get(e.odds);
                        return rangeContain(oddsRange, horse.getOdds());
                    }
                    return true;

                })
                .collect(Collectors.toList());
    }

    private boolean rangeContain(Range<Double> r, Double value) {
        return (r == null && value == null)
                || (r != null && value != null && r.isContain(value));
    }

    private boolean rangeContain(Range<Double> r, Integer value) {
        return (r == null && value == null)
                || (r != null && value != null && r.isContain(Double.valueOf(value)));
    }

    /**
     * 結果テーブルの値を算出する
     *
     * @param filteredResultList
     * @return
     */
    private RightTable calcValue(List<Result> filteredResultList) {

        BigDecimal kaisyu = new BigDecimal(0);
        Integer countSum = Integer.valueOf(0);
        Integer boughtBakenNum = Integer.valueOf(0);
        BigDecimal payyoffAvg = new BigDecimal(0);

        for (Result r : filteredResultList) {
            kaisyu = kaisyu.add(new BigDecimal(r.kaisyu));
            countSum += r.count;
            boughtBakenNum += r.bakenAllNum;
            payyoffAvg = payyoffAvg.add(new BigDecimal(r.payoffSum));
        }

        // マッチした件数が0件でない場合のみ計算を実施
        if (!filteredResultList.isEmpty()) {
            BigDecimal bunbo = new BigDecimal(filteredResultList.size());

            // 循環小数による例外を回避
            kaisyu = kaisyu.divide(bunbo, Const.decimalDivideRoundNum, RoundingMode.HALF_UP);
            payyoffAvg = payyoffAvg.divide(bunbo, Const.decimalDivideRoundNum, RoundingMode.HALF_UP);
        }

        Double winper = countSum * 1.0 / boughtBakenNum * 100.0;
        if (winper.isNaN()) {
            winper = 0.0;
        }

        return new RightTable(
                kaisyu.doubleValue(),
                winper,
                countSum,
                boughtBakenNum,
                payyoffAvg.doubleValue());

    }
}
