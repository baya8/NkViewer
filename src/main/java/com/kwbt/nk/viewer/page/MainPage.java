package com.kwbt.nk.viewer.page;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraiper.constant.ScraiperConst;
import com.kwbt.nk.scraiper.page.CollectJRARaceInfoPage;
import com.kwbt.nk.scraiper.util.ScraipingRaceFromJRA;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.core.Calculation;
import com.kwbt.nk.viewer.core.ExpectationLogic;
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.ExpecationModel;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.DataSourceReader;
import com.kwbt.nk.viewer.util.GetSheetData;
import com.kwbt.nk.viewer.util.JComboBoxUtility;
import com.kwbt.nk.viewer.util.MessageBuilder;
import com.kwbt.nk.viewer.util.ScraipingSetter;
import com.kwbt.nk.viewer.util.TableUtility;

public class MainPage extends JFrame {

    /** ロガー */
    private final static Logger logger = LoggerFactory.getLogger(MainPage.class);

    /** テーブルに値を書き込んだりするツール */
    private final static TableUtility tableUtility = new TableUtility();

    /** コンボボックスをなんやかんやするためのツール */
    private final static JComboBoxUtility jComboBoxUtility = new JComboBoxUtility();

    /** スクレイピング結果を画面に設定するツール */
    private final static ScraipingSetter scraipSetter = new ScraipingSetter();

    /** 各種計算ツール */
    private final Calculation calcInstance = new Calculation();

    private final ExpectationLogic expectationLogic = new ExpectationLogic();

    /**
     * 画面コンポーネント
     */
    private JPanel contentPane;

    // メニュー
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("ファイル");
    private JMenuItem menuItemFileExit = new JMenuItem("終了");
    private JMenu menuKino = new JMenu("機能");
    private JMenuItem menuItemKinoJRA = new JMenuItem("レースヘルプ(JRA)");
    private JMenu menuHelp = new JMenu("ヘルプ");;
    private JMenuItem menuItemHelpInfo = new JMenuItem("情報");

    // テーブル用スクロールパネル
    private final JScrollPane scrollPaneLeft = new JScrollPane();
    private final JScrollPane scrollPaneRight = new JScrollPane();
    private final JScrollPane scrollPaneKakudo = new JScrollPane();

    // テーブル
    private final JTable tableWhere = new JTable();
    private final JTable tableResult = new JTable();
    private final JTable tableExpecation = new JTable();

    private final JFormattedTextField textFieldDistance = new JFormattedTextField(new DecimalFormat("####"));

    // コンボボックス
    private final JComboBox<String> comboBoxSurface = new JComboBox<>();
    private final JComboBox<String> comboBoxWeather = new JComboBox<>();
    private final JComboBox<String> comboBoxCourse = new JComboBox<>();

    // 押しボタン
    private final JButton btnClearTableLeft = new JButton("clear");
    private final JButton btnClearTableRight = new JButton("clear");
    private final JButton btnAnalyze = new JButton("Calcuration");
    private final JButton btnGetRaceInfo = new JButton("GetRace");

    // ラベル
    private final JLabel labelSurface = new JLabel("地面");
    private final JLabel labelWeather = new JLabel("天気");
    private final JLabel labelCourse = new JLabel("コース");
    private final JLabel labelDistance = new JLabel("距離");
    private final JLabel labelRaceTitle = new JLabel("");
    private final JLabel labelRace = new JLabel("レース情報");
    private final JLabel labelInfoTable1 = new JLabel("条件入力");
    private final JLabel labelInfoTable2 = new JLabel("結果表示");
    private final JLabel labelInfoTable3 = new JLabel("期待値");
    private JProgressBar progressBar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

                    MainPage frame = new MainPage();
                    frame.setVisible(true);

                    logger.info("hello application");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainPage() {

        logger.info("start constractor");
        textFieldDistance.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));

        textFieldDistance.setBounds(348, 40, 100, 32);
        textFieldDistance.setColumns(10);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1231, 721);

        setJMenuBar(menuBar);
        menuBar.add(menuFile);

        menuFile.add(menuItemFileExit);
        menuBar.add(menuKino);
        menuKino.add(menuItemKinoJRA);
        menuBar.add(menuHelp);
        menuHelp.add(menuItemHelpInfo);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        scrollPaneLeft.setBounds(12, 170, 364, 426);

        contentPane.add(scrollPaneLeft);
        tableWhere.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        tableWhere.setModel(new DefaultTableModel(
                new Object[][] {
                        { new Integer(1), null, null, null, null },
                        { new Integer(2), null, null, null, null },
                        { new Integer(3), null, null, null, null },
                        { new Integer(4), null, null, null, null },
                        { new Integer(5), null, null, null, null },
                        { new Integer(6), null, null, null, null },
                        { new Integer(7), null, null, null, null },
                        { new Integer(8), null, null, null, null },
                        { new Integer(9), null, null, null, null },
                        { new Integer(10), null, null, null, null },
                        { new Integer(11), null, null, null, null },
                        { new Integer(12), null, null, null, null },
                        { new Integer(13), null, null, null, null },
                        { new Integer(14), null, null, null, null },
                        { new Integer(15), null, null, null, null },
                        { new Integer(16), null, null, null, null },
                        { new Integer(17), null, null, null, null },
                        { new Integer(18), null, null, null, null },
                        { new Integer(19), null, null, null, null },
                        { new Integer(20), null, null, null, null },
                        { new Integer(21), null, null, null, null },
                        { new Integer(22), null, null, null, null },
                        { new Integer(23), null, null, null, null },
                        { new Integer(24), null, null, null, null },
                        { new Integer(25), null, null, null, null },
                },
                new String[] {
                        "\u99ACNo", "weight", "dhweight", "dsl", "odds"
                }) {
            Class[] columnTypes = new Class[] {
                    Integer.class, Double.class, Double.class, Integer.class, Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[] {
                    false, true, true, true, true
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableWhere.getColumnModel().getColumn(0).setPreferredWidth(41);

        scrollPaneLeft.setViewportView(tableWhere);
        comboBoxSurface.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        comboBoxSurface.setModel(new DefaultComboBoxModel<>(Const.surfaceArray));
        comboBoxSurface.setBounds(12, 39, 100, 33);

        contentPane.add(comboBoxSurface);
        comboBoxWeather.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        comboBoxWeather.setModel(new DefaultComboBoxModel<>(Const.weatherArray));
        comboBoxWeather.setBounds(124, 39, 100, 33);

        contentPane.add(comboBoxWeather);
        comboBoxCourse.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        comboBoxCourse.setModel(new DefaultComboBoxModel<>(Const.courseArray));
        comboBoxCourse.setBounds(236, 39, 100, 33);

        contentPane.add(comboBoxCourse);
        contentPane.add(textFieldDistance);
        labelSurface.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelSurface.setBounds(12, 10, 50, 18);
        contentPane.add(labelSurface);
        labelWeather.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelWeather.setBounds(124, 10, 50, 18);
        contentPane.add(labelWeather);
        labelCourse.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelCourse.setBounds(236, 10, 50, 18);
        contentPane.add(labelCourse);
        labelDistance.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelDistance.setBounds(348, 10, 50, 18);
        contentPane.add(labelDistance);
        btnClearTableLeft.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        btnClearTableLeft.setBounds(285, 606, 91, 34);
        contentPane.add(btnClearTableLeft);
        scrollPaneRight.setBounds(388, 170, 407, 426);

        contentPane.add(scrollPaneRight);
        tableResult.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        tableResult.setModel(new DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                },
                new String[] {
                        "\u56DE\u53CE\u7387(avg)", "\u52DD\u7387(%)", "\u30AB\u30A6\u30F3\u30C8(count)", "\u8CFC\u5165\u6570(sum)", "\u30DA\u30A4\u30AA\u30D5"
                }) {
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        scrollPaneRight.setViewportView(tableResult);
        btnClearTableRight.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        btnClearTableRight.setBounds(704, 606, 91, 34);
        contentPane.add(btnClearTableRight);
        btnAnalyze.setFont(new Font("MS UI Gothic", Font.BOLD, 16));
        btnAnalyze.setBounds(587, 11, 139, 48);
        contentPane.add(btnAnalyze);
        labelRaceTitle.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelRaceTitle.setBounds(124, 82, 364, 41);
        btnGetRaceInfo.setFont(new Font("MS UI Gothic", Font.BOLD, 16));
        btnGetRaceInfo.setBounds(460, 11, 117, 48);
        contentPane.add(btnGetRaceInfo);

        contentPane.add(labelRaceTitle);
        labelRace.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        labelRace.setBounds(22, 82, 100, 28);

        contentPane.add(labelRace);
        scrollPaneKakudo.setBounds(807, 170, 274, 426);

        contentPane.add(scrollPaneKakudo);
        tableExpecation.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
        tableExpecation.setModel(new DefaultTableModel(
                new Object[][] {
                        { new Integer(1), null, null, null },
                        { new Integer(2), null, null, null },
                        { new Integer(3), null, null, null },
                        { new Integer(4), null, null, null },
                        { new Integer(5), null, null, null },
                        { new Integer(6), null, null, null },
                        { new Integer(7), null, null, null },
                        { new Integer(8), null, null, null },
                        { new Integer(9), null, null, null },
                        { new Integer(10), null, null, null },
                        { new Integer(11), null, null, null },
                        { new Integer(12), null, null, null },
                        { new Integer(13), null, null, null },
                        { new Integer(14), null, null, null },
                        { new Integer(15), null, null, null },
                        { new Integer(16), null, null, null },
                        { new Integer(17), null, null, null },
                        { new Integer(18), null, null, null },
                        { new Integer(19), null, null, null },
                        { new Integer(20), null, null, null },
                        { new Integer(21), null, null, null },
                        { new Integer(22), null, null, null },
                        { new Integer(23), null, null, null },
                        { new Integer(24), null, null, null },
                        { new Integer(25), null, null, null },
                },
                new String[] {
                        "\u8CFC\u5165\u6570", "\u99ACNo", "\u52DD\u7387", "\u671F\u5F85\u5024"
                }) {
            Class[] columnTypes = new Class[] {
                    Integer.class, Integer.class, Object.class, Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[] {
                    false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableExpecation.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableExpecation.getColumnModel().getColumn(1).setPreferredWidth(50);
        tableExpecation.getColumnModel().getColumn(3).setPreferredWidth(100);

        scrollPaneKakudo.setViewportView(tableExpecation);
        labelInfoTable1.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));

        labelInfoTable1.setBounds(12, 133, 864, 27);
        contentPane.add(labelInfoTable1);
        labelInfoTable2.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));

        labelInfoTable2.setBounds(388, 133, 488, 27);
        contentPane.add(labelInfoTable2);
        labelInfoTable3.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));

        labelInfoTable3.setBounds(807, 133, 69, 27);
        contentPane.add(labelInfoTable3);

        progressBar = new JProgressBar();
        progressBar.setBounds(1057, 15, 146, 14);
        contentPane.add(progressBar);

        setTitle("NkViewer");

        // JTableのセルが編集中の状態でもコミットする
        // https://ateraimemo.com/Swing/TerminateEdit.html
        tableWhere.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // コンポーネントにハンドラーを登録する
        listenerHandle();

        // 起動時の各チェック
        initCheck();
    }

    /**
     * 起動時の各チェック
     */
    private void initCheck() {

        // データソースの読み込みチェック
        Const.dataSheetMap = DataSourceReader.readJsonFile(Const.dataFile);
        if (Const.dataSheetMap.isEmpty()) {
            new MessageBuilder()
                    .addLine("データソースが読み込みに失敗しています。")
                    .addLine("ログを開発者へ送付してください。")
                    .showMessage(contentPane, "警告", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * コンポーネントにハンドラーを登録する
     */
    private void listenerHandle() {

        // メニューの終了
        menuItemFileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        // [メニュー] - [ヘルプ] - [情報]
        menuItemHelpInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String label = Const.dataSheetMap.isEmpty()
                        ? "NG"
                        : "OK";

                String dbStatus = String.format("データソースの読込状況：%s", label);

                new MessageBuilder()
                        .addLine("NetKeiba Viewer")
                        .addLine("version: 3.0.3")
                        .addLine("copy right: kawabata 2020")
                        .addLine(dbStatus)
                        .showMessage(contentPane, "情報", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // (メニュー)JRAスクレイピング入力画面
        menuItemKinoJRA.addActionListener(
                getScrapingListener());

        btnGetRaceInfo.addActionListener(
                getScrapingListener());

        // 左テーブルクリアボタン
        btnClearTableLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableUtility.clearLeftTable(tableWhere);
            }
        });

        // 右テーブルクリアボタン
        btnClearTableRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableUtility.clearRightTable(tableResult);
            }
        });

        // 計算ボタン
        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startCalc();
            }
        });

        // DELETEキー押下でセルの値を削除
        tableWhere.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tableUtility.deleteCellValue(tableWhere);
            }
        });
    }

    /**
     * 左テーブルの入力値から右テーブルの計算を行う。
     */
    private void startCalc() {

        if (inputValidationIsOk()) {

            // 入力値をモデルへセット
            CalcModel model = new CalcModel();
            model.setInputedDistance(Integer.valueOf(textFieldDistance.getText()));
            model.setSelectedCourse(comboBoxCourse.getSelectedIndex());
            model.setSelectedSurface(comboBoxSurface.getSelectedIndex());
            model.setSelectedWeather(comboBoxWeather.getSelectedIndex());
            model.setTableList(tableUtility.conv2LeftTableModel(tableWhere));

            try {

                // 右テーブルを計算
                List<RightTable> resultList = calcInstance.calcRightTable(model);
                tableUtility.setValueToRightTable(tableResult, resultList);

                // 期待値テーブル処理
                List<ExpecationModel> expectationList = expectationLogic.getKitaichiList(resultList);
                tableUtility.setValueToExpectationTable(tableExpecation, expectationList);

            } catch (Exception e) {

                e.printStackTrace();
                logger.error("", e.fillInStackTrace());
                JOptionPane.showMessageDialog(this, "計算が異常終了しました。", "ERROR", JOptionPane.ERROR_MESSAGE);

            }

        }
    }

    /**
     * 右テーブル計算前の入力値チェックを行う
     *
     * @return true or false
     */
    private boolean inputValidationIsOk() {

        MessageBuilder msgBuilder = new MessageBuilder();

        if (Const.dataSheetMap == null || Const.dataSheetMap.isEmpty()) {
            msgBuilder.addLine("データファイルが読み込まれていません。");
        }

        if (StringUtils.isBlank(textFieldDistance.getText())) {
            msgBuilder.addLine("レース距離は必須入力です。");
        } else if (GetSheetData.getDistanceKey(textFieldDistance.getText()) == null) {
            msgBuilder.addLine("入力されたレース距離は過去データに存在しません。");
        }

        if (jComboBoxUtility.isNoneSelected(comboBoxCourse)) {
            msgBuilder.addLine("コースが未選択です。");
        }

        if (jComboBoxUtility.isNoneSelected(comboBoxSurface)) {
            msgBuilder.addLine("地面が未選択です。");
        }

        if (jComboBoxUtility.isNoneSelected(comboBoxWeather)) {
            msgBuilder.addLine("天気が未選択です。");
        }

        if (tableUtility.isEmptyLeftTable(tableWhere)) {
            msgBuilder.addLine("左テーブルに値が何も入力されていません。");
        }

        if (!msgBuilder.isEmpty()) {
            msgBuilder.showMessage(contentPane, "入力チェック", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;

    }

    /**
     * システム終了処理
     */
    public void exit() {
        ScraipingRaceFromJRA.close();
        System.exit(0);
    }

    /**
     * スクレイピング画面を開くイベントを返す。
     *
     * @return
     */
    private ActionListener getScrapingListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // 取得レースの選択画面を表示
                CollectJRARaceInfoPage popup = scraipSetter.showDialog(contentPane);

                // OKが押された場合だけ処理する
                if (popup.getModel().getCloseAction() == ScraiperConst.Closeby.ok) {

                    tableUtility.clearLeftTable(tableWhere);
                    tableUtility.clearRightTable(tableResult);

                    progressBar.setIndeterminate(true);

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            scraipSetter.setScraipingResult(
                                    popup.getModel(),
                                    contentPane,
                                    comboBoxCourse,
                                    comboBoxSurface,
                                    comboBoxWeather,
                                    textFieldDistance,
                                    tableWhere,
                                    labelRaceTitle);

                            progressBar.setIndeterminate(false);
                        }
                    });

                    t.start();
                }
            }
        };
    }
}
