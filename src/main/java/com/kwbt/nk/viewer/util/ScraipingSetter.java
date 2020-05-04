package com.kwbt.nk.viewer.util;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraiper.model.CollectRaceFromJRAInputModel;
import com.kwbt.nk.scraiper.model.RaceInfoModel;
import com.kwbt.nk.scraiper.page.CollectJRARaceInfoPage;
import com.kwbt.nk.scraiper.util.ScraipingRaceFromJRA;

public final class ScraipingSetter {

    private final static Logger logger = LoggerFactory.getLogger(ScraipingSetter.class);

    private final TableUtility tableUtility = new TableUtility();

    /**
     * JRA空情報を取得するための入力画面を表示する
     *
     * @param page
     * @return
     */
    public CollectJRARaceInfoPage showDialog(JPanel page) {

        CollectJRARaceInfoPage popup = new CollectJRARaceInfoPage();
        popup.setModal(true);
        popup.setVisible(true);
        return popup;

    }

    /**
     * JRAサイトから取得した情報を、選択ボックス、左テーブルへ設定する
     *
     * @param model
     * @param page
     * @param comboBoxCourse
     * @param comboBoxSurface
     * @param comboBoxWeather
     * @param textFieldDistance
     * @param tableLeft
     * @param raceTitle
     */
    public void setScraipingResult(
            CollectRaceFromJRAInputModel model,
            JPanel page,
            JComboBox<String> comboBoxCourse,
            JComboBox<String> comboBoxSurface,
            JComboBox<String> comboBoxWeather,
            JFormattedTextField textFieldDistance,
            JTable tableLeft,
            JLabel raceTitle) {

        ScraipingRaceFromJRA scraiper = new ScraipingRaceFromJRA();

        try {
            RaceInfoModel raceInfoModel = scraiper.cloningAndScraipingJRA(model);

            comboBoxCourse.setSelectedItem(raceInfoModel.getCourse());
            comboBoxSurface.setSelectedItem(raceInfoModel.getSurface());
            comboBoxWeather.setSelectedItem(raceInfoModel.getWeather());

            if (StringUtils.isNumeric(raceInfoModel.getDistance())) {
                textFieldDistance.setValue(Integer.valueOf(raceInfoModel.getDistance()));
            }

            tableUtility.setJRARaceInfoToLeftTable(tableLeft, raceInfoModel.getHouseList());

            raceTitle.setText(raceInfoModel.getRaceTitle());

            if (raceInfoModel.hasMissingValue()) {

                MessageBuilder msgBuilder = new MessageBuilder();

                if (raceInfoModel.isMissingHweight()) {
                    msgBuilder.addLine("馬体重(hweight)のスクレイピングに失敗している行があります。");
                }
                if (raceInfoModel.isMissingDhweight()) {
                    msgBuilder.addLine("馬体重変動(dhweight)のスクレイピングに失敗している行があります。");
                }
                if (raceInfoModel.isMissingDsl()) {
                    msgBuilder.addLine("レース間隔(dsl)のスクレイピングに失敗している行があります。");
                }
                if (raceInfoModel.isMissingOdds()) {
                    msgBuilder.addLine("オッズ(odds)のスクレイピングに失敗している行があります。");
                }

                msgBuilder.showMessage(page, "警告", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Throwable e) {

            logger.info("Causes Exception :", e);

            new MessageBuilder()
                    .addLine("スクレイピング処理で異常が発生しました。")
                    .showMessage(page, "致命的", JOptionPane.ERROR_MESSAGE);
        }

    }
}
