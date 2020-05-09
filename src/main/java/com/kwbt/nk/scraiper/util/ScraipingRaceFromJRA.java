package com.kwbt.nk.scraiper.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraiper.model.CollectRaceFromJRAInputModel;
import com.kwbt.nk.scraiper.model.HorseModel;
import com.kwbt.nk.scraiper.model.RaceInfoModel;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Document;
import com.ui4j.api.event.DocumentLoadEvent;

/**
 * JRAのWEBサイトから該当レース情報を抽出（スクレイピング）するクラス
 *
 * @author baya
 */
public class ScraipingRaceFromJRA {

    private final static Logger logger = LoggerFactory.getLogger(ScraipingRaceFromJRA.class);

    private final ScraipingUtils sUtil = new ScraipingUtils();

    private final static PageConfiguration conf = new PageConfiguration()
            .setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");

    // 馬名のところ
    //    private final static String cssSelecterNextRaceJudge = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(1)";
    private final static String cssSelecterNextRaceJudge = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1)";

    /**
     * WebKitは2重起動を防止するため、JVM起動時に生成しておく
     */
    static {

        // よくわからんけど、マニュアルからコピペ
        System.setProperty("ui4j", "true");
    }

    /**
     * 終了処理を実装する際は、必ずこのメソッドを呼ぶこと。
     */
    public static void close() {
        BrowserFactory.getWebKit().shutdown();
    }

    private Page initializePgae() {

        Page page = BrowserFactory.getWebKit().navigate("http://www.jra.go.jp/", conf);
        page.hide();
        page.addDocumentListener((DocumentLoadEvent dle) -> {
            try {

                // システムが提供する一時フォルダに、スナップショットを保存
                String fileName = System.getProperty("java.io.tmpdir")
                        + File.separator
                        + UUID.randomUUID();

                page.captureScreen(new FileOutputStream(new File(fileName + ".png")));

                logger.info("SI4J Snapshot file: {}", fileName);

            } catch (FileNotFoundException e) {
                logger.error("WebKit：Page生成エラー", e);
                e.printStackTrace();
            }
        });

        return page;
    }

    /**
     * メイン処理
     *
     * @param dialogInput
     * @return
     * @throws Throwable
     */
    public RaceInfoModel cloningAndScraipingJRA(CollectRaceFromJRAInputModel dialogInput) throws Throwable {

        RaceInfoModel result = new RaceInfoModel();

        try (Page page = initializePgae();) {

            Document doc = page.getDocument();
            getRaceTitle(doc, result, dialogInput);

        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("スクレイピング処理に失敗しました。", e);
            throw e;
        }

        return result;

    }

    /**
     * 取得したHTMLを、レースヘッダ情報モデル、馬情報モデルへ格納する
     *
     * @param doc
     *            HTMLドキュメント
     * @param model
     *            レース情報モデル
     * @param dialogInput
     *            ダイアログの入力値モデル
     * @throws Throwable
     */
    private void getRaceTitle(
            Document doc,
            RaceInfoModel model,
            CollectRaceFromJRAInputModel dialogInput)
            throws Throwable {

        sUtil.move出馬表(doc);

        sUtil.move一覧(doc, dialogInput);

        // ------------------------
        // 天気が表示されていれば、天気を取得
        // ------------------------
        sUtil.select天気(doc, model);

        // ------------------------------------------
        // レース番号を指定してクリック
        // ------------------------------------------
        sUtil.moveレース(doc, model, dialogInput);

        //------------------------------------------
        // レースタイトルを取得
        //------------------------------------------
        sUtil.selectレースタイトル(doc, model);

        //------------------------------------------
        // 天気以外のヘッダー情報を取得
        //------------------------------------------
        sUtil.select距離地面コース(doc, model);

        logger.info("getRaceTitle：model data {}", model);

        {
            // 馬体重、馬体重変動、レース間隔、オッズを取得
            List<HorseModel> houseModelList = new ArrayList<>();

            int i = 1;

            // レース2日前だと馬体重が取れないので、馬名で次行があるかを判定しよう
            while (doc.query(String.format(cssSelecterNextRaceJudge, i)).isPresent()) {

                HorseModel horseModel = new HorseModel();

                sUtil.select馬体重変動(doc, horseModel, model, i);

                // レース間隔
                sUtil.selectレース間隔(doc, horseModel, model, i);

                // オッズ
                // 「取消」の場合があるので、文字列取得後に数字かいるかどうかを判定してエンティティへ詰める
                sUtil.selectオッズ(doc, horseModel, model, i);

                houseModelList.add(horseModel);
                i++;
            }

            model.getHouseList().addAll(houseModelList);
        }
    }

}
