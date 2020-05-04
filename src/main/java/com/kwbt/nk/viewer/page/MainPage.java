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
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.Calculation;
import com.kwbt.nk.viewer.util.DataSourceReader;
import com.kwbt.nk.viewer.util.GetSheetData;
import com.kwbt.nk.viewer.util.JComboBoxUtility;
import com.kwbt.nk.viewer.util.MessageBuilder;
import com.kwbt.nk.viewer.util.ScraipingSetter;
import com.kwbt.nk.viewer.util.TableUtility;

public class MainPage extends JFrame {

    private final static Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final static TableUtility tableUtility = new TableUtility();
    private final static JComboBoxUtility jComboBoxUtility = new JComboBoxUtility();
    private final static ScraipingSetter scraipSetter = new ScraipingSetter();
    private final Calculation calcInstance = new Calculation();

    /**
     * 画面コンポーネント
     */
    private JPanel contentPane;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("ファイル");
    private JMenuItem menuItemFileExit = new JMenuItem("終了");
    private JMenu menuKino = new JMenu("機能");
    private JMenuItem menuItemKinoJRA = new JMenuItem("レースヘルプ(JRA)");
    private JMenu menuHelp = new JMenu("ヘルプ");;
    private JMenuItem menuItemHelpInfo = new JMenuItem("情報");
    private final JScrollPane scrollPaneLeft = new JScrollPane();
    private final JScrollPane scrollPaneRight = new JScrollPane();
    private final JScrollPane scrollPaneKakudo = new JScrollPane();
    private final JTable tableLeft = new JTable();
    private final JTable tableRight = new JTable();
    private final JTable tableKakudo = new JTable();
    private final JFormattedTextField textFieldDistance = new JFormattedTextField(new DecimalFormat("####"));
    private final JComboBox<String> comboBoxSurface = new JComboBox<>();
    private final JComboBox<String> comboBoxWeather = new JComboBox<>();
    private final JComboBox<String> comboBoxCourse = new JComboBox<>();
    private final JButton btnClearTableLeft = new JButton("clear");
    private final JButton btnClearTableRight = new JButton("clear");
    private final JButton btnAnalyze = new JButton("analyze");
    private final JLabel labelSurface = new JLabel("地面");
    private final JLabel labelWeather = new JLabel("天気");
    private final JLabel labelCourse = new JLabel("コース");
    private final JLabel labelDistance = new JLabel("距離");
    private final JLabel labelRaceTitle = new JLabel("");
    private final JLabel labelRace = new JLabel("レース情報");

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

        textFieldDistance.setBounds(348, 40, 100, 20);
        textFieldDistance.setColumns(10);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1033, 682);

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
        scrollPaneLeft.setBounds(12, 149, 364, 426);

        contentPane.add(scrollPaneLeft);
        tableLeft.setModel(new DefaultTableModel(
                new Object[][] {
                        { Integer.valueOf(1), null, null, null, null },
                        { Integer.valueOf(2), null, null, null, null },
                        { Integer.valueOf(3), null, null, null, null },
                        { Integer.valueOf(4), null, null, null, null },
                        { Integer.valueOf(5), null, null, null, null },
                        { Integer.valueOf(6), null, null, null, null },
                        { Integer.valueOf(7), null, null, null, null },
                        { Integer.valueOf(8), null, null, null, null },
                        { Integer.valueOf(9), null, null, null, null },
                        { Integer.valueOf(10), null, null, null, null },
                        { Integer.valueOf(11), null, null, null, null },
                        { Integer.valueOf(12), null, null, null, null },
                        { Integer.valueOf(13), null, null, null, null },
                        { Integer.valueOf(14), null, null, null, null },
                        { Integer.valueOf(15), null, null, null, null },
                        { Integer.valueOf(16), null, null, null, null },
                        { Integer.valueOf(17), null, null, null, null },
                        { Integer.valueOf(18), null, null, null, null },
                        { Integer.valueOf(19), null, null, null, null },
                        { Integer.valueOf(20), null, null, null, null },
                        { Integer.valueOf(21), null, null, null, null },
                        { Integer.valueOf(22), null, null, null, null },
                        { Integer.valueOf(23), null, null, null, null },
                        { Integer.valueOf(24), null, null, null, null },
                        { Integer.valueOf(25), null, null, null, null },
                },
                new String[] {
                        "No", "weight", "dhweight", "dsl", "odds"
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
        tableLeft.getColumnModel().getColumn(0).setPreferredWidth(41);

        scrollPaneLeft.setViewportView(tableLeft);
        comboBoxSurface.setModel(new DefaultComboBoxModel<>(Const.surfaceArray));
        comboBoxSurface.setBounds(12, 39, 100, 20);

        contentPane.add(comboBoxSurface);
        comboBoxWeather.setModel(new DefaultComboBoxModel<>(Const.weatherArray));
        comboBoxWeather.setBounds(124, 39, 100, 20);

        contentPane.add(comboBoxWeather);
        comboBoxCourse.setModel(new DefaultComboBoxModel<>(Const.courseArray));
        comboBoxCourse.setBounds(236, 39, 100, 20);

        contentPane.add(comboBoxCourse);
        contentPane.add(textFieldDistance);
        labelSurface.setBounds(12, 15, 50, 13);
        contentPane.add(labelSurface);
        labelWeather.setBounds(124, 15, 50, 13);
        contentPane.add(labelWeather);
        labelCourse.setBounds(236, 15, 50, 13);
        contentPane.add(labelCourse);
        labelDistance.setBounds(348, 15, 50, 13);
        contentPane.add(labelDistance);
        btnClearTableLeft.setBounds(283, 585, 91, 21);
        contentPane.add(btnClearTableLeft);
        scrollPaneRight.setBounds(390, 149, 407, 426);

        contentPane.add(scrollPaneRight);
        tableRight.setModel(new DefaultTableModel(
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
                        "回収率(avg)", "勝率(%)", "カウント(count)", "購入数(sum)", "ペイオフ(avg)"
                }) {
            boolean[] columnEditables = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });

        scrollPaneRight.setViewportView(tableRight);
        btnClearTableRight.setBounds(706, 585, 91, 21);
        contentPane.add(btnClearTableRight);
        btnAnalyze.setFont(new Font("MS UI Gothic", Font.BOLD, 16));
        btnAnalyze.setBounds(470, 11, 117, 48);
        contentPane.add(btnAnalyze);
        labelRaceTitle.setBounds(12, 91, 364, 48);

        contentPane.add(labelRaceTitle);
        labelRace.setBounds(12, 69, 71, 13);

        contentPane.add(labelRace);
        scrollPaneKakudo.setBounds(809, 149, 192, 426);

        contentPane.add(scrollPaneKakudo);
        tableKakudo.setModel(new DefaultTableModel(
                new Object[][] {
                        { Integer.valueOf(1), null },
                        { Integer.valueOf(2), null },
                        { Integer.valueOf(3), null },
                        { Integer.valueOf(4), null },
                        { Integer.valueOf(5), null },
                        { Integer.valueOf(6), null },
                        { Integer.valueOf(7), null },
                        { Integer.valueOf(8), null },
                        { Integer.valueOf(9), null },
                        { Integer.valueOf(10), null },
                        { Integer.valueOf(11), null },
                        { Integer.valueOf(12), null },
                        { Integer.valueOf(13), null },
                        { Integer.valueOf(14), null },
                        { Integer.valueOf(15), null },
                        { Integer.valueOf(16), null },
                        { Integer.valueOf(17), null },
                        { Integer.valueOf(18), null },
                        { Integer.valueOf(19), null },
                        { Integer.valueOf(20), null },
                        { Integer.valueOf(21), null },
                        { Integer.valueOf(22), null },
                        { Integer.valueOf(23), null },
                        { Integer.valueOf(24), null },
                        { Integer.valueOf(25), null },
                },
                new String[] {
                        "購入数", "回収率"
                }) {
            Class[] columnTypes = new Class[] {
                    Integer.class, Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[] {
                    false, true
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableKakudo.getColumnModel().getColumn(0).setPreferredWidth(56);
        tableKakudo.getColumnModel().getColumn(1).setPreferredWidth(108);

        scrollPaneKakudo.setViewportView(tableKakudo);

        setTitle("NkViewer");

        // JTableのセルが編集中の状態でもコミットする
        // https://ateraimemo.com/Swing/TerminateEdit.html
        tableLeft.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        listenerHandle();

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
                MessageBuilder msgBuilder = new MessageBuilder();
                msgBuilder.addLine("NetKeiba Viewer");
                msgBuilder.addLine("version: 3.0.2");
                msgBuilder.addLine("copy right: kawabata 2019");
                msgBuilder.addLine(String.format("データソースの読込状況：%s",
                        Const.dataSheetMap.isEmpty()
                                ? "NG"
                                : "OK"));
                msgBuilder.showMessage(contentPane, "情報", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // JRAスクレイピング入力画面
        menuItemKinoJRA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CollectJRARaceInfoPage popup = scraipSetter.showDialog(contentPane);
                if (popup.getModel().getCloseAction() == ScraiperConst.Closeby.ok) {

                    ProgessBar bar1 = new ProgessBar(contentPane) {

                        @Override
                        public void registMethod() {
                            scraipSetter.setScraipingResult(
                                    popup.getModel(),
                                    contentPane,
                                    comboBoxCourse,
                                    comboBoxSurface,
                                    comboBoxWeather,
                                    textFieldDistance,
                                    tableLeft,
                                    labelRaceTitle);
                        }
                    };
                    bar1.start();
                }
            }
        });

        // 左テーブルクリアボタン
        btnClearTableLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableUtility.clearLeftTable(tableLeft);
            }
        });

        // 右テーブルクリアボタン
        btnClearTableRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableUtility.clearRightTable(tableRight);
            }
        });

        // 計算ボタン
        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startCalc();
            }
        });

        // DELETEキー押下でセルの値を削除
        tableLeft.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tableUtility.deleteCellValue(tableLeft);
            }
        });
    }

    /**
     * 左テーブルの入力値から右テーブルの計算を行う。
     */
    private void startCalc() {

        if (inputValidation()) {

            // 入力値をモデルへセット
            CalcModel model = new CalcModel();
            model.setInputedDistance(Integer.valueOf(textFieldDistance.getText()));
            model.setSelectedCourse(comboBoxCourse.getSelectedIndex());
            model.setSelectedSurface(comboBoxSurface.getSelectedIndex());
            model.setSelectedWeather(comboBoxWeather.getSelectedIndex());
            model.setTableList(tableUtility.getLeftTableModelList(tableLeft));

            // 右テーブルを計算
            List<RightTable> resultList = calcInstance.calcRightTable(model);
            tableUtility.setValueToRightTable(tableRight, resultList);
        }
    }

    /**
     * 右テーブル計算前の入力値チェックを行う
     *
     * @return true or false
     */
    private boolean inputValidation() {

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

        if (tableUtility.isEmptyLeftTable(tableLeft)) {
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
}
