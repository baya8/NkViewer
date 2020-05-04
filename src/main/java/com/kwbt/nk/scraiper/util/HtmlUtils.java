package com.kwbt.nk.scraiper.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        return list
                .parallelStream()
                .map(e -> removeLf(e))
                .collect(Collectors.toList());

    }

    /**
     * LF改行文字を除去する
     *
     * @param str
     * @return
     */
    public String removeLf(String str) {
        return str
                .replaceAll(LF_LINE, StringUtils.LF)
                .replaceAll(LF_ONLY, StringUtils.EMPTY);
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

        return Stream
                .of(str.split(regex))
                .filter(e -> StringUtils.isNotBlank(e))
                .collect(Collectors.toList());
    }

    /**
     * List型Elementのオブジェクトを、List型Stringへ変換する
     *
     * @param elements
     * @return
     */
    public List<String> element2String(List<Element> elements) {

        if (elements == null)
            return new ArrayList<>();

        return elements
                .parallelStream()
                .map(e -> removeLf(e.getText().get()))
                .collect(Collectors.toList());

    }
}
