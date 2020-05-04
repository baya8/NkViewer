package com.kwbt.nk.scraiper.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
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
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.event.DocumentLoadEvent;

/**
 * JRAのWEBサイトから該当レース情報を抽出（スクレイピング）するクラス
 *
 * @author baya
 */
public class ScraipingRaceFromJRA {

	private final static Logger logger = LoggerFactory.getLogger(ScraipingRaceFromJRA.class);

	/**
	 * 仮想ブラウザ
	 */
	private final static PageConfiguration conf = new PageConfiguration();

	/**
	 * 仮想ページ
	 */
	private final static Page page;

	// private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)";
	// private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(3) > span:nth-child(1)";
	// private final static String cssSelecterRaceHouseHweightAndDhweight = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)";
	private final static String cssSelecterRaceHouseHweightAndDhweight = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(2) > div:nth-child(1)";

	private final static String cssSelecterRaceSpan = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(5) > div:nth-child(1) > div:nth-child(1)";

	// private final static String cssSelecterRaceOdds = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(2)";
	private final static String cssSelecterRaceOdds = ".basic > tbody:nth-child(3) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > strong:nth-child(1)";

	// 馬名のところ
	private final static String cssSelecterNextRaceJudge = ".mainList > tbody:nth-child(1) > tr:nth-child(%d) > td:nth-child(3) > div:nth-child(1) > div:nth-child(1)";

	/**
	 * WebKitは2重起動を防止するため、JVM起動時に生成しておく
	 */
	static {

		// よくわからんけど、マニュアルからコピペ
		System.setProperty("ui4j", "true");

		conf.setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");
		page = BrowserFactory.getWebKit().navigate("http://www.jra.go.jp/", conf);
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
				logger.info("WebKit初期起動時エラー", e);
				e.printStackTrace();
			}
		});
	}

	private final static Map<Integer, Integer> cssChildNumMap = new HashMap<>();
	static {
		cssChildNumMap.put(ScraiperConst.tokyo_num, 1);
		cssChildNumMap.put(ScraiperConst.kyoto_num, 2);
		cssChildNumMap.put(ScraiperConst.nigata_num, 3);
		cssChildNumMap.put(ScraiperConst.week_sat, 3);
		cssChildNumMap.put(ScraiperConst.week_sun, 4);
	}

	private final static Pattern MATCH_NUM = Pattern.compile("[0-9]");
	private final static Pattern MATCH_WEATHER = Pattern.compile("天候：.");

	private final static Pattern MATCH_HWEIGHT = Pattern.compile("^[0-9]+");
	private final static Pattern MATCH_DHWEIGHT = Pattern.compile("\\(.*\\)$");
	private final static Pattern MATCH_ODDS = Pattern.compile("^[0-9]+\\.[0-9]+");

	private final static int defaultStartHouseTableStartIndex = 4;

	private final static String parseDateStringFormat = "uuuu年M月d日";

	/**
	 * 終了処理を実装する際は、必ずこのメソッドを呼ぶこと。
	 */
	public static void close() {
		BrowserFactory.getWebKit().shutdown();
	}

	/**
	 * メイン処理
	 *
	 * @param dialogInput
	 * @return
	 * @throws Throwable
	 */
	public RaceInfoModel cloningAndScraipingJRA(CollectRaceFromJRAInputModel dialogInput) throws Throwable {

		Document doc = page.getDocument();

		RaceInfoModel result = new RaceInfoModel();

		try {
			getRaceTitle(doc, result, dialogInput);
		} catch (Throwable e) {
			logger.info("スクレイピング処理に失敗しました。", e);
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
	 * @throws InterruptedException
	 *             Thread.sleepメソッドが投げる例外
	 */
	private void getRaceTitle(Document doc, RaceInfoModel model, CollectRaceFromJRAInputModel dialogInput) throws InterruptedException {

		// トップヘッドメニューの「出馬表」をクリック
		{
			String selector = "#menu1 > ul:nth-child(2) > li:nth-child(2) > a:nth-child(1)";
			logger.info("select [メニュー] - [出馬表]:" + selector);
			doc.query(selector)
					.get()
					.click();
		}

		Thread.sleep(500);

		// 開催曜日と、開催地から、レース情報ページへ遷移
		{
			// String selector = String.format("div.kaisaiDayOutDiv:nth-child(%d) > div:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(%d) > a:nth-child(1)",
			String selector = String.format("#main > div:nth-child(%d) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(%d) > a:nth-child(1)",
					cssChildNumMap.get(dialogInput.getRaceWeek()),
					cssChildNumMap.get(dialogInput.getRaceLocation()));
			logger.info("select:" + selector);
			doc.query(selector)
					.get()
					.click();
		}

		Thread.sleep(500);

		// 天気が表示されていれば、天気を取得
		{
			Optional<Element> weatherEl = doc.query(".heading1RaceInfo");
			if (weatherEl.isPresent()) {
				logger.info("scraip [天気]");
				String text = weatherEl
						.get()
						.getText()
						.get();
				String weatherStr = getMatchedText(MATCH_WEATHER.matcher(text));
				model.setWeather(weatherStr.substring(weatherStr.length() - 1));
			}
		}

		String raceRoundCSSSelector1 =
				String.format("#race_list > tbody:nth-child(3) > tr:nth-child(%d) > th:nth-child(1) > a:nth-child(1)",
						dialogInput.getRaceRound());
		if (doc.query(raceRoundCSSSelector1).isPresent()) {
			logger.info("move race info by selector 1");
			doc.query(raceRoundCSSSelector1)
					.get()
					.click();
		}
		Thread.sleep(1000);

		// レースタイトルを取得
		logger.info("read race title");
		model.setRaceTitle("<html>" + doc.query(".race_header > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")
				.get()
				.getText()
				// .get()
				// + "<br>"
				// + doc.query(".heading2Font")
				// .get()
				// .getText()
				// .get()
				// + "<br>"
				// + doc.query("div.raceInfoAreaRowDiv:nth-child(1) > div:nth-child(1)")
				// .get()
				// .getText()
				.get());

		{
			// 天気以外のヘッダー情報を取得
			Optional<Element> element = doc.query("div.cell:nth-child(5)");
			if (element.isPresent()) {
				logger.info("scraip race info (距離, 地面, コース)");
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

		logger.info("model data {}", model);

		{
			// 馬体重、馬体重変動、レース間隔、オッズを取得
			List<HorseModel> houseModelList = new ArrayList<>();

			int i = defaultStartHouseTableStartIndex;

			// レース2日前だと馬体重が取れないので、馬名で次行があるかを判定しよう
			while (doc.query(String.format(cssSelecterNextRaceJudge, i)).isPresent()) {

				HorseModel horseModel = new HorseModel();

				Optional<Element> firstElement = doc.query(String.format(cssSelecterRaceHouseHweightAndDhweight, i));
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
							String dhweightStr =
									getDhweightStr(
											getMatchedText(dhweightMatcher));

							// （初出馬）の場合があるので、数字かどうかを確認する
							if (MATCH_NUM.matcher(dhweightStr).find()) {
								horseModel.setDhweight(Integer.valueOf(dhweightStr));
							}
						}
					} catch (NoSuchElementException e) {
						logger.error("scraiping weightText failed, count {} horse info.", i);
						model.setMissingHweight(true);
						model.setMissingDhweight(true);
					}
				} else {
					model.setMissingDhweight(true);
					model.setMissingHweight(true);
				}

				// レース間隔
				// TODO 初出走はここじゃなくて、単純に馬体重変動のとこ見ればよくね？
				Optional<Element> beforeRun = doc.query(String.format(cssSelecterRaceSpan, i));
				if (beforeRun.isPresent()) {
					try {
						String beforeRunStr = beforeRun
								.get()
								.getText()
								.get();
						LocalDate now = LocalDate.now();
						LocalDate beforeRunDate = LocalDate.parse(beforeRunStr, DateTimeFormatter.ofPattern(parseDateStringFormat));
						Long dsl = ChronoUnit.DAYS.between(beforeRunDate, now);
						horseModel.setDsl(dsl.intValue());
						horseModel.setFirstRun(false);
					} catch (NoSuchElementException e) {
						logger.error("scraiping beforeRunStr failed, count {} horse info.", i);
						model.setMissingDsl(true);
					}

				} else {
					horseModel.setFirstRun(true);
				}

				// オッズ
				// 「取消」の場合があるので、文字列取得後に数字かいるかどうかを判定してエンティティへ詰める
				Optional<Element> oddsElement = doc.query(String.format(cssSelecterRaceOdds, i));
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
						logger.error("scraiping odds failed, count {} horse info.", i);
						model.setMissingOdds(true);
					}
				} else {
					model.setMissingOdds(true);
				}

				houseModelList.add(horseModel);
				i++;
			}

			model.getHouseList().addAll(houseModelList);
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
