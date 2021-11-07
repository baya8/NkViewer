package com.kwbt.nk.viewer.core;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.kwbt.nk.common.FeatureMatcher;
import com.kwbt.nk.common.JsonData;
import com.kwbt.nk.common.Result;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.LeftTable;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.DataSourceReader;
import com.kwbt.nk.viewer.util.GetSheetData;

public class CalculationTest {

    private final Calculation target = new Calculation();

    static {

        Const.dataSheetMap = DataSourceReader.readJsonFile(Const.dataFile);

    }

    @Test
    public void test() {

        CalcModel model = new CalcModel();

        model.setInputedDistance(1200);
        model.setSelectedCourse(1);
        model.setSelectedSurface(0);
        model.setSelectedWeather(0);

        //        LeftTable table = new LeftTable(null, 423.0, 3.0, 5, 3.5);
        LeftTable table = new LeftTable(null, 423.0, null, null, null);
        model.getTableList().add(table);

        List<RightTable> result = target.calcRightTable(model);

        System.out.println(result.get(0));
    }

    @Test
    public void test2() {

        CalcModel model = new CalcModel();

        model.setInputedDistance(1300);
        model.setSelectedCourse(2);
        model.setSelectedSurface(1);
        model.setSelectedWeather(0);

        GetSheetData getSheetData = new GetSheetData();
        List<JsonData> dataSheet = getSheetData.getSheetPack(model.getInputedDistance());

        List<FeatureMatcher> asdf = Const.dataSheetMap
                .values()
                .stream()
                .flatMap(e -> e.stream())
                .map(e -> e.getFeatureMatcher())
                .filter(e -> e.dsl != -1)
                .collect(Collectors.toList());
        asdf.forEach(System.out::println);

        Result r = dataSheet.stream()
                .map(e -> e.getResultModel())
                .filter(e -> e.dsl != -1)
                .findFirst()
                .get();

        System.out.println(r);
    }

}
