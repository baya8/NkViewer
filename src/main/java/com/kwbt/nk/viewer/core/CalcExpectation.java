package com.kwbt.nk.viewer.core;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.kwbt.nk.viewer.model.ExpectModel;
import com.kwbt.nk.viewer.model.RightTable;

/**
 * 期待値算出を行う
 * 
 * @author gskwb
 *
 */
public class CalcExpectation {

    /**
     * 
     * @param rightTable
     * @return
     */
    public List<ExpectModel> exec(List<RightTable> rightTable) {

        // 勝率の降順でソート
        List<RightTable> sorted = rightTable
                .stream()
                .sorted(Comparator.comparing(RightTable::getWinper).reversed())
                .collect(Collectors.toList());

        Double winperSum = sorted
                .stream()
                .mapToDouble(e -> e.getWinper())
                .sum();

        return sorted
                .stream()
                .map(e -> {

                    ExpectModel model = new ExpectModel();
                    Double expect = e.getWinper() * e.getPayoffAvg() / 100.0;
                    Double winperB = e.getWinper() / winperSum * 100.0;

                    model.setNo(e.getNo());
                    model.setExpectation(expect);
                    model.setWinper(winperB);
                    return model;
                })
                .collect(Collectors.toList());
    }
}
