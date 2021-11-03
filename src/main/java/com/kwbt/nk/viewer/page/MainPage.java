package com.kwbt.nk.viewer.page;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.common.MatcherConst;
import com.kwbt.nk.scraip.NkInput;
import com.kwbt.nk.scraip.NkOutput;
import com.kwbt.nk.scraip.NkScraip;
import com.kwbt.nk.viewer.constant.Const;
import com.kwbt.nk.viewer.core.Calculation;
import com.kwbt.nk.viewer.helper.TableRefresh;
import com.kwbt.nk.viewer.model.CalcModel;
import com.kwbt.nk.viewer.model.LeftTable;
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
    private final JButton button_calc = new JButton("勝率算出");
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
    private final JTable table = new JTable();
    private final JTextField input_roundNumber;

    /**
     * その他部品
     */
    private final TableRefresh tableRefresh = new TableRefresh();
    private final TableUtility tableUtility = new TableUtility();
    private final NkScraip scraip = new NkScraip();
    private final Calculation calculation = new Calculation();

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
        setBounds(100, 100, 1228, 661);

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

        panel_1.setBounds(230, 153, 292, 21);
        contentPane.add(panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 107, 81, 81, 0 };
        gbl_panel_1.rowHeights = new int[] { 21, 0 };
        gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);
        GridBagConstraints gbc_button_getRaceInfo = new GridBagConstraints();
        gbc_button_getRaceInfo.fill = GridBagConstraints.HORIZONTAL;
        gbc_button_getRaceInfo.anchor = GridBagConstraints.NORTH;
        gbc_button_getRaceInfo.insets = new Insets(0, 0, 0, 5);
        gbc_button_getRaceInfo.gridx = 0;
        gbc_button_getRaceInfo.gridy = 0;
        panel_1.add(button_getRaceInfo, gbc_button_getRaceInfo);
        GridBagConstraints gbc_button_calc = new GridBagConstraints();
        gbc_button_calc.fill = GridBagConstraints.HORIZONTAL;
        gbc_button_calc.anchor = GridBagConstraints.NORTH;
        gbc_button_calc.insets = new Insets(0, 0, 0, 5);
        gbc_button_calc.gridx = 1;
        gbc_button_calc.gridy = 0;
        panel_1.add(button_calc, gbc_button_calc);

        GridBagConstraints gbc_button_refresh = new GridBagConstraints();
        gbc_button_refresh.fill = GridBagConstraints.HORIZONTAL;
        gbc_button_refresh.anchor = GridBagConstraints.NORTH;
        gbc_button_refresh.gridx = 2;
        gbc_button_refresh.gridy = 0;
        panel_1.add(button_refresh, gbc_button_refresh);
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
                        new String[] { "No", "weight", "dhweight", "dsl", "odds" }) {
                    boolean[] columnEditables = new boolean[] { true, false, true, true, true };

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
                        new String[] { "\u56DE\u53CE\u7387(avg)", "\u52DD\u7387(%)", "\u7DCF\u6570", "\u8CFC\u5165\u6570", "\u30DA\u30A4\u30AA\u30D5" }) {
                    boolean[] columnEditables = new boolean[] { false, false, false, false, false };

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
        scrollPaneHorse_1_1.setBounds(822, 222, 300, 355);
        contentPane.add(scrollPaneHorse_1_1);
        table.setModel(new DefaultTableModel(
                new Object[][] {
                        { "1", null, null, null },
                        { "2", null, null, null },
                        { "3", null, null, null },
                        { "4", null, null, null },
                        { "5", null, null, null },
                        { "6", null, null, null },
                        { "7", null, null, null },
                        { "8", null, null, null },
                        { "9", null, null, null },
                        { "10", null, null, null },
                        { "11", null, null, null },
                        { "12", null, null, null },
                        { "13", null, null, null },
                        { "14", null, null, null },
                        { "15", null, null, null },
                        { "16", null, null, null },
                        { "17", null, null, null },
                        { "18", null, null, null },
                        { "19", null, null, null },
                        { "20", null, null, null },
                },
                new String[] {
                        "\u8CFC\u5165\u6570", "\u99ACNo", "\u52DD\u7387", "\u671F\u5F85\u5024"
                }) {
            boolean[] columnEditables = new boolean[] {
                    false, true, true, true
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(41);

        scrollPaneHorse_1_1.setViewportView(table);

        JPanel group_round = new JPanel();
        group_round.setLayout(null);
        group_round.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "ラウンド", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_round.setBounds(525, 20, 84, 53);
        contentPane.add(group_round);

        input_roundNumber = new JTextField();
        input_roundNumber.setBounds(12, 20, 44, 19);
        group_round.add(input_roundNumber);
        input_roundNumber.setColumns(10);

        JLabel labelStr01_1_1_1 = new JLabel("R");
        labelStr01_1_1_1.setBounds(60, 23, 17, 13);
        group_round.add(labelStr01_1_1_1);

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

                NkOutput output = scraiping();
                if (Objects.isNull(output)) {
                    return;
                }

                CalcModel model = change(output);
                List<RightTable> result = calculation.calcRightTable(model);
                tableUtility.setValueToRightTable(table_calc, result);
            }
        });

    }

    /**
     * スクレイピング実行
     */
    private NkOutput scraiping() {

        if (inputValidateIsNG()) {
            return null;
        }

        NkInput input = new NkInput();

        String 開催地selection = group_raceField.getSelection().getActionCommand();
        String 曜日selection = group_raceWeekday.getSelection().getActionCommand();

        input.set選択開催地(NkInput.開催地.get(Integer.valueOf(開催地selection)));
        input.set選択曜日(NkInput.曜日.get(Integer.valueOf(曜日selection)));
        input.setラウンド(Integer.valueOf(input_roundNumber.getText()));

        try {

            NkOutput output = scraip.letsScraip(input);

            label_course.setText(output.getCourse());
            label_surface.setText(output.getSurface());
            label_raceName.setText(output.getRaceTitle());
            label_distance.setText(output.getDistance());
            label_weather.setText(output.getWeather());
            tableUtility.setJRARaceInfoToLeftTable(table_horse, output.getHorses());
            return output;

        } catch (Throwable e) {
            logger.error("スクレイピング処理に失敗", e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * スクレイプ前の入力チェック
     * 
     * @return
     */
    private boolean inputValidateIsNG() {

        MessageBuilder msgBuilder = new MessageBuilder();

        if (StringUtils.isBlank(input_roundNumber.getText())) {
            msgBuilder.addLine("レース番号（ラウンド）が入力されていません。");
        }

        if (!StringUtils.isNumeric(input_roundNumber.getText())) {
            msgBuilder.addLine("レース番号（ラウンド）は数字で入力してください。");
        }

        if (!msgBuilder.isEmpty()) {
            msgBuilder.showMessage(this, "入力チェック", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        return false;
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
     * スクレイプした情報（日本語）を<br>
     * 解析用キー（数値）に変換する
     * 
     * @param output
     * @return
     */
    private CalcModel change(NkOutput output) {

        CalcModel model = new CalcModel();
        // コース
        {
            Integer key = MatcherConst.courseMap
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(output.getCourse()))
                    .findFirst()
                    .get()
                    .getKey();
            model.setSelectedCourse(key);
        }

        // 地面
        {
            Integer key = MatcherConst.surfaceMap
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(output.getSurface().replaceAll("ダート", "ダ")))
                    .findFirst()
                    .get()
                    .getKey();
            model.setSelectedSurface(key);
        }

        // 天気
        {
            Integer key = MatcherConst.weatherMap
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(output.getWeather()))
                    .findFirst()
                    .get()
                    .getKey();
            model.setSelectedWeather(key);
        }

        // 距離
        model.setInputedDistance(Integer.valueOf(output.getDistance()));

        // 馬テーブルの情報
        List<LeftTable> tableList = tableUtility.conv2LeftTableModel(table_horse);
        model.setTableList(tableList);

        return model;
    }
}
