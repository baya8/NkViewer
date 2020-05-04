package com.kwbt.nk.viewer.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraiper.model.HorseModel;
import com.kwbt.nk.viewer.model.LeftTable;
import com.kwbt.nk.viewer.model.RightTable;

/**
 * JTableの各操作を行うためのユーティリティクラス
 *
 * @author baya
 */
public class TableUtility {

	private final static Logger logger = LoggerFactory.getLogger(TableUtility.class);

	private final static Integer leftTableColumnHweight = 1;
	private final static Integer leftTableColumnDhweight = 2;
	private final static Integer leftTableColumnDsl = 3;
	private final static Integer leftTableColumnOdds = 4;

	private final static int rightTableStartCellValue = 0;

	private final static DecimalFormat doubleFormat = new DecimalFormat("0.0");

	/**
	 * 左テーブルの各セルに1つでも入力があるかどうかをチェック
	 *
	 * @param table
	 * @return
	 */
	public boolean isEmptyLeftTable(JTable table) {
		boolean returnValue = true;
		for (LeftTable l : conv2LeftTableModel(table)) {
			if (!l.isEmpty()) {
				return false;
			}
		}
		return returnValue;
	}

	/**
	 * 左テーブルに入力された値を、計算用のデータモデルへ詰める
	 *
	 * @param table
	 * @return
	 */
	public List<LeftTable> conv2LeftTableModel(JTable table) {

		List<LeftTable> returnValue = new ArrayList<>();

		TableModel model = table.getModel();
		int row = 0;
		while (model.getValueAt(row, 1) != null
				|| model.getValueAt(row, 2) != null
				|| model.getValueAt(row, 3) != null
				|| model.getValueAt(row, 4) != null) {
			LeftTable l = new LeftTable();
			l.setDhweight(object2Double(model.getValueAt(row, leftTableColumnDhweight)));
			l.setHweight(object2Double(model.getValueAt(row, leftTableColumnHweight)));
			l.setDsl(object2Integer(model.getValueAt(row, leftTableColumnDsl)));
			l.setOdds(object2Double(model.getValueAt(row, leftTableColumnOdds)));
			returnValue.add(l);
			row++;
		}

		return returnValue;
	}

	private Double object2Double(Object obj) {
		return obj == null ? null : Double.valueOf(obj.toString());
	}

	private Integer object2Integer(Object obj) {
		return obj == null ? null : Integer.valueOf(obj.toString());
	}

	/**
	 * 引数の右テーブルデータモデルを、画面の右テーブルへセットする
	 *
	 * @param rightTable
	 * @param outputModelList
	 */
	public void setValueToRightTable(JTable rightTable, List<RightTable> outputModelList) {

		TableModel model = rightTable.getModel();
		for (int i = 0; i < outputModelList.size(); i++) {

			AtomicInteger index = new AtomicInteger(rightTableStartCellValue);

			RightTable outputModel = outputModelList.get(i);
			model.setValueAt(formatDoubleValue(outputModel.getKaisyu()), i, index.getAndIncrement());
			model.setValueAt(formatDoubleValue(outputModel.getWinper()), i, index.getAndIncrement());
			model.setValueAt(formatDoubleValue(outputModel.getCountSum()), i, index.getAndIncrement());
			model.setValueAt(formatDoubleValue(outputModel.getBoughtBakenNum()), i, index.getAndIncrement());
			model.setValueAt(formatDoubleValue(outputModel.getPayoffAvg()), i, index.get());
		}
	}

	private Object formatDoubleValue(Object obj) {

		logger.info(" try format {}", obj);

		if (obj == null) {
			return null;
		}

		if (!(obj instanceof Double)) {
			return obj;
		}

		Double obj_d = (Double) obj;
		if (obj_d.isNaN()) {
			return null;
		}

		return doubleFormat(object2Double(obj));
	}

	private Double doubleFormat(Double obj) {
		logger.debug("format to double {}", obj);
		return Double.valueOf(doubleFormat.format(obj));

	}

	/**
	 * 左テーブルの値を全て削除する<br>
	 * ただし、No列は除く
	 *
	 * @param table
	 */
	public void clearLeftTable(JTable table) {

		int rowMax = table.getModel().getRowCount();
		int colMax = table.getModel().getColumnCount();

		for (int r = 0; r < rowMax; r++) {
			for (int c = 0; c < colMax; c++) {
				if (table.getModel().isCellEditable(r, c)) {
					table.getModel().setValueAt(null, r, c);
				}
			}
		}
	}

	/**
	 * 右テーブルの値を全てクリアする
	 *
	 * @param table
	 */
	public void clearRightTable(JTable table) {

		int rowMax = table.getModel().getRowCount();
		int colMax = table.getModel().getColumnCount();

		for (int r = 0; r < rowMax; r++) {
			for (int c = 0; c < colMax; c++) {
				table.getModel().setValueAt(null, r, c);
			}
		}
	}

	/**
	 * 選択中のセルの値をクリアする
	 *
	 * @param table
	 */
	public void deleteCellValue(JTable table) {

		int r = table.getSelectedRow();
		int c = table.getSelectedColumn();

		if (table.getModel().isCellEditable(r, c)) {
			table.getModel().setValueAt(null, r, c);
		}
	}

	/**
	 * webから自動取得した値を、左テーブルに詰める
	 *
	 * @param leftTable
	 * @param houseList
	 */
	public void setJRARaceInfoToLeftTable(JTable leftTable, List<HorseModel> houseList) {

		clearLeftTable(leftTable);

		TableModel model = leftTable.getModel();

		logger.info("houseList: {}", houseList.size());

		for (int i = 0; i < houseList.size(); i++) {
			HorseModel e = houseList.get(i);
			model.setValueAt(e.getHweight(), i, leftTableColumnHweight);
			model.setValueAt(e.getDhweight(), i, leftTableColumnDhweight);
			model.setValueAt(e.getDsl(), i, leftTableColumnDsl);
			model.setValueAt(e.getOdds(), i, leftTableColumnOdds);
		}
	}
}
