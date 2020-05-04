package com.kwbt.nk.scraiper.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.scraiper.constant.ScraiperConst;
import com.kwbt.nk.scraiper.model.CollectRaceFromJRAInputModel;
import com.kwbt.nk.scraiper.model.HorseModel;
import com.kwbt.nk.scraiper.model.RaceInfoModel;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class ScraipingUtils {

    private final static Logger logger = LoggerFactory.getLogger(ScraipingUtils.class);

    // private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)";
    // private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(3) > span:nth-child(1)";
    // private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)";
    private final static String cssSelecterRaceHouseHweightAndDhweight = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1)";

    //    private final static String cssSelecterRaceSpan = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(5) > div:nth-child(1) > div:nth-child(1)";
    private final static String cssSelecterRaceSpan = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(5) > div:nth-child(1) > div:nth-child(1)";

    // private final static String cssSelecterRaceOdds = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(2)";
    private final static String cssSelecterRaceOdds = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > strong:nth-child(1)";

    /**
     * 【CSSセレクタ】開催曜日と開催場所
     */
    private final static String cssSelecterField = "#main > div:nth-child(%d) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(%d) > a:nth-child(1)";
    /**
     * 【CSSセレクタ】レースラウンド
     */
    private final static String cssSelecterRoundNumber = "#race_list > tbody:nth-child(3) > tr:nth-child(%d) > th:nth-child(1) > a:nth-child(1)";

    /**
     * 【CSSセレクタ】天気
     */
    //    private final static String cssSelecterWeather = ".heading1RaceInfo";
    private final static String cssSelecterWeather = ".weather > span:nth-child(1) > span:nth-child(2)";

    /**
     * 【CSSセレクタ】レースタイトル
     */
    //    private final static String cssSelecterRaceName = ".race_header > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)";
    private final static String cssSelecterRaceName = ".race_name";

    /**
     * 距離とか地面とかコースとか
     */
    private final static String cssSelecterOtherRaceHeaderInfo = "div.cell:nth-child(5)";

    private final static Map<Integer, Integer> cssChildNumMap = new HashMap<>();
    static {
        cssChildNumMap.put(ScraiperConst.tokyo_num, 1);
        cssChildNumMap.put(ScraiperConst.kyoto_num, 2);
        cssChildNumMap.put(ScraiperConst.nigata_num, 3);
        cssChildNumMap.put(ScraiperConst.week_sat, 3);
        cssChildNumMap.put(ScraiperConst.week_sun, 4);
    }

    private final static Pattern MATCH_NUM = Pattern.compile("[0-9]");
    private final static Pattern MATCH_HWEIGHT = Pattern.compile("^[0-9]+");
    private final static Pattern MATCH_DHWEIGHT = Pattern.compile("\\(.*\\)$");
    private final static Pattern MATCH_ODDS = Pattern.compile("^[0-9]+\\.[0-9]+");

    private final static String parseDateStringFormat = "uuuu年M月d日";

    /**
     * トップヘッドメニューの「出馬表」をクリック
     * @param doc
     * @throws InterruptedException
     */
    protected void move出馬表(Document doc) throws InterruptedException {

        //        String selector = "#menu1 > ul:nth-child(2) > li:nth-child(2) > a:nth-child(1)";
        String selector = "#quick_menu > div:nth-child(1) > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)";
        logger.info("select [メニュー] - [出馬表]:" + selector);
        doc.query(selector)
                .get()
                .click();
        Thread.sleep(500);
    }

    /**
     * 開催曜日と、開催地から、レース一覧ページへ遷移
     *
     * @param doc
     * @param dialogInput
     * @throws InterruptedException
     */
    protected void move一覧(Document doc, CollectRaceFromJRAInputModel dialogInput) throws InterruptedException {

        // ------------------------------------------
        // 以下画面上のボタンの並び
        // ------------------------
        // ①  ②  ③ (土曜)
        // ④  ⑤  ⑥ (日曜)
        //
        // ①#main > div:nth-child(3) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)
        // ②#main > div:nth-child(3) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)
        // ③#main > div:nth-child(3) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(3) > a:nth-child(1)
        // ④#main > div:nth-child(4) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)
        // ⑤#main > div:nth-child(4) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)
        // ⑥#main > div:nth-child(4) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(3) > a:nth-child(1)
        String selector = String.format(
                cssSelecterField,
                cssChildNumMap.get(dialogInput.getRaceWeek()),
                cssChildNumMap.get(dialogInput.getRaceLocation()));
        //        logger.info("select:" + selector);
        logger.info("move {}", selector);
        doc.query(selector)
                .get()
                .click();
        Thread.sleep(500);
    }

    protected void select天気(Document doc, RaceInfoModel model) {

        Optional<Element> weatherEl = doc.query(cssSelecterWeather);
        logger.info("select {}", cssSelecterWeather);
        if (weatherEl.isPresent()) {
            String text = weatherEl
                    .get()
                    .getText()
                    .get();
            model.setWeather(text);
        }
    }

    /**
     * レース番号を指定してクリック
     *
     * @param doc
     * @param model
     * @param dialogInput
     * @throws InterruptedException
     */
    protected void moveレース(
            Document doc,
            RaceInfoModel model,
            CollectRaceFromJRAInputModel dialogInput) throws InterruptedException {

        String raceRoundCSSSelector1 = String.format(
                cssSelecterRoundNumber,
                dialogInput.getRaceRound());

        Optional<Element> dom = doc.query(raceRoundCSSSelector1);
        if (dom.isPresent()) {

            logger.info("move {}", raceRoundCSSSelector1);

            dom.get()
                    .click();
        }

        Thread.sleep(1000);
    }

    /**
     * レースタイトルを取得
     *
     * @param doc
     * @param model
     */
    protected void selectレースタイトル(Document doc, RaceInfoModel model) {

        logger.info("select {}", cssSelecterRaceName);
        model.setRaceTitle("<html>"
                + doc.query(cssSelecterRaceName)
                        .get()
                        .getText()
                        .get());
    }

    protected void select距離地面コース(Document doc, RaceInfoModel model) {

        Optional<Element> element = doc.query(cssSelecterOtherRaceHeaderInfo);
        logger.info("select {}", cssSelecterOtherRaceHeaderInfo);
        if (element.isPresent()) {
            String text = element
                    .get()
                    .getText()
                    .get();

            // レース距離
            String distance = getMatchedText(MATCH_NUM.matcher(text));
            model.setDistance(distance);

            // 地面
            String surface = getSurface(text);
            model.setSurface(surface);

            // コース
            String course = getCourse(text);
            model.setCourse(course);
        }
    }

    protected void select馬体重変動(Document doc, HorseModel horseModel, RaceInfoModel model, int tableRowIndex) {

        Optional<Element> firstElement = doc
                .query(String.format(cssSelecterRaceHouseHweightAndDhweight, tableRowIndex));
        logger.info("select {}", String.format(cssSelecterRaceHouseHweightAndDhweight, tableRowIndex));

        if (firstElement.isPresent()) {

            try {
                String weightText = firstElement
                        .get()
                        .getText()
                        .get();

                // 馬体重
                String hweight = getMatchedText(MATCH_HWEIGHT.matcher(weightText));
                horseModel.setHweight(Integer.valueOf(hweight));

                // 馬体重変動
                Matcher dhweightMatcher = MATCH_DHWEIGHT.matcher(weightText);
                if (dhweightMatcher.find()) {
                    dhweightMatcher.reset();
                    String dhweightStr = getDhweightStr(
                            getMatchedText(dhweightMatcher));

                    // （初出馬）の場合があるので、数字かどうかを確認する
                    if (MATCH_NUM.matcher(dhweightStr).find()) {
                        horseModel.setDhweight(Integer.valueOf(dhweightStr));
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                logger.error("exception cause:", e);
                model.setMissingHweight(true);
                model.setMissingDhweight(true);
            }
        } else {
            model.setMissingDhweight(true);
            model.setMissingHweight(true);
        }
    }

    protected void selectレース間隔(Document doc, HorseModel horseModel, RaceInfoModel model, int i) {

        // TODO 初出走はここじゃなくて、単純に馬体重変動のとこ見ればよくね？
        Optional<Element> beforeRun = doc.query(String.format(cssSelecterRaceSpan, i));
        logger.info("select {}", String.format(cssSelecterRaceSpan, i));
        if (beforeRun.isPresent()) {
            try {
                String beforeRunStr = beforeRun
                        .get()
                        .getText()
                        .get();
                LocalDate now = LocalDate.now();
                LocalDate beforeRunDate = LocalDate.parse(beforeRunStr,
                        DateTimeFormatter.ofPattern(parseDateStringFormat));
                Long dsl = ChronoUnit.DAYS.between(beforeRunDate, now);
                horseModel.setDsl(dsl.intValue());
                horseModel.setFirstRun(false);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                logger.error("exception cause:", e);
                model.setMissingDsl(true);
            }

        } else {
            horseModel.setFirstRun(true);
        }
    }

    protected void selectオッズ(Document doc, HorseModel horseModel, RaceInfoModel model, int i) {

        Optional<Element> oddsElement = doc.query(String.format(cssSelecterRaceOdds, i));
        logger.info("select {}", String.format(cssSelecterRaceOdds, i));
        if (oddsElement.isPresent()) {
            try {
                String odds = getMatchedText(
                        MATCH_ODDS.matcher(
                                oddsElement
                                        .get()
                                        .getText()
                                        .get()));
                if (MATCH_NUM.matcher(odds).find()) {
                    horseModel.setOdds(Double.valueOf(odds));
                }
            } catch (NoSuchElementException e) {
                logger.error("exception cause:", e);
                model.setMissingOdds(true);
            }
        } else {
            model.setMissingOdds(true);
        }
    }

    /**
     * Matcherクラス内に一致した文字列を取り出す
     *
     * @param m
     * @return
     */
    private String getMatchedText(Matcher m) {
        String match = StringUtils.EMPTY;
        while (m.find()) {
            match += m.group();
        }
        return match;
    }

    private String getDhweightStr(String str) {
        return str.replace("(", StringUtils.EMPTY).replace(")", StringUtils.EMPTY);
    }

    /**
     * 引数内に[芝、ダート]を含む場合、含む文字列を返す。
     *
     * @param text
     * @return [芝、ダ]、[""]
     */
    private String getSurface(String text) {

        for (Integer key : MatcherConst.surfaceMap.keySet()) {
            String value = MatcherConst.surfaceMap.get(key);
            if (text.contains(value)) {
                return value;
            }
        }

        // if (text.contains("ダート")) {
        // return MainFrame.surfaceConst_dato;
        // }

        return StringUtils.EMPTY;
    }

    /**
     * 引数内に[]に含む場合、含む文字列を返す
     *
     * @param text
     * @return [右、左、直線]、[""]
     */
    private String getCourse(String text) {

        for (Integer key : MatcherConst.courseMap.keySet()) {
            String value = MatcherConst.courseMap.get(key);
            if (text.contains(value)) {
                return value;
            }
        }

        return StringUtils.EMPTY;
    }
}
