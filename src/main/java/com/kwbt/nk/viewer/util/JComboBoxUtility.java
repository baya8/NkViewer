package com.kwbt.nk.viewer.util;

import javax.swing.JComboBox;

public class JComboBoxUtility {

	/**
	 * コンボボックスが選択状態化を判定する
	 *
	 * @param box
	 * @return
	 */
	public <T> boolean isNoneSelected(JComboBox<T> box) {
		return box.getSelectedIndex() < 0;
	}

}
