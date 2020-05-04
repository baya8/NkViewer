package com.kwbt.nk.scraiper.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ui4j.api.dom.Element;

public class HtmlUtils {

	private final static String LF_LINE = StringUtils.LF + "+";
	private final static String LF_ONLY = "^" + StringUtils.LF;

	/**
	 * LF改行文字を空白文字へ置換する
	 *
	 * @param list
	 * @return
	 */
	public List<String> removeLf(List<String> list) {

		List<String> result = new ArrayList<>(list.size());

		list.forEach(e -> {
			result.add(removeLf(e));
		});

		return result;

	}

	/**
	 * LF改行文字を除去する
	 *
	 * @param str
	 * @return
	 */
	public String removeLf(String str) {
		return str.replaceAll(LF_LINE, StringUtils.LF).replaceAll(LF_ONLY, StringUtils.EMPTY);
	}

	/**
	 * 文字列を、指定した正規表現で分割し、リストで返す
	 *
	 * @param str
	 *            文字列
	 * @param regex
	 *            分割したい正規表現
	 * @return ArrayList
	 */
	public List<String> splitStr2List(String str, String regex) {
		return new ArrayList<String>() {
			{
				for (String s : str.split(regex)) {
					if (StringUtils.isNotBlank(s)) {
						add(s);
					}
				}
			}
		};
	}

	/**
	 * List型Elementのオブジェクトを、List型Stringへ変換する
	 *
	 * @param elements
	 * @return
	 */
	public List<String> element2String(List<Element> elements) {

		if (elements == null)
			return new ArrayList<String>();

		return new ArrayList<String>() {
			{
				for (Element e : elements) {
					String t = removeLf(e.getText().get());
					if (StringUtils.isNotBlank(t)) {
						add(t);
					}
				}
			}
		};
	}
}
