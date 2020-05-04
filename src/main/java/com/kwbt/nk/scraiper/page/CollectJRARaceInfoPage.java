package com.kwbt.nk.scraiper.page;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.StringUtils;

import com.kwbt.nk.scraiper.constant.ScraiperConst;
import com.kwbt.nk.scraiper.constant.ScraiperConst.Closeby;
import com.kwbt.nk.scraiper.model.CollectRaceFromJRAInputModel;
import com.kwbt.nk.viewer.util.MessageBuilder;

/**
 * JRAからスクレイピングする情報を入力する画面
 */
public class CollectJRARaceInfoPage extends JDialog {

    private final CollectRaceFromJRAInputModel model = new CollectRaceFromJRAInputModel();

    private final JPanel buttonPane = new JPanel();

    private final JLabel labelInformation = new JLabel(
            "<html>JRAのサイトからスクレイピングを行います。<br>"
                    + "以下のチェックボックスから、取得したい情報を選択してからOKボタンを押してください。<br>"
                    + "※レース2日前とかは、馬体重など欲しいデータがそもそもwebにないためエラーとなります。");

    private final JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);

    private final JLabel labelRaceField = new JLabel("開催場所");
    private final ButtonGroup btnGroupRaceLocation = new ButtonGroup();
    private final JRadioButton radioButton_tokyo = new JRadioButton("東京(新潟)");
    private final JRadioButton radioButton_kyoto = new JRadioButton("京都(小倉)");
    private final JRadioButton radioButton_nigata = new JRadioButton("新潟(札幌)");

    private final JLabel labelRaceWeek = new JLabel("開催曜日");
    private final ButtonGroup btnGroupRaceDateWeek = new ButtonGroup();
    private final JRadioButton raceDateWeek_sat = new JRadioButton("土曜");
    private final JRadioButton raceDateWeek_sun = new JRadioButton("日曜");

    private final JLabel labelRaceRound = new JLabel("レースラウンド");
    private final JFormattedTextField inputAreaRaceRound = new JFormattedTextField(new NumberFormatter());

    {
        btnGroupRaceLocation.add(radioButton_kyoto);
        btnGroupRaceLocation.add(radioButton_tokyo);
        btnGroupRaceLocation.add(radioButton_nigata);
        btnGroupRaceDateWeek.add(raceDateWeek_sat);
        btnGroupRaceDateWeek.add(raceDateWeek_sun);
    }

    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");

    /**
     * Create the dialog.
     */
    public CollectJRARaceInfoPage() {

        setBounds(100, 100, 511, 272);
        getContentPane().setLayout(null);
        setTitle("レースデータをスクレイピング");

        buttonPane.setBounds(54, 192, 439, 31);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane);

        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        labelInformation.setBounds(12, 10, 471, 38);

        getContentPane().add(labelInformation);
        separator.setBounds(12, 58, 481, 1);

        getContentPane().add(separator);
        labelRaceField.setBounds(22, 69, 75, 13);

        getContentPane().add(labelRaceField);
        radioButton_tokyo.setSelected(true);
        radioButton_tokyo.setBounds(32, 88, 102, 21);
        radioButton_tokyo.setActionCommand(String.valueOf(ScraiperConst.tokyo_num));

        getContentPane().add(radioButton_tokyo);
        radioButton_kyoto.setBounds(32, 111, 102, 21);
        radioButton_kyoto.setActionCommand(String.valueOf(ScraiperConst.kyoto_num));

        getContentPane().add(radioButton_kyoto);
        radioButton_nigata.setBounds(32, 134, 102, 21);
        radioButton_nigata.setActionCommand(String.valueOf(ScraiperConst.nigata_num));

        getContentPane().add(radioButton_nigata);
        labelRaceRound.setBounds(252, 69, 75, 13);

        getContentPane().add(labelRaceRound);

        inputAreaRaceRound.setBounds(262, 87, 75, 22);
        inputAreaRaceRound.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        getContentPane().add(inputAreaRaceRound);
        labelRaceWeek.setBounds(153, 69, 75, 13);

        getContentPane().add(labelRaceWeek);
        raceDateWeek_sat.setSelected(true);
        raceDateWeek_sat.setActionCommand("1");
        raceDateWeek_sat.setBounds(163, 88, 75, 21);
        raceDateWeek_sat.setActionCommand(String.valueOf(ScraiperConst.week_sat));

        getContentPane().add(raceDateWeek_sat);
        raceDateWeek_sun.setActionCommand("1");
        raceDateWeek_sun.setBounds(163, 111, 75, 21);
        raceDateWeek_sun.setActionCommand(String.valueOf(ScraiperConst.week_sun));

        getContentPane().add(raceDateWeek_sun);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        listenerHandle();

        this.getRootPane().setDefaultButton(okButton);
    }

    /**
     * 画面の各操作に対するアクションを登録
     */
    private void listenerHandle() {

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execOK();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setCloseAction(Closeby.cancel);
                dispose();
            }
        });

        inputAreaRaceRound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execOK();
            }
        });

    }

    /**
     * OKボタンやEnterキーが押された場合、入力値をモデルに詰めて前画面に戻る
     */
    private void execOK() {
        if (validator()) {
            model.setRaceLocation(Integer.valueOf(btnGroupRaceLocation.getSelection().getActionCommand()));
            model.setRaceRound(Integer.valueOf(inputAreaRaceRound.getText()));
            model.setRaceWeek(Integer.valueOf(btnGroupRaceDateWeek.getSelection().getActionCommand()));
            model.setCloseAction(Closeby.ok);
            dispose();
        }
    }

    /**
     * 各入力欄の入力値チェックを実施<br>
     * ラジオボタン系はデフォルト選択状態を指定しているので、値のチェックは不要。
     *
     * @return OKならtrue、だめなら警告を出してfalse
     */
    private boolean validator() {

        MessageBuilder msgBuilder = new MessageBuilder();

        if (StringUtils.isBlank(inputAreaRaceRound.getText())) {
            msgBuilder.addLine("レース番号（ラウンド）が入力されていません。");
        }

        if (!StringUtils.isNumeric(inputAreaRaceRound.getText())) {
            msgBuilder.addLine("レース番号（ラウンド）は数字で入力してください。");
        }

        if (!msgBuilder.isEmpty()) {
            msgBuilder.showMessage(this, "入力チェック", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public CollectRaceFromJRAInputModel getModel() {
        return model;
    }

}
