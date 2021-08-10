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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kwbt.nk.scraiper.util.ScraipingRaceFromJRA;
import com.kwbt.nk.viewer.helper.Refresh;
import com.kwbt.nk.viewer.helper.Scraiper;
import com.kwbt.nk.viewer.util.MessageBuilder;

public class MainPageSmall extends JFrame {

    /** ロガー */
    private final static Logger logger = LoggerFactory.getLogger(MainPageSmall.class);

    /**
     * 画面コンポーネント
     */
    private JPanel contentPane;

    private final ButtonGroup btnGroupRaceLocation = new ButtonGroup();
    private final ButtonGroup btnGroupRaceDateWeek = new ButtonGroup();

    // メニュー
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("ファイル");
    private JMenuItem menuItemFileExit = new JMenuItem("終了");
    private JMenu menuKino = new JMenu("機能");
    private JMenuItem menuItemKinoJRA = new JMenuItem("レースヘルプ(JRA)");
    private JMenu menuHelp = new JMenu("ヘルプ");;
    private JMenuItem menuItemHelpInfo = new JMenuItem("情報");
    private final JLabel labelStr03 = new JLabel("地面");
    private final JLabel labelStr05 = new JLabel("コース");
    private final JLabel labelStr06 = new JLabel("天気");
    private final JLabel labelStr07 = new JLabel("レース名");
    private final JRadioButton radioButton_tokyo = new JRadioButton("東京(新潟)");
    private final JRadioButton radioButton_kyoto = new JRadioButton("京都(小倉)");
    private final JRadioButton radioButton_nigata = new JRadioButton("新潟(札幌)");
    private final JRadioButton raceDateWeek_sat = new JRadioButton("土曜");
    private final JRadioButton raceDateWeek_sun = new JRadioButton("日曜");
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

    /**
     * その他部品
    */
    private final Refresh refresh = new Refresh();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

                    MainPageSmall frame = new MainPageSmall();
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
    public MainPageSmall() {

        logger.info("start constractor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1052, 681);

        setJMenuBar(menuBar);
        menuBar.add(menuFile);

        menuFile.add(menuItemFileExit);
        menuBar.add(menuKino);
        menuKino.add(menuItemKinoJRA);
        menuBar.add(menuHelp);
        menuHelp.add(menuItemHelpInfo);

        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        group_place.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u958B\u50AC\u5730", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_place.setBounds(228, 20, 93, 94);

        contentPane.add(group_place);
        group_place.setLayout(new GridLayout(0, 1, 0, 0));
        group_place.add(radioButton_tokyo);
        radioButton_tokyo.setSelected(true);
        radioButton_tokyo.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        radioButton_tokyo.setActionCommand("1");

        btnGroupRaceLocation.add(radioButton_tokyo);
        group_place.add(radioButton_kyoto);
        radioButton_kyoto.setSelected(true);
        radioButton_kyoto.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        radioButton_kyoto.setActionCommand("1");
        btnGroupRaceLocation.add(radioButton_kyoto);
        group_place.add(radioButton_nigata);
        radioButton_nigata.setSelected(true);
        radioButton_nigata.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        radioButton_nigata.setActionCommand("1");
        btnGroupRaceLocation.add(radioButton_nigata);
        group_days.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u66DC\u65E5", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        group_days.setBounds(333, 20, 61, 68);

        contentPane.add(group_days);
        group_days.setLayout(null);

        btnGroupRaceDateWeek.add(raceDateWeek_sat);
        raceDateWeek_sat.setBounds(6, 15, 49, 21);
        group_days.add(raceDateWeek_sat);
        raceDateWeek_sat.setSelected(true);
        raceDateWeek_sat.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        raceDateWeek_sat.setActionCommand("1");
        btnGroupRaceDateWeek.add(raceDateWeek_sun);
        raceDateWeek_sun.setBounds(6, 41, 49, 21);
        group_days.add(raceDateWeek_sun);
        raceDateWeek_sun.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        raceDateWeek_sun.setActionCommand("1");

        panel_1.setBounds(230, 153, 292, 21);
        contentPane.add(panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 107, 81, 81, 0 };
        gbl_panel_1.rowHeights = new int[] { 21, 0 };
        gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);
        GridBagConstraints gbc_button_getRaceInfo = new GridBagConstraints();
        gbc_button_getRaceInfo.anchor = GridBagConstraints.NORTHWEST;
        gbc_button_getRaceInfo.insets = new Insets(0, 0, 0, 5);
        gbc_button_getRaceInfo.gridx = 0;
        gbc_button_getRaceInfo.gridy = 0;
        panel_1.add(button_getRaceInfo, gbc_button_getRaceInfo);
        GridBagConstraints gbc_button_calc = new GridBagConstraints();
        gbc_button_calc.anchor = GridBagConstraints.NORTHWEST;
        gbc_button_calc.insets = new Insets(0, 0, 0, 5);
        gbc_button_calc.gridx = 1;
        gbc_button_calc.gridy = 0;
        panel_1.add(button_calc, gbc_button_calc);

        GridBagConstraints gbc_button_refresh = new GridBagConstraints();
        gbc_button_refresh.anchor = GridBagConstraints.NORTH;
        gbc_button_refresh.fill = GridBagConstraints.HORIZONTAL;
        gbc_button_refresh.gridx = 2;
        gbc_button_refresh.gridy = 0;
        panel_1.add(button_refresh, gbc_button_refresh);
        labelStr01_1.setBounds(25, 199, 93, 13);
        labelStr01_1.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        contentPane.add(labelStr01_1);
        scrollPaneHorse.setBounds(17, 222, 406, 355);
        contentPane.add(scrollPaneHorse);
        table_horse.setModel(new DefaultTableModel(new Object[][] { { "1", null, null, null, null }, { "2", null, null, null, null }, { "3", null, null, null, null }, { "4", null, null, null, null }, { "5", null, null, null, null }, { "6", null, null, null, null }, { "7", null, null, null, null }, { "8", null, null, null, null }, { "9", null, null, null, null }, { "10", null, null, null, null }, { "11", null, null, null, null }, { "12", null, null, null, null }, { "13", null, null, null, null }, { "14", null, null, null, null }, { "15", null, null, null, null }, { "16", null, null, null, null }, { "17", null, null, null, null }, { "18", null, null, null, null }, { "19", null, null, null, null }, { "20", null, null, null, null }, }, new String[] { "No", "weight", "dhweight", "dsl", "odds" }) {
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
        table_calc.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, }, new String[] { "\u56DE\u53CE\u7387(avg)", "\u52DD\u7387(%)", "\u7DCF\u6570", "\u8CFC\u5165\u6570", "\u30DA\u30A4\u30AA\u30D5" }) {
            boolean[] columnEditables = new boolean[] { false, false, false, false, false };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        table_calc.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));

        scrollPaneHorse_1.setViewportView(table_calc);
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "\u30EC\u30FC\u30B9\u60C5\u5831", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(16, 20, 186, 128);

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

        setTitle("NkViewer");

        listenerHandle();
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

                new MessageBuilder().addLine("Netkeiba Viewer").addLine("version: 4.0").addLine("copy right: kawabata 2021").showMessage(contentPane, "情報", JOptionPane.INFORMATION_MESSAGE);
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

                refresh();

                try (Scraiper scraiper = new Scraiper();) {

                } catch (Exception e1) {
                    logger.error("スクレイピング処理に失敗", e1);
                }
            }
        });

    }

    /**
     * システム終了処理
     */
    public void exit() {
        ScraipingRaceFromJRA.close();
        System.exit(0);
    }

    private void refresh() {

        refresh.refreshTableHasNo(table_horse);
        refresh.refreshTableNotContainNo(table_calc);
        label_course.setText("");
        label_surface.setText("");
        label_raceName.setText("");
        label_distance.setText("");
        label_weather.setText("");

        radioButton_tokyo.setSelected(true);
        raceDateWeek_sat.setSelected(true);
    }
}
