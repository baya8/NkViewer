package com.kwbt.nk.viewer.model;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * シミュレーション確度テーブル用モデル。<br>
 * 行を追加する度、回収率の平均を自動的に算出する。<br>
 * テーブル1に対し、1インスタンス。
 *
 * @author baya
 */
public class KakudoTable {

	private TreeMap<Integer, Double> dataMap;

	public KakudoTable() {

		// 追加した順番後逆順（降順）になる設定
		this.dataMap = new TreeMap<>(Collections.reverseOrder());
	}

	/**
	 * 該当行の回収率を追加する。<br>
	 * 追加すると、それまでの追加された分との平均値に換算されたのち、マップに格納される。<br>
	 * <br>
	 * 計算式<br>
	 * $$add(n, avg) = \frac{avg * avg(size) + n}{avg(size) + 1}$$<br>
	 * $$n・・・ある行の回収率$$<br>
	 * $$avg・・・それまでの平均値$$<br>
	 *
	 * @param kaisyu
	 *            回収率
	 */
	public void addLine(Double kaisyu) {

		Integer maxNum = this.dataMap.size() == 0 ? 1 : this.dataMap.size() + 1;

		Double lastKaisyuAvg = this.dataMap.isEmpty()
				? 0.0
				: this.dataMap.lastEntry().getValue();

		Double average = (lastKaisyuAvg * maxNum + kaisyu) / (maxNum + 1);

		this.dataMap.put(maxNum, average);
	}

	/**
	 * 格納したデータを、行数分のマップとして返す。<br>
	 * 戻り値は、昇順である。
	 *
	 * @return
	 */
	public Map<Integer, Double> getTableList() {

		Map<Integer, Double> result = new TreeMap<>();
		result.putAll(dataMap);

		return result;
	}
}
