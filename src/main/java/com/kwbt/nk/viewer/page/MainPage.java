package com.kwbt.nk.viewer.page;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraip.NkInput;
import com.kwbt.nk.scraip.NkOutput;
import com.kwbt.nk.scraip.NkScraip;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.core.CalcExpectation;
import com.kwbt.nk.viewer.core.Calculation;
import com.kwbt.nk.viewer.helper.TableRefresh;
import com.kwbt.nk.viewer.model.ExpectModel;
import com.kwbt.nk.viewer.model.RightTable;
import com.kwbt.nk.viewer.util.DataSourceReader;
import com.kwbt.nk.viewer.util.MessageBuilder;
import com.kwbt.nk.viewer.util.TableUtility;

public class MainPage extends JFrame {

    /** ロガー */
    private final static Logger logger = LoggerFactory.getLogger(MainPage.class);

    /**
     * 画面コンポーネント
     */
    private JPanel contentPane;

    private final ButtonGroup group_raceField = new ButtonGroup();
    private final ButtonGroup group_raceWeekday = new ButtonGroup();

    // メニュー
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuFile = new JMenu("ファイル");
    private final JMenuItem menuItemFileExit = new JMenuItem("終了");
    private final JMenu menuHelp = new JMenu("ヘルプ");;
    private final JMenuItem menuItemHelpInfo = new JMenuItem("情報");
    private final JLabel labelStr03 = new JLabel("地面");
    private final JLabel labelStr05 = new JLabel("コース");
    private final JLabel labelStr06 = new JLabel("天気");
    private final JLabel labelStr07 = new JLabel("レース名");
    private final JRadioButton input_radioButton_tokyo = new JRadioButton("東京(新潟)");
    private final JRadioButton input_radioButton_kyoto = new JRadioButton("京都(小倉)");
    private final JRadioButton input_radioButton_nigata = new JRadioButton("新潟(札幌)");
    private final JRadioButton input_radioButton_sat = new JRadioButton("土曜");
    private final JRadioButton input_radioButton_sun = new JRadioButton("日曜");
    private final JLabel labelStr01_1 = new JLabel("レース情報");
    private final JScrollPane scrollPaneHorse = new JScrollPane();
    private final JTable table_horse = new JTable();
    private final JButton button_getRaceInfo = new JButton("レース情報取得");
    private final JScrollPane scrollPaneHorse_1 = new JScrollPane();
    private final JTable table_calc = new JTable();
    private final JPanel group_place = new JPanel();
    private final JPanel group_days = new JPanel();
    private final JPanel panel = new JPanel();
    private final JLabel label_distance = new JLabel("");
    private final JLabel label_weather = new JLabel("");
    private final JLabel labelStr04_1 = new JLabel("距離");
    private final JLabel label_course = new JLabel("");
    private final JLabel label_surface = new JLabel("");
    private final JLabel label_raceName = new JLabel("");
    private final JButton button_refresh = new JButton("リフレッシュ");
    private final JPanel panel_1 = new JPanel();
    private final JTable table_expection = new JTable();
    private final JComboBox<Integer> input_roundNumber = new JComboBox<>();

    /**
     * その他部品
     */
    private final TableRefresh tableRefresh = new TableRefresh();
    private final TableUtility tableUtility = new TableUtility();
    private final NkScraip scraip = new NkScraip();
    private final Calculation calculation = new Calculation();
    private final CalcExpectation calcExpect = new CalcExpectation();

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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 661);

        setJMenuBar(menuBar);
        menuBar.add(menuFile);

        menuFile.add(menuItemFileExit);
        menuBar.add(menuHelp);
        menuHelp.add(menuItemHelpInfo);

        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        group_place.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u958B\u50AC\u5730", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_place.setBounds(330, 20, 93, 94);

        contentPane.add(group_place);
        group_place.setLayout(new GridLayout(0, 1, 0, 0));
        group_place.add(input_radioButton_tokyo);
        input_radioButton_tokyo.setSelected(true);
        input_radioButton_tokyo.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        input_radioButton_tokyo.setActionCommand(String.valueOf(NkInput.開催地.東京.getInt()));

        group_raceField.add(input_radioButton_tokyo);
        group_place.add(input_radioButton_kyoto);
        input_radioButton_kyoto.setSelected(true);
        input_radioButton_kyoto.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        input_radioButton_kyoto.setActionCommand(String.valueOf(NkInput.開催地.阪神.getInt()));
        group_raceField.add(input_radioButton_kyoto);
        group_place.add(input_radioButton_nigata);
        input_radioButton_nigata.setSelected(true);
        input_radioButton_nigata.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        input_radioButton_nigata.setActionCommand(String.valueOf(NkInput.開催地.新潟.getInt()));
        group_raceField.add(input_radioButton_nigata);
        group_days.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u66DC\u65E5", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_days.setBounds(450, 20, 61, 68);

        contentPane.add(group_days);
        group_days.setLayout(null);

        group_raceWeekday.add(input_radioButton_sat);
        input_radioButton_sat.setBounds(6, 15, 49, 21);
        group_days.add(input_radioButton_sat);
        input_radioButton_sat.setSelected(true);
        input_radioButton_sat.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        input_radioButton_sat.setActionCommand(String.valueOf(NkInput.曜日.土曜.getInt()));
        group_raceWeekday.add(input_radioButton_sun);
        input_radioButton_sun.setBounds(6, 41, 49, 21);
        group_days.add(input_radioButton_sun);
        input_radioButton_sun.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        input_radioButton_sun.setActionCommand(String.valueOf(NkInput.曜日.日曜.getInt()));

        panel_1.setBounds(245, 153, 220, 21);
        contentPane.add(panel_1);
        panel_1.setLayout(new GridLayout(0, 2, 0, 0));
        panel_1.add(button_getRaceInfo);
        panel_1.add(button_refresh);
        labelStr01_1.setBounds(25, 199, 93, 13);
        labelStr01_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        contentPane.add(labelStr01_1);
        scrollPaneHorse.setBounds(17, 222, 406, 355);
        contentPane.add(scrollPaneHorse);
        table_horse.setModel(
                new DefaultTableModel(
                        new Object[][] {
                                { "1", null, null, null, null },
                                { "2", null, null, null, null },
                                { "3", null, null, null, null },
                                { "4", null, null, null, null },
                                { "5", null, null, null, null },
                                { "6", null, null, null, null },
                                { "7", null, null, null, null },
                                { "8", null, null, null, null },
                                { "9", null, null, null, null },
                                { "10", null, null, null, null },
                                { "11", null, null, null, null },
                                { "12", null, null, null, null },
                                { "13", null, null, null, null },
                                { "14", null, null, null, null },
                                { "15", null, null, null, null },
                                { "16", null, null, null, null },
                                { "17", null, null, null, null },
                                { "18", null, null, null, null },
                                { "19", null, null, null, null },
                                { "20", null, null, null, null },
                        },
                        new String[] {
                                "No", "weight", "dhweight", "dsl", "odds"
                        }) {
                    boolean[] columnEditables = new boolean[] {
                            true, false, false, false, false
                    };

                    public boolean isCellEditable(int row, int column) {
                        return columnEditables[column];
                    }
                });
        table_horse.getColumnModel().getColumn(0).setPreferredWidth(35);
        table_horse.getColumnModel().getColumn(1).setPreferredWidth(60);
        table_horse.getColumnModel().getColumn(2).setPreferredWidth(60);
        table_horse.getColumnModel().getColumn(3).setPreferredWidth(60);
        table_horse.getColumnModel().getColumn(4).setPreferredWidth(60);
        table_horse.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));

        scrollPaneHorse.setViewportView(table_horse);
        scrollPaneHorse_1.setBounds(434, 221, 376, 355);
        contentPane.add(scrollPaneHorse_1);
        table_calc.setModel(
                new DefaultTableModel(
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
                        },
                        new String[] {
                                "\u56DE\u53CE\u7387(avg)", "\u52DD\u7387(%)", "\u7DCF\u6570", "\u8CFC\u5165\u6570", "\u30DA\u30A4\u30AA\u30D5"
                        }) {
                    boolean[] columnEditables = new boolean[] {
                            false, false, false, false, false
                    };

                    public boolean isCellEditable(int row, int column) {
                        return columnEditables[column];
                    }
                });
        table_calc.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));

        scrollPaneHorse_1.setViewportView(table_calc);
        panel.setBorder(
                new TitledBorder(
                        new EtchedBorder(
                                EtchedBorder.LOWERED,
                                new Color(255, 255, 255),
                                new Color(160, 160, 160)),
                        "\u30EC\u30FC\u30B9\u60C5\u5831",
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        null,
                        new Color(0, 0, 0)));
        panel.setBounds(16, 20, 275, 128);

        contentPane.add(panel);
        panel.setLayout(new GridLayout(0, 2, 0, 0));
        panel.add(labelStr07);

        panel.add(label_raceName);

        panel.add(labelStr04_1);

        panel.add(label_distance);
        panel.add(labelStr03);

        panel.add(label_surface);
        panel.add(labelStr05);

        panel.add(label_course);
        panel.add(labelStr06);

        panel.add(label_weather);

        JLabel labelStr01_1_1 = new JLabel("予想結果");
        labelStr01_1_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        labelStr01_1_1.setBounds(434, 199, 93, 13);
        contentPane.add(labelStr01_1_1);

        JScrollPane scrollPaneHorse_1_1 = new JScrollPane();
        scrollPaneHorse_1_1.setBounds(822, 222, 246, 355);
        contentPane.add(scrollPaneHorse_1_1);
        table_expection.setModel(new DefaultTableModel(
                new Object[][] {
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                },
                new String[] {
                        "\u99ACNo", "\u52DD\u7387", "\u671F\u5F85\u5024"
                }));

        scrollPaneHorse_1_1.setViewportView(table_expection);

        JPanel group_round = new JPanel();
        group_round.setLayout(null);
        group_round.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "ラウンド", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_round.setBounds(525, 20, 84, 53);
        contentPane.add(group_round);

        JLabel labelStr01_1_1_1 = new JLabel("R");
        labelStr01_1_1_1.setBounds(60, 23, 17, 13);
        group_round.add(labelStr01_1_1_1);

        input_roundNumber.setMaximumRowCount(16);
        input_roundNumber.setBounds(11, 19, 45, 21);
        group_round.add(input_roundNumber);
        input_roundNumber.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }));

        tableUtility.tableRendererRight(table_horse);
        tableUtility.tableRendererRight(table_calc);

        setTitle("NkViewer");

        listenerHandle();

        // dataSourceの読み込み
        Const.dataSheetMap = DataSourceReader.readJsonFile(Const.dataFile);
    }

    /**
     * コンポーネントにハンドラーを登録する
     */
    private void listenerHandle() {

        // メニューの終了処理
        menuItemFileExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        // バージョン情報
        menuItemHelpInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new MessageBuilder()
                        .addLine("Netkeiba Viewer")
                        .addLine("version: 4.0")
                        .addLine("copy right: kawabata 2021")
                        .showMessage(contentPane, "情報", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // リフレッシュ
        button_refresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        // レース情報をスクレイピング
        button_getRaceInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                tableRefresh.refreshTableHasNo(table_horse);
                tableRefresh.refreshTableNotContainNo(table_calc);
                tableRefresh.refreshTableNotContainNo(table_expection);

                // スクレイピング
                NkOutput output = scraiping();
                if (Objects.isNull(output)) {
                    return;
                }

                // 計算実施
                List<RightTable> rightTable = calcuration(output);
                if (Objects.isNull(rightTable)) {
                    return;
                }

                // 期待値計算
                List<ExpectModel> expection = calcExpect.exec(rightTable);
                tableUtility.setValueToExpectationTable(table_expection, expection);
            }
        });
    }

    /**
     * スクレイピング実行
     */
    private NkOutput scraiping() {

        NkInput input = new NkInput();

        String 開催地selection = group_raceField.getSelection().getActionCommand();
        String 曜日selection = group_raceWeekday.getSelection().getActionCommand();

        input.set選択開催地(NkInput.開催地.get(Integer.valueOf(開催地selection)));
        input.set選択曜日(NkInput.曜日.get(Integer.valueOf(曜日selection)));

        // TODO
        input.setラウンド(Integer.valueOf(input_roundNumber.getSelectedIndex() + 1));

        try {

            NkOutput output = scraip.letsScraip(input);

            label_course.setText(output.getCourse());
            label_surface.setText(output.getSurface());
            label_raceName.setText(output.getRaceTitle());
            label_distance.setText(output.getDistance());
            label_weather.setText(output.getWeather());
            tableUtility.setJRARaceInfoToLeftTable(table_horse, output.getHorses());

            return output;

        } catch (NoSuchElementException e) {

            e.printStackTrace();
            logger.error("スクレイピング処理に失敗", e);
            new MessageBuilder()
                    .addLine("スクレイピングに失敗しました。")
                    .addLine("以下の理由が考えられます。")
                    .addLine("1．サイトの掲載場所が変わっている")
                    .addLine("2．次週レース前（水曜、木曜）は空ページになるため、スクレイピング不可")
                    .showMessage(this, "警告", JOptionPane.WARNING_MESSAGE);

        } catch (Throwable e) {

            e.printStackTrace();
            logger.error("スクレイピング処理に失敗", e);
            new MessageBuilder()
                    .addLine("スクレイピングが原因不明の理由により失敗しました。")
                    .addLine("ログファイルを管理者に送付してください。")
                    .showMessage(this, "エラー", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    /**
     * システム終了処理
     */
    public void exit() {
        try {
            scraip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * 入力項目のクリア
     */
    private void refresh() {

        tableRefresh.refreshTableHasNo(table_horse);
        tableRefresh.refreshTableNotContainNo(table_calc);
        label_course.setText("");
        label_surface.setText("");
        label_raceName.setText("");
        label_distance.setText("");
        label_weather.setText("");

        input_radioButton_tokyo.setSelected(true);
        input_radioButton_sat.setSelected(true);
    }

    /**
     * 右テーブルの計算
     * 
     * @param output
     */
    private List<RightTable> calcuration(NkOutput output) {

        try {

            List<RightTable> result = calculation.calcRight(output);
            tableUtility.setValueToRightTable(table_calc, result);

            return result;

        } catch (Exception e) {

            logger.error("計算処理が失敗しました。", e);
            new MessageBuilder()
                    .addLine("計算処理が失敗しました。")
                    .addLine("ログファイルを管理者に送付してください。")
                    .showMessage(this, "エラー", JOptionPane.ERROR_MESSAGE);

            return null;
        }
    }
}
