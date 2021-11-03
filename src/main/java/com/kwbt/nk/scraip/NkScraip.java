package com.kwbt.nk.scraip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.scraip.NkInput.曜日;
import com.kwbt.nk.scraip.NkInput.開催地;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Document;
import com.ui4j.api.event.DocumentLoadEvent;

/**
 * 
 * @author gskwb
 *
 */
public class NkScraip {

    private final static Logger logger = LoggerFactory.getLogger(NkScraip.class);

    private final static PageConfiguration conf = new PageConfiguration()
            .setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");

    private final String 出馬表 = "#quick_menu > div:nth-child(1) > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1)";

    private final static String css土曜東京 = "#main > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1)";
    private final static String css土曜阪神 = "#main > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > a:nth-child(1)";
    private final static String css土曜新潟 = "#main > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > a:nth-child(1)";
    private final static String css日曜東京 = "#main > div:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1)";
    private final static String css日曜阪神 = "#main > div:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > a:nth-child(1)";
    private final static String css日曜新潟 = "#main > div:nth-child(4) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > a:nth-child(1)";

    private final static String sp1R = "#race_list > tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp2R = "#race_list > tbody:nth-child(3) > tr:nth-child(2) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp3R = "#race_list > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp4R = "#race_list > tbody:nth-child(3) > tr:nth-child(4) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp5R = "#race_list > tbody:nth-child(3) > tr:nth-child(5) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp6R = "#race_list > tbody:nth-child(3) > tr:nth-child(6) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp7R = "#race_list > tbody:nth-child(3) > tr:nth-child(7) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp8R = "#race_list > tbody:nth-child(3) > tr:nth-child(8) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp9R = "#race_list > tbody:nth-child(3) > tr:nth-child(9) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp10R = "#race_list > tbody:nth-child(3) > tr:nth-child(10) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp11R = "#race_list > tbody:nth-child(3) > tr:nth-child(11) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp12R = "#race_list > tbody:nth-child(3) > tr:nth-child(12) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp13R = "#race_list > tbody:nth-child(3) > tr:nth-child(13) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp14R = "#race_list > tbody:nth-child(3) > tr:nth-child(14) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp15R = "#race_list > tbody:nth-child(3) > tr:nth-child(15) > td:nth-child(7) > a:nth-child(1)";
    private final static String sp16R = "#race_list > tbody:nth-child(3) > tr:nth-child(16) > td:nth-child(7) > a:nth-child(1)";

    private final static Set<Selector> selector = new HashSet<>();
    private final static Map<Integer, String> roundMap = new HashMap<>();

    private final static Pattern distancePattern = Pattern.compile("[0-9]+メートル");
    private final static Pattern surfacePattern = Pattern.compile("芝|ダート");
    private final static Pattern coursePattern = Pattern.compile("左|右");

    /**
     * WebKitは2重起動を防止するため、JVM起動時に生成しておく
     */
    static {

        // よくわからんけど、マニュアルからコピペ
        System.setProperty("ui4j", "true");

        selector.add(new Selector(曜日.土曜, 開催地.東京, css土曜東京));
        selector.add(new Selector(曜日.土曜, 開催地.阪神, css土曜阪神));
        selector.add(new Selector(曜日.土曜, 開催地.新潟, css土曜新潟));
        selector.add(new Selector(曜日.日曜, 開催地.東京, css日曜東京));
        selector.add(new Selector(曜日.日曜, 開催地.阪神, css日曜阪神));
        selector.add(new Selector(曜日.日曜, 開催地.新潟, css日曜新潟));

        roundMap.put(1, sp1R);
        roundMap.put(2, sp2R);
        roundMap.put(3, sp3R);
        roundMap.put(4, sp4R);
        roundMap.put(5, sp5R);
        roundMap.put(6, sp6R);
        roundMap.put(7, sp7R);
        roundMap.put(8, sp8R);
        roundMap.put(9, sp9R);
        roundMap.put(10, sp10R);
        roundMap.put(11, sp11R);
        roundMap.put(12, sp12R);
        roundMap.put(13, sp13R);
        roundMap.put(14, sp14R);
        roundMap.put(15, sp15R);
        roundMap.put(16, sp16R);

    }

    public void close() {

        BrowserFactory.getWebKit().shutdown();
    }

    /**
     * 
     */
    public NkOutput letsScraip(NkInput input) {

        // https://www.jra.go.jp/
        // https://sp.jra.jp/

        try (Page page = BrowserFactory.getWebKit().navigate("https://www.jra.go.jp/", conf);) {

            pageInitalize(page);

            Document doc = page.getDocument();

            // -------------------------------
            // ページ遷移

            doc.query(出馬表)
                    .get()
                    .click();

            Thread.sleep(200);

            doc.query(css曜日と開催地(input))
                    .get()
                    .click();

            Thread.sleep(200);

            doc.query(cssラウンド(input))
                    .get()
                    .click();

            Thread.sleep(200);

            // -------------------------------
            // 情報取得

            NkOutput output = new NkOutput();

            // 天気は晴れ一択でいく
            output.setWeather("晴");

            // レースタイトル
            String title = 整形(doc.query(".race_name")
                    .get()
                    .getText()
                    .get());

            output.setRaceTitle(title);

            String distance = doc.query("div.cell:nth-child(5)")
                    .get()
                    .getText()
                    .get()
                    .replaceAll(",", "");

            System.out.println(distance);

            // レース距離
            {
                Matcher matcher = distancePattern.matcher(distance);
                if (matcher.find()) {
                    output.setDistance(matcher.group().replaceAll("メートル", ""));
                }
            }

            // 地面
            {
                Matcher matcher = surfacePattern.matcher(distance);
                if (matcher.find()) {
                    MatcherConst.surfaceMap
                            .entrySet()
                            .stream()
                            .filter(e -> e.getValue().equals(matcher.group()))
                            .findFirst();
                    output.setSurface(matcher.group());
                }
            }

            // コース（右・左）
            {
                Matcher matcher = coursePattern.matcher(distance);
                if (matcher.find()) {
                    output.setCourse(matcher.group());
                }
            }

            int count = 1;

            while (true) {

                String cssWeightとか = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3)";

                //                System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★");

                //                if (count == 3) {
                //                    break;
                //                }

                if (!doc.query(String.format(cssWeightとか, count)).isPresent()) {
                    System.out.println("count：" + count);
                    break;
                }

                HorseModel model = new HorseModel();

                String weightなど = doc.query(String.format(cssWeightとか, count))
                        .get()
                        .getText()
                        .get();

                // 馬体重
                {
                    Pattern patternWeight = Pattern.compile("[0-9]+kg");
                    Matcher matcher = patternWeight.matcher(weightなど);
                    if (matcher.find()) {
                        String weight = matcher.group().replaceAll("kg", "");
                        model.setHweight(Integer.valueOf(weight));
                        // System.out.println("★馬体重：" + weight);

                    }
                }

                // 馬体重変動
                {
                    Pattern patternDWeight = Pattern.compile("\\(\\+[0-9]+\\)|\\(\\-[0-9]+\\)|\\(0\\)");
                    Matcher matcher = patternDWeight.matcher(weightなど);
                    if (matcher.find()) {
                        String dhweight = matcher.group()
                                .replaceAll("\\(", "")
                                .replaceAll("\\)", "");
                        model.setDhweight(Integer.valueOf(dhweight));
                        // System.out.println("★馬体重変動：" + matcher.group());
                    }
                }

                // オッズ
                {
                    Pattern patternOdds = Pattern.compile("[0-9]+.[0-9]");
                    Matcher matcher = patternOdds.matcher(weightなど);
                    if (matcher.find()) {
                        model.setOdds(Double.valueOf(matcher.group()));
                        // System.out.println("★オッズ：" + matcher.group());
                    }
                }

                // 前走
                {

                    String cssDsl = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(5) > div:nth-child(1) > div:nth-child(1)";

                    if (doc.query(String.format(cssDsl, count)).isPresent()) {

                        String dslStr = doc.query(String.format(cssDsl, count))
                                .get()
                                .getText()
                                .get();

                        String parseDateStringFormat = "uuuu年M月d日";

                        LocalDate now = LocalDate.now();
                        LocalDate beforeRunDate = LocalDate.parse(
                                dslStr,
                                DateTimeFormatter.ofPattern(parseDateStringFormat));
                        Long dsl = ChronoUnit.DAYS.between(beforeRunDate, now);

                        model.setDsl(dsl.intValue());
                        // System.out.println("★前走：" + dsl);                        
                    }

                    else {
                        model.setDsl(0);
                    }

                }

                output.getHorses().add(model);

                count++;
            }

            return output;

        } catch (NoSuchElementException no) {

            // ボタン配置が変わっている可能性
            throw no;

        } catch (InterruptedException e) {

            // その他例外

            e.printStackTrace();
        }

        throw new RuntimeException();
    }

    private String 整形(String str) {

        return str
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("^( )+", "")
                .replaceAll("( )+$", "")
                .replaceAll("( )+", " ")
                // 文字コードが異なる空白
                .replaceAll(" ", " ");
    }

    /**
     * 
     * @param page
     */
    private void pageInitalize(Page page) {

        page.hide();
        page.addDocumentListener((DocumentLoadEvent dle) -> {
            try {

                // システムが提供する一時フォルダに、スナップショットを保存
                String fileName = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID();

                page.captureScreen(new FileOutputStream(new File(fileName + ".png")));

                logger.info("SI4J Snapshot file: {}", fileName);

            } catch (FileNotFoundException e) {
                logger.error("WebKit：Page生成エラー", e);
                e.printStackTrace();
            }
        });
    }

    /**
     * 
     * @param input
     * @return
     */
    private String css曜日と開催地(NkInput input) {

        return selector.stream()
                .filter(e -> e.equals(input))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("検索条件に合致する要素が存在しません。"))
                .getCss();
    }

    /**
     * 
     * @param input
     * @return
     */
    private String cssラウンド(NkInput input) {

        return roundMap.get(input.getラウンド());
    }

}
