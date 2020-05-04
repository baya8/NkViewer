package com.kwbt.nk.viewer.page;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * プログレスバーの表示を行うクラス<br>
 * モーダリーにできないーーーー
 *
 * @author baya
 */
public abstract class ProgessBar extends JDialog {

    JPanel contentPane = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel label = new JLabel("読込中");

    private Thread t = new Thread(new Runnable() {

        @Override
        public void run() {
            execute();
        }
    });

    /**
     * Create the frame.
     */
    public ProgessBar(JPanel panel) {

        // ヘッダの非表示
        //        setUndecorated(true);

        // 親パネルの中央に表示
        setLocationRelativeTo(panel);
        setSize(150, 180);

        contentPane.setBorder(UIManager.getBorder("Tree.editorBorder"));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 0, 5, 5));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        contentPane.add(label);
        progressBar.setIndeterminate(true);

        contentPane.add(progressBar);
    }

    /**
     * 登録した処理を実行
     */
    public final void start() {
        t.start();
    }

    private final void execute() {
        setVisible(true);
        registMethod();
        close();
    }

    /**
     * 裏で走らせたい処理を実装
     */
    public abstract void registMethod();

    /**
     * 画面を閉じる
     */
    public final void close() {
        dispose();
    }
}
