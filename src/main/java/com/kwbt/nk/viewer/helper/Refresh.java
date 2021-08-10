package com.kwbt.nk.viewer.helper;

import javax.swing.JTable;

/**
 * 画面のリフレッシュを行う
 *
 * @author baya
 */
public class Refresh {

    /**
     * No列を含むテーブルのセル値をクリア
     * 
     * @param table
     */
    public void refreshTableHasNo(JTable table) {

        int rowMax = table.getModel().getRowCount();
        int colMax = table.getModel().getColumnCount();

        int startNo = 1;

        for (int r = 0; r < rowMax; r++) {
            for (int c = startNo; c < colMax; c++) {
                table.getModel().setValueAt(null, r, c);
            }
        }
    }

    /**
     * No列を含まないテーブルのセル値をクリア
     * 
     * @param table
     */
    public void refreshTableNotContainNo(JTable table) {

        int rowMax = table.getModel().getRowCount();
        int colMax = table.getModel().getColumnCount();

        int startNo = 0;

        for (int r = 0; r < rowMax; r++) {
            for (int c = startNo; c < colMax; c++) {
                table.getModel().setValueAt(null, r, c);
            }
        }
    }

}
