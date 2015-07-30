package org.processmining.plugins.crossorgprocmin.utilities.vis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMSplitPane;
import org.processmining.framework.util.ui.widgets.ProMTable;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.RefinedActivityUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentConditionUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentDependencyUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.DifferentMomentsUnit;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

import cern.colt.Arrays;

import com.fluxicon.slickerbox.components.IconVerticalTabbedPane;
import com.fluxicon.slickerbox.components.StackedCardsTabbedPane;
import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * Main panel for {@link OrganizationalLogs}
 * 
 * @author onuryilmaz
 *
 */
public class OrganizationalLogMainPanel extends JPanel {

	private static final long serialVersionUID = -975010703302834231L;

	double performanceDifferentThresholdValue = 0;
	double refinedActivityThresholdValue = 0.25;
	String organizationName = "";

	SlickerFactory factory;

	String fontWhite8 = "<font color='white' face='helvetica,arial,sans-serif' size='8'>";
	String fontWhite6 = "<font color='white' face='helvetica,arial,sans-serif' size='6'>";
	String fontWhite4 = "<font color='white' face='helvetica,arial,sans-serif' size='4'>";
	String fontBlack8 = "<font color='black' face='helvetica,arial,sans-serif' size='8'>";
	String fontBlack6 = "<font color='black' face='helvetica,arial,sans-serif' size='6'>";
	String fontBlack4 = "<font color='black' face='helvetica,arial,sans-serif' size='4'>";

	ImageIcon gearsIcon = new ImageIcon(OrganizationalLogMainPanel.class.getClassLoader().getResource("gears-64.png"));

	JLabel performanceThresholdValueLabel;
	JComboBox<String> organizationCombo;

	ProMSplitPane splitPanel;
	StackedCardsTabbedPane tabbed;

	/**
	 * Create the panel.
	 * 
	 * @return
	 */
	public JComponent create(OrganizationalLogs logs) {
		factory = SlickerFactory.instance();

		splitPanel = new ProMSplitPane(ProMSplitPane.VERTICAL_SPLIT);
		splitPanel.setBackground((Color.white));
		splitPanel.setBorder(BorderFactory.createEmptyBorder());
		splitPanel.setDividerLocation(0.5);
		ProMSplitPane headerPanel = createHeaderPanel(logs, factory);
		splitPanel.setTopComponent(headerPanel);

		return splitPanel;
	}

	/**
	 * Steps to handle when "Analyze" button is clicked
	 * 
	 * @param logs
	 */
	private void analyzeClicked(OrganizationalLogs logs) {
		IconVerticalTabbedPane tabbed = new IconVerticalTabbedPane(Color.GRAY, Color.BLACK, 85);

		int firstOrg = logs.organizationNames.indexOf(organizationName);

		if (logs != null) {
			for (String s : logs.organizationNames) {
				if (!s.equals("Select")) {
					int secondOrg = logs.organizationNames.indexOf(s);
					JPanel panel = new JPanel(new GridBagLayout());
					panel.setBackground(new Color(40, 40, 40));

					if (!s.equals(organizationName)) {

						GridBagConstraints c = new GridBagConstraints();

						JTextPane headerPanel = new JTextPane();
						headerPanel.setContentType("text/html");
						headerPanel
								.setText("<center><font color='white' face='helvetica,arial,sans-serif' size='8'>Mismatch Patterns</font><font color='white' face='helvetica,arial,sans-serif' size='4'> <br>"
										+ organizationName + " (Selected) vs " + s + " (Other)");
						headerPanel.setEditable(false);
						headerPanel.setBackground(null);
						c.gridx = 0;
						c.gridy = 0;
						panel.add(headerPanel, c);

						ProMSplitPane splitPanelSkippedActivity = skippedActivityPanelCreator(logs, firstOrg, secondOrg);
						c.gridy = 1;
						if (splitPanelSkippedActivity != null)
							panel.add(splitPanelSkippedActivity, c);

						ProMSplitPane splitPanelRefinedActivity = refinedActivityPanelCreator(logs, firstOrg, secondOrg);
						c.gridy = 2;
						if (splitPanelRefinedActivity != null)
							panel.add(splitPanelRefinedActivity, c);

						ProMSplitPane splitPanelDifferentMoments = differentMomentsPanelCreator(logs, firstOrg,
								secondOrg);
						c.gridy = 3;
						if (splitPanelDifferentMoments != null)
							panel.add(splitPanelDifferentMoments, c);

						ProMSplitPane splitPanelDifferentMoments2 = differentConditionsPanelCreator(logs, firstOrg,
								secondOrg);
						c.gridy = 4;
						if (splitPanelDifferentMoments2 != null)
							panel.add(splitPanelDifferentMoments2, c);

						ProMSplitPane splitPanelDifferentMoments3 = differentDependencyPanelCreator(logs, firstOrg,
								secondOrg);
						c.gridy = 5;
						if (splitPanelDifferentMoments3 != null)
							panel.add(splitPanelDifferentMoments3, c);

						ProMSplitPane splitPanelDifferentMoments4 = additionalDependencyPanelCreator(logs, firstOrg,
								secondOrg);
						c.gridy = 6;
						if (splitPanelDifferentMoments4 != null)
							panel.add(splitPanelDifferentMoments4, c);

					} else {

						GridBagConstraints c = new GridBagConstraints();

						JTextPane headerPanel = new JTextPane();
						headerPanel.setContentType("text/html");
						headerPanel
								.setText("<center><font color='white' face='helvetica,arial,sans-serif' size='8'>Mismatch Patterns</font><font color='white' face='helvetica,arial,sans-serif' size='4'> <br>"
										+ organizationName);
						headerPanel.setEditable(false);
						headerPanel.setBackground(null);
						c.gridx = 0;
						c.gridy = 0;
						panel.add(headerPanel, c);

						ProMSplitPane splitPanelSkippedActivity = skippedActivityPanelCreator(logs, firstOrg, secondOrg);
						c.gridy = 1;
						panel.add(splitPanelSkippedActivity, c);

					}
					// tabbed.addTab(s, panel);
					ProMScrollPane jScrollPane = new ProMScrollPane(panel);
					jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
					jScrollPane.setBackground(Color.black);
					String label = "Org #";
					if (firstOrg == secondOrg) {
						label = label + firstOrg;
					} else {
						label = label + secondOrg;
					}

					tabbed.addTab(label, gearsIcon.getImage(), jScrollPane);

				}
			}
		}
		splitPanel.setBottomComponent(tabbed);

	}

	/**
	 * Create header panel
	 * 
	 * @param logs
	 * @param factory
	 * @return
	 */
	private ProMSplitPane createHeaderPanel(final OrganizationalLogs logs, SlickerFactory factory) {
		ProMSplitPane headerPanel = new ProMSplitPane(ProMSplitPane.HORIZONTAL_SPLIT);

		{
			JTextPane headerExplanation = new JTextPane();
			headerExplanation.setContentType("text/html");
			headerExplanation
					.setText(fontBlack6
							+ "Mismatch Pattern Analysis without Performance Clustering</font><hr>"
							+ fontBlack4
							+ " Select an organization from left and click \"Analyze\" to list mismatch patterns compared to other organizations.");
			headerExplanation.setEditable(false);
			headerExplanation.setBackground(null);
			headerPanel.setRightComponent(headerExplanation);
		}

		{
			JPanel headerLeftPanel = new JPanel();
			headerLeftPanel.setBackground(null);
			headerLeftPanel.setLayout(new GridBagLayout());
			{
				List<String> organizationList = new ArrayList<String>();
				if (logs != null) {

					organizationList.addAll(logs.organizationNames);

				}
				organizationCombo = factory.createComboBox(organizationList.toArray());

				GridBagConstraints organizationComboConstraint = new GridBagConstraints();
				organizationComboConstraint.gridx = 0;
				organizationComboConstraint.gridy = 1;

				organizationComboConstraint.anchor = GridBagConstraints.WEST;

				organizationCombo.addActionListener((new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						organizationName = (String) organizationCombo.getSelectedItem();
					}
				}));
				headerLeftPanel.add(organizationCombo, organizationComboConstraint);
			}
			{
				JLabel organizationComboLabel = factory.createLabel("Organization");
				GridBagConstraints organizationComboLabelConstraint = new GridBagConstraints();
				organizationComboLabelConstraint.gridx = 0;
				organizationComboLabelConstraint.gridy = 0;
				organizationComboLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
				headerLeftPanel.add(organizationComboLabel, organizationComboLabelConstraint);
			}

			{
				JButton analyzeButton = factory.createButton("Analyze");
				GridBagConstraints analyzeButtonConstraint = new GridBagConstraints();
				analyzeButtonConstraint.gridx = 0;
				analyzeButtonConstraint.gridy = 3;
				analyzeButtonConstraint.gridwidth = 3;
				analyzeButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
				analyzeButtonConstraint.anchor = GridBagConstraints.WEST;
				analyzeButton.addActionListener((new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						organizationName = (String) organizationCombo.getSelectedItem();
						if (!(organizationName.equals("Select") || organizationName.equals(""))) {
							System.out.println(organizationName + " " + performanceDifferentThresholdValue);
							analyzeClicked(logs);
						}
					}
				}));
				headerLeftPanel.add(analyzeButton, analyzeButtonConstraint);
			}
			headerPanel.setLeftComponent(headerLeftPanel);
		}
		return headerPanel;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane skippedActivityPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelSkippedActivity = new ProMSplitPane();
		splitPanelSkippedActivity.setBackground(null);

		ProMTable comp = null;
		if (firstOrg == secondOrg) {
			comp = skippedActivityTableCreator(logs, firstOrg);
		} else {
			comp = skippedActivityTableCreator(logs, secondOrg);
		}
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		if (firstOrg == secondOrg) {
			textField
					.setText(fontBlack6
							+ "Skipped Activities ("
							+ comp.getTable().getRowCount()
							+ ") </font><hr>"
							+ fontBlack4
							+ " An activity exists in one process but no equivalent activity is found in the other process. In the list you can see the skipped activities in "
							+ logs.organizationNames.get(firstOrg) + ".");
		} else {
			textField
					.setText(fontBlack6
							+ "Skipped Activities ("
							+ comp.getTable().getRowCount()
							+ ")</font><hr>"
							+ fontBlack4
							+ " An activity exists in one process but no equivalent activity is found in the other process. In the list you can see the skipped activities in "
							+ logs.organizationNames.get(secondOrg) + " compared to "
							+ logs.organizationNames.get(firstOrg) + ".");
		}
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelSkippedActivity.setLeftComponent(comp);
		splitPanelSkippedActivity.setRightComponent(scrollPane);
		return splitPanelSkippedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param i
	 * @return
	 */
	private ProMTable skippedActivityTableCreator(OrganizationalLogs logs, int i) {
		List<String> skippedActivities = logs.mismatchPatterns.skippedActivityComplete.get(i);

		if (skippedActivities.size() == 0)
			return null;

		String[][] data = new String[skippedActivities.size()][1];
		int j = 0;
		for (String skipped : skippedActivities) {
			data[j][0] = skipped;
			j++;
		}

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Skipped Activities" });
		ProMTable table = new ProMTable(tbModel);
		table.setEnabled(false);
		return table;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane refinedActivityPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);

		ProMTable comp = refinedActivityTableCreator(logs, firstOrg, secondOrg);
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Refined Activities ("
						+ comp.getTable().getRowCount()
						+ ")</font><hr>"
						+ fontBlack4
						+ "An activity exists in one process but, as an equivalent, a collection of activitiesare existing in the other process to achieve the same task. In the list you can see the refined activity in "
						+ logs.organizationNames.get(firstOrg) + " and a potential list of activities in "
						+ logs.organizationNames.get(secondOrg) + ".");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMTable refinedActivityTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		List<RefinedActivityUnit> refinedActivities = logs.mismatchPatterns.getRefinedActivityUnitsComplete(firstOrg,
				secondOrg);

		Map<String, String> dataMap = new HashMap<String, String>();

		int k = 0;
		for (RefinedActivityUnit ru : refinedActivities) {

			String values = "";
			Map<Double, String> reverseOrderedMap = new TreeMap<Double, String>(Collections.reverseOrder());
			reverseOrderedMap.putAll(ru.activityNames_2);
			for (Entry<Double, String> e : reverseOrderedMap.entrySet()) {

				Double value = e.getKey();
				if (value > refinedActivityThresholdValue) {
					float total = 1;
					float percent = (float) ((100 * value) / total);
					values = values + " " + e.getValue() + " (" + String.format("%.0f%%", percent) + "); ";
				}
			}

			if (!values.isEmpty()) {
				values = values.substring(0, values.length() - 2);
				dataMap.put(ru.activityName_1, values);

			}
		}
		if (dataMap.size() == 0)
			return null;

		String[][] data = new String[dataMap.size()][2];
		int count = 0;
		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			data[count][0] = entry.getKey();
			data[count][1] = entry.getValue();
			count++;
		}

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Refined Activity",
				"Potential Activities" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane differentMomentsPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);

		ProMTable comp = differentMomentsTableCreator(logs, firstOrg, secondOrg);
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Moments in Processes ("
						+ comp.getTable().getRowCount()
						+ ")</font><hr>"
						+ fontBlack4
						+ "Set of activities are undertaken with different orders in different processes. In the list you can see different previous or next acitivities in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMTable differentMomentsTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {

		List<DifferentMomentsUnit> dmus = logs.mismatchPatterns.getDifferentMoments(firstOrg, secondOrg);

		if (dmus.size() == 0)
			return null;

		String[][] data = new String[dmus.size()][5];

		int i = 0;
		for (DifferentMomentsUnit dmu : dmus) {
			data[i][0] = dmu.previous_1;
			data[i][1] = dmu.previous_2;
			data[i][2] = dmu.activityName;
			data[i][3] = dmu.next_1;
			data[i][4] = dmu.next_2;
			i++;
		}

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Previous (Selected)",
				"Previous (Other)", "Activity", "Next (Selected)", "Next (Other)" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane differentConditionsPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);

		ProMTable comp = differentConditionsTableCreator(logs, firstOrg, secondOrg);
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Conditions for Occurrence ("
						+ comp.getTable().getRowCount()
						+ ")</font><hr>"
						+ fontBlack4
						+ "Set of dependencies are same for two processes; however, occurrence condition is different in organizations. In the list you can see different occurrence conditions in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMTable differentConditionsTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {

		List<DifferentConditionUnit> dcus = logs.mismatchPatterns.getDifferentConditions(firstOrg, secondOrg);

		if (dcus.size() == 0)
			return null;
		String[][] data = new String[dcus.size()][3];

		int i = 0;
		for (DifferentConditionUnit dcu : dcus) {
			data[i][0] = dcu.dependencyUnit_1.activityName;
			data[i][1] = dcu.dependencyUnit_1.gateway.toString();
			data[i][2] = dcu.dependencyUnit_2.gateway.toString();
			i++;
		}

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Activity", "Gateway (Selected)",
				"Gateway (Other)" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane differentDependencyPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);

		ProMTable comp = differentDependencyTableCreator(logs, firstOrg, secondOrg);
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Dependencies ("
						+ comp.getTable().getRowCount()
						+ ")</font><hr>"
						+ fontBlack4
						+ "Dependency set of activities differ in different organizations. In the list you can see different dependency sets in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMTable differentDependencyTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {

		List<DifferentDependencyUnit> ddus = logs.mismatchPatterns.getDifferentDependencies(firstOrg, secondOrg);
		if (ddus.size() == 0)
			return null;

		String[][] data = new String[ddus.size()][3];

		int i = 0;
		for (DifferentDependencyUnit ddu : ddus) {
			data[i][0] = ddu.dependencyUnit_1.activityName;
			data[i][1] = Arrays.toString(ddu.dependencyUnit_1.previousActivities.toArray());
			data[i][2] = Arrays.toString(ddu.dependencyUnit_2.previousActivities.toArray());
			i++;
		}

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Activity", "Dependency Set (Selected)",
				"Dependency Set (Other)" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMSplitPane additionalDependencyPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);

		ProMTable comp = additionalDependencyTableCreator(logs, firstOrg, secondOrg);
		if (comp == null)
			return null;

		JTextPane textField = new JTextPane();
		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Additional Dependencies ("
						+ comp.getTable().getRowCount()
						+ ")</font><hr>"
						+ fontBlack4
						+ " This pattern is a special case of different dependencies where one set of activities includes the other and results with additional dependencies. In the list you can see additional dependency sets in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @return
	 */
	private ProMTable additionalDependencyTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg) {

		List<DifferentDependencyUnit> ddus = logs.mismatchPatterns.getDifferentDependencies(firstOrg, secondOrg);

		String[][] data = new String[ddus.size()][3];

		int i = 0;
		for (DifferentDependencyUnit ddu : ddus) {
			if (ddu.additionalDependencyFlag) {
				data[i][0] = ddu.dependencyUnit_1.activityName;
				data[i][1] = Arrays.toString(ddu.dependencyUnit_1.previousActivities.toArray());
				data[i][2] = Arrays.toString(ddu.dependencyUnit_2.previousActivities.toArray());
				i++;
			}
		}
		if (data.length == 0)
			return null;

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Activity", "Dependency Set (Selected)",
				"Dependency Set (Other)" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}
}
