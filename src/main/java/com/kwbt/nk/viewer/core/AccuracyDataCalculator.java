package com.kwbt.nk.viewer.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.common.JsonData;
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.KakudoTable;
import com.kwbt.nk.viewer.model.LeftTable;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.GetSheetData;

/**
 * シミュレーション確度検証機能ロジック<br>
 * <br>
 * 1. 入力テーブルから一次計算テーブルへ値を設定。<br>
 * 2. 入力テーブルおよび一次計算テーブルの結果から、上位順にソート<br>
 * 3. 上位順からSQLを発行して、結果を算出<br>
 * 4. 確度テーブルへ、結果を格納する<br>
 *
 * @author baya
 */
public class AccuracyDataCalculator {

	private final static Logger logger = LoggerFactory.getLogger(AccuracyDataCalculator.class);

	/**
	 * レース固有条件
	 */
	private Integer selectedCourse = null;
	private Integer selectedWeather = null;
	private Integer selectedSurface = null;
	private Integer inputedDistance = null;

	/**
	 * 入力・一次計算テーブルの値を行単位で格納
	 */
	private List<DataModel> dataModelList;

	private KakudoTable kakudoTable;

	public AccuracyDataCalculator() {

	}

	/**
	 * コンストラクタで、確度算出に必要なパラメータを受ける。<br>
	 * 入力パラメータ<br>
	 * 左上の性別、レース距離、天候、コース<br>
	 * 左テーブル一式、右テーブル一式<br>
	 * <br>
	 * 左テーブルと右テーブルの行数（モデルリスト数）が同一であることが前提条件。
	 */
	public AccuracyDataCalculator(CalcModel calcModel, List<RightTable> rightTableList) {

		if (calcModel.getTableList().size() != rightTableList.size()) {
			throw new RuntimeException("左テーブルリストと右テーブルリストのサイズが同じではありません。");
		}

		this.selectedCourse = calcModel.getSelectedCourse();
		this.selectedWeather = calcModel.getSelectedWeather();
		this.selectedSurface = calcModel.getSelectedSurface();
		this.inputedDistance = calcModel.getInputedDistance();
		this.dataModelList = new ArrayList<>(rightTableList.size());
		for (int i = 0; i < rightTableList.size(); i++) {
			DataModel dataModel = new DataModel();
			dataModel.setLeftTable(calcModel.getTableList().get(i));
			dataModel.setRightTable(rightTableList.get(i));
			this.dataModelList.add(dataModel);
		}
	}

	/**
	 * ゼッケン番号で並んでいる行を、右テーブルの回収率順にソートする。
	 */
	public void sort() {

		Collections.sort(this.dataModelList, new Comparator<DataModel>() {

			@Override
			public int compare(DataModel o1, DataModel o2) {
				Double diff = o1.getRightTable().getKaisyu() - o2.getRightTable().getKaisyu();
				return diff.intValue();
			}
		});
	}

	public void calculation() {

		final GetSheetData getSheetData = new GetSheetData();
		final List<JsonData> dataSheet = getSheetData.getSheetPack(this.inputedDistance);

		/**
		 * TODO 10行ちょっとしかないから、SQLがいいかも
		 * 該当raceに候補が何馬いるか、現状のSheetPackじゃデータが抽出できない
		 */

		this.kakudoTable = new KakudoTable();

	}

	/**
	 * 計算結果のデータを取得する。
	 *
	 * @return
	 */
	public Map<Integer, Double> get() {
		return this.kakudoTable.getTableList();
	}

	/**
	 * データ処理に使う、内部クラス。<br>
	 * 右テーブルと左テーブルの行番号を保証するため。<br>
	 *
	 * @author baya
	 */
	private static class DataModel {

		private LeftTable leftTable;
		private RightTable rightTable;

		@SuppressWarnings("unused")
		public LeftTable getLeftTable() {
			return leftTable;
		}

		public void setLeftTable(LeftTable leftTable) {
			this.leftTable = leftTable;
		}

		@SuppressWarnings("unused")
		public RightTable getRightTable() {
			return rightTable;
		}

		public void setRightTable(RightTable rightTable) {
			this.rightTable = rightTable;
		}

	}
}
