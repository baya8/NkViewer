package com.kwbt.nk.viewer.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraip.HorseModel;
import com.kwbt.nk.viewer.helper.TableRefresh;
import com.kwbt.nk.viewer.model.ExpecationModel;
import com.kwbt.nk.viewer.model.LeftTable;
import com.kwbt.nk.viewer.model.RightTable;

/**
 * JTableの各操作を行うためのユーティリティクラス
 *
 * @author baya
 */
public class TableUtility {

    private final static Logger logger = LoggerFactory.getLogger(TableUtility.class);

    private final static Integer leftTableColumnNo = 0;
    private final static Integer leftTableColumnHweight = 1;
    private final static Integer leftTableColumnDhweight = 2;
    private final static Integer leftTableColumnDsl = 3;
    private final static Integer leftTableColumnOdds = 4;

    private final static int rightTableStartCellValue = 0;
    private final static int expecationTableStartCellValue = 1;

    private final static DecimalFormat doubleFormat = new DecimalFormat("0.0");

    private final TableRefresh reflesh = new TableRefresh();

    private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
        {
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }
    };

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
            l.setNo(object2Integer(model.getValueAt(row, leftTableColumnNo)));
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

    /**
     * 期待値テーブルの値を設定する
     *
     * @param expectationTable
     * @param expectationList
     */
    public void setValueToExpectationTable(JTable expectationTable, List<ExpecationModel> expectationList) {

        TableModel model = expectationTable.getModel();

        for (int i = 0; i < expectationList.size(); i++) {

            AtomicInteger index = new AtomicInteger(expecationTableStartCellValue);

            ExpecationModel expecationEntity = expectationList.get(i);
            model.setValueAt(expecationEntity.getNo(), i, index.getAndIncrement());
            model.setValueAt(formatDoubleValue(expecationEntity.getWinper()), i, index.getAndIncrement());
            model.setValueAt(formatDoubleValue(expecationEntity.getExpectation()), i, index.get());
        }
    }

    /**
     * 指定の値を、double表記へ変換することを試みる
     *
     * @param obj
     * @return
     */
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

        logger.debug("format to double {}", obj);
        return Double.valueOf(doubleFormat.format(obj));
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

        reflesh.refreshTableHasNo(leftTable);

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

    /**
     * 引数に指定したテーブルのセルを、右揃えにする
     * 
     * @param table
     */
    public void tableRendererRight(JTable table) {

        Enumeration<TableColumn> asdf = table.getColumnModel().getColumns();
        while (asdf.hasMoreElements()) {
            TableColumn col = asdf.nextElement();
            col.setCellRenderer(renderer);
        }
    }
}
