package com.kwbt.nk.viewer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.analyzer.step.model.JsonData;
import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.common.Range;
import com.kwbt.nk.common.Result;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.LeftTable;
import com.kwbt.nk.viewer.model.RightTable;

/**
 * シミュレーション計算ロジック
 *
 * @author baya
 */
public class Calculation {

    private final static Logger logger = LoggerFactory.getLogger(Calculation.class);

    private final GetSheetData getSheetData = new GetSheetData();

    /**
     * 左テーブルの入力値から、右テーブルの値を算出
     *
     * @param model
     * @return
     */
    public List<RightTable> calcRightTable(CalcModel model) {

        final List<LeftTable> leftTableList = model.getTableList();

        final List<RightTable> returnValue = new ArrayList<>(leftTableList.size());

        // データシートのラベルはレース距離値
        final List<JsonData> dataSheet = getSheetData.getSheetPack(model.getInputedDistance());

        final List<Result> resultList = dataSheet
                .parallelStream()
                .map(e -> e.getResultModel())
                .collect(Collectors.toList());

        leftTableList.forEach(l -> {
            logger.info(l.toString());
            List<Result> filteredResultList = filter(resultList, l, model);
            RightTable rightTable = calculation(filteredResultList);
            returnValue.add(rightTable);
        });

        return returnValue;
    }

    private List<Result> filter(List<Result> resultList, LeftTable l, CalcModel model) {

        return resultList
                .parallelStream()

                // 左上の入力値でフィルター
                .filter(e -> {

                    String courseStr = MatcherConst.courseMap.get(e.course);
                    String surfaceStr = MatcherConst.surfaceMap.get(e.surface);
                    String weatherStr = MatcherConst.weatherMap.get(e.weather);

                    return courseStr.equals(Const.courseArray[model.getSelectedCourse()])
                            && surfaceStr.equals(Const.surfaceArray[model.getSelectedSurface()])
                            && weatherStr.equals(Const.weatherArray[model.getSelectedWeather()]);
                })
                .filter(e -> {
                    Range<Double> hweightRange = MatcherConst.hweightMap.get(e.hweight);
                    return Helper.contain(hweightRange, l.getHweight());
                })
                .filter(e -> {
                    Range<Double> dhweightRange = MatcherConst.dhweightMap.get(e.dhweight);
                    return Helper.contain(dhweightRange, l.getDhweight());
                })
                .filter(e -> {
                    Range<Double> dslRange = MatcherConst.dslMap.get(e.dsl);
                    return Helper.contain(dslRange, l.getDsl());
                })
                .filter(e -> {
                    Range<Double> oddsRange = MatcherConst.oddsMap.get(e.odds);
                    return Helper.contain(oddsRange, l.getOdds());
                })
                .collect(Collectors.toList());
    }

    private RightTable calculation(List<Result> filteredResultList) {

        Double kaisyuu = filteredResultList
                .parallelStream()
                .mapToDouble(e -> e.kaisyu)
                .sum();

        Integer countSum = filteredResultList
                .parallelStream()
                .mapToInt(e -> e.count)
                .sum();

        Integer bakenNum = filteredResultList
                .parallelStream()
                .mapToInt(e -> e.bakenAllNum)
                .sum();

        Double payoff = filteredResultList
                .parallelStream()
                .mapToDouble(e -> e.payoffSum)
                .sum();

        // マッチした件数が0件でない場合のみ計算を実施
        if (!filteredResultList.isEmpty()) {

            // 循環小数による例外を回避
            kaisyuu = kaisyuu / filteredResultList.size();
            payoff = payoff / filteredResultList.size();
        }

        return new RightTable(
                kaisyuu,
                (countSum * 1.0 / bakenNum * 100.0),
                countSum,
                bakenNum,
                payoff);

    }
}
