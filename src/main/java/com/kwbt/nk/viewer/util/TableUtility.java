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

    private final static Integer leftHweight = 1;
    private final static Integer leftDhweight = 2;
    private final static Integer leftDsl = 3;
    private final static Integer leftOdds = 4;

    private final static int rightStartCellValue = 0;

    private final static DecimalFormat doubleFormat = new DecimalFormat("0.0");

    /**
     * 左テーブルの各セルに1つでも入力があるかどうかをチェック
     *
     * @param table
     * @return
     */
    public boolean isEmptyLeftTable(JTable table) {

        return getLeftTableModelList(table)
                .parallelStream()
                .allMatch(e -> e.isEmpty());
    }

    /**
     * 左テーブルに入力された値を、計算用のデータモデルへ詰める
     *
     * @param table
     * @return
     */
    public List<LeftTable> getLeftTableModelList(JTable table) {

        List<LeftTable> returnValue = new ArrayList<>();

        TableModel model = table.getModel();
        int row = 0;

        while (isExistInput(model, row)) {

            LeftTable l = new LeftTable();
            l.setDhweight(Helper.toDouble(model.getValueAt(row, leftDhweight)));
            l.setHweight(Helper.toDouble(model.getValueAt(row, leftHweight)));
            l.setDsl(Helper.toInteger(model.getValueAt(row, leftDsl)));
            l.setOdds(Helper.toDouble(model.getValueAt(row, leftOdds)));
            returnValue.add(l);
            row++;
        }

        return returnValue;
    }

    /**
     * テーブルの入力値が存在するかをチェック
     *
     * @param model
     * @param row
     * @return
     */
    private boolean isExistInput(TableModel model, int row) {

        return model.getValueAt(row, 1) != null
                || model.getValueAt(row, 2) != null
                || model.getValueAt(row, 3) != null
                || model.getValueAt(row, 4) != null;
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

            AtomicInteger index = new AtomicInteger(rightStartCellValue);

            RightTable outputModel = outputModelList.get(i);
            model.setValueAt(toDouble(outputModel.getKaisyu()), i, index.getAndIncrement());
            model.setValueAt(toDouble(outputModel.getWinper()), i, index.getAndIncrement());
            model.setValueAt(toDouble(outputModel.getCountSum()), i, index.getAndIncrement());
            model.setValueAt(toDouble(outputModel.getBoughtBakenNum()), i, index.getAndIncrement());
            model.setValueAt(toDouble(outputModel.getPayoffAvg()), i, index.get());
        }
    }

    private Object toDouble(Object obj) {

        if (obj == null) {
            return null;
        }

        if (!(obj instanceof Double)) {
            return null;
        }

        Double obj_d = (Double) obj;
        if (obj_d.isNaN()) {
            return null;
        }

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
            model.setValueAt(e.getHweight(), i, leftHweight);
            model.setValueAt(e.getDhweight(), i, leftDhweight);
            model.setValueAt(e.getDsl(), i, leftDsl);
            model.setValueAt(e.getOdds(), i, leftOdds);
        }
    }
}
