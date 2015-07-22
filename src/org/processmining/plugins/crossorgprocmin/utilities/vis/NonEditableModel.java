package org.processmining.plugins.crossorgprocmin.utilities.vis;

import javax.swing.table.DefaultTableModel;

/**
 * Extension of table model for non-editable information
 * 
 * @author onuryilmaz
 *
 */
public class NonEditableModel extends DefaultTableModel {

	private static final long serialVersionUID = 7210148558921838527L;

	NonEditableModel(Object[][] data, String[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
