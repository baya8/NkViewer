package com.kwbt.nk.viewer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

		final List<JsonData> dataSheet = getSheetData.getSheetPack(model.getInputedDistance());

		final List<Result> resultList = dataSheet.parallelStream().map(e -> e.getResultModel()).collect(Collectors.toList());

		leftTableList.forEach(l -> {
			logger.info(l.toString());
			List<Result> filteredResultList = filterResultList(resultList, l, model);
			RightTable rightTable = calcValue(filteredResultList);
			returnValue.add(rightTable);
		});

		return returnValue;
	}

	private List<Result> filterResultList(List<Result> resultList, LeftTable l, CalcModel model) {

		final List<Result> matchedList = new ArrayList<>(resultList.size());
		final List<Result> subList = new ArrayList<>(resultList.size());

		// 左上の入力値でフィルター
		resultList.forEach(r -> {
			String courseStr = MatcherConst.courseMap.get(r.course);
			String surfaceStr = MatcherConst.surfaceMap.get(r.surface);
			String weatherStr = MatcherConst.weatherMap.get(r.weather);

			if (courseStr.equals(Const.courseArray[model.getSelectedCourse()])
					&& surfaceStr.equals(Const.surfaceArray[model.getSelectedSurface()])
					&& weatherStr.equals(Const.weatherArray[model.getSelectedWeather()])) {

				matchedList.add(r);
			}
		});

		if (matchedList.isEmpty()) {
			return matchedList;
		}

		logger.debug(String.format("filter left top conf - size: %d", matchedList.size()));

		// weightでフィルター
		if (l.getHweight() != null) {
			matchedList.forEach(r -> {
				Range<Double> hweightRange = MatcherConst.hweightMap.get(r.hweight);
				if (rangeContain(hweightRange, l.getHweight())) {
					subList.add(r);
				}
			});
			pass(subList, matchedList);
		}
		logger.debug(String.format("filter weight - size: %d", matchedList.size()));

		// dhweightでフィルター
		if (l.getDhweight() != null) {
			matchedList.forEach(r -> {
				Range<Double> dhweightRange = MatcherConst.dhweightMap.get(r.dhweight);
				if (rangeContain(dhweightRange, l.getDhweight())) {
					subList.add(r);
				}
			});
			pass(subList, matchedList);
		}
		logger.debug(String.format("filter dhweight - size: %d", matchedList.size()));

		// dslでフィルター
		if (l.getDsl() != null) {
			matchedList.forEach(r -> {
				Range<Double> dslRange = MatcherConst.dslMap.get(r.dsl);
				if (rangeContain(dslRange, l.getDsl())) {
					subList.add(r);
				}
			});
			pass(subList, matchedList);
		}
		logger.debug(String.format("filter dsl - size: %d", matchedList.size()));

		// oddsでフィルター
		if (l.getOdds() != null) {
			matchedList.forEach(r -> {
				Range<Double> oddsRange = MatcherConst.oddsMap.get(r.odds);
				if (rangeContain(oddsRange, l.getOdds())) {
					subList.add(r);
				}
			});
			pass(subList, matchedList);
		}
		logger.debug(String.format("filter odds - size: %d", matchedList.size()));

		return matchedList;
	}

	private boolean rangeContain(Range<Double> r, Double value) {
		return (r == null && value == null) || (r != null && value != null && r.isContain(value));
	}

	private boolean rangeContain(Range<Double> r, Integer value) {
		return (r == null && value == null) || (r != null && value != null && r.isContain(Double.valueOf(value)));
	}

	private void pass(List<Result> from, List<Result> to) {
		to.clear();
		to.addAll(from);
		from.clear();
	}

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

		return new RightTable(
				kaisyu.doubleValue(),
				(countSum * 1.0 / boughtBakenNum * 100.0),
				countSum,
				boughtBakenNum,
				payyoffAvg.doubleValue());

	}
}
