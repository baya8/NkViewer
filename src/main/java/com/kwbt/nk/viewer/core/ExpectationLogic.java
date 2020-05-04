package com.kwbt.nk.viewer.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Objects;
import com.kwbt.nk.viewer.model.ExpecationModel;
import com.kwbt.nk.viewer.model.RightTable;

/**
 * 期待値テーブル計算ロジック<br>
 * <br>
 * 結果テーブルを以下条件でソート<br>
 * 1．勝率でソート<br>
 * 2．期待値を求める<br>
 * <br>
 * 期待値の求め方は以下の通り<br>
 * 1．馬ごとに、勝率×ペイオフを求める。<br>
 * 2．勝率の高い順に①を合計／平均する。<br>
 *
 * @author kawabata
 */
public class ExpectationLogic {

    private Integer Integer_Zero = new Integer(0);

    /**
     * mainメソッド
     *
     * @param rightTableList
     * @return
     */
    public List<ExpecationModel> getKitaichiList(List<RightTable> rightTableList) {

        List<TemporaryModel> temporaryList = toTemporaryModelList(rightTableList);

        return avgExpectation(temporaryList);
    }

    /**
     * 各馬ごとに、個々の期待値を算出し、一時的なエンティティへ格納する。
     *
     * @param rightTableList
     * @return
     */
    private List<TemporaryModel> toTemporaryModelList(List<RightTable> rightTableList) {

        return rightTableList
                .parallelStream()

                // 中間操作
                .map(e -> {

                    // 勝率を100で割って、単位をパーセンテージにする
                    Double kitaichi = e.getWinper() * e.getPayoffAvg() / 100.0;

                    TemporaryModel t = new TemporaryModel();
                    t.setNo(e.getNo());
                    t.setWinper(e.getWinper());
                    t.setKitaichiPer(kitaichi);

                    return t;
                })

                // 終端操作
                // 勝率の降順でソート
                .sorted(Comparator.comparing(TemporaryModel::getWinper).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 各行ごとの、平均期待値を算出する
     *
     * @param list
     * @return
     */
    private List<ExpecationModel> avgExpectation(List<TemporaryModel> list) {

        // 1週目はリストが空になっているので、1行分の期待値をそのまま設定
        // 2週目以降は、各行分の平均値を設定する

        List<ExpecationModel> model = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            TemporaryModel m = list.get(i);

            ExpecationModel e = new ExpecationModel();

            Double sum = sum(list, i);
            Double expectation = devide(sum, i);

            e.setNo(m.getNo());
            e.setWinper(m.getWinper());
            e.setExpectation(expectation);
            model.add(e);
        }

        return model;
    }

    /**
     * 指定したインデックスまでの期待値の合計を返す
     *
     * @param list
     * @param index
     * @return
     */
    private Double sum(List<TemporaryModel> list, Integer index) {

        Double sum = 0.0;

        for (int i = 0; i <= index; i++) {
            sum += list.get(i).getKitaichiPer();
        }

        return sum;
    }

    /**
     * 期待値の合計を平均化する。<br>
     * 割る数が0の場合は、引数の期待値をそのまま返す。
     *
     * @param value
     * @param num
     * @return
     */
    private Double devide(Double value, Integer num) {

        if (Objects.equal(num, Integer_Zero)) {
            return value;
        }

        return value / num;
    }

    public class TemporaryModel {

        public Integer no;
        public double winper;
        public double kitaichiPer;

        public double getWinper() {
            return winper;
        }

        public void setWinper(double winper) {
            this.winper = winper;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public double getKitaichiPer() {
            return kitaichiPer;
        }

        public void setKitaichiPer(double kitaichiPer) {
            this.kitaichiPer = kitaichiPer;
        }

    }
}
