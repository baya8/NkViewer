package com.kwbt.nk.viewer.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 画面に表示するポップアップを出すビルダークラス
 *
 * @author baya
 */
public class MessageBuilder {

    private StringBuilder sb = new StringBuilder();

    /**
     * メッセージを追加する
     *
     * @param msg
     * @return
     */
    public MessageBuilder add(String msg) {
        sb.append(msg);
        return this;
    }

    /**
     * メッセージを改行付きで追加する
     *
     * @param msg
     * @return
     */
    public MessageBuilder addLine(String msg) {
        sb.append(msg)
                .append(System.lineSeparator());
        return this;
    }

    /**
     * 追加した文字列をString型として生成する
     *
     * @return
     */
    public String getMessage() {
        String returnValue = sb.toString();
        sb = new StringBuilder();
        return returnValue;
    }

    /**
     * メッセージが1文字以上あるかどうか判定
     *
     * @return
     */
    public boolean isEmpty() {
        return sb.length() == 0;
    }

    /**
     * ポップアップメッセージを表示する
     *
     * @param page
     * @param title
     * @param type
     */
    public void showMessage(JPanel page, String title, int type) {
        JOptionPane.showMessageDialog(page, getMessage(), title, type);
    }

    /**
     * ポップアップメッセージを表示する
     *
     * @param page
     * @param title
     * @param type
     */
    public void showMessage(JFrame page, String title, int type) {
        JOptionPane.showMessageDialog(page, getMessage(), title, type);
    }

    /**
     * ポップアップメッセージを表示する
     *
     * @param page
     * @param title
     * @param type
     */
    public void showMessage(JDialog page, String title, int type) {
        JOptionPane.showMessageDialog(page, getMessage(), title, type);
    }

}
