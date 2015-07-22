package org.processmining.plugins.crossorgprocmin.utilities.vis;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMSplitPane;
import org.processmining.framework.util.ui.widgets.ProMTable;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.RefinedActivityClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.SkippedActivityUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentConditionClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentDependencyClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.DifferentMomentsClusterUnit;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import cern.colt.Arrays;

import com.fluxicon.slickerbox.components.IconVerticalTabbedPane;
import com.fluxicon.slickerbox.components.StackedCardsTabbedPane;
import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * Main panel for {@link OrganizationalLogs} with performance clustering
 * information
 * 
 * @author onuryilmaz
 *
 */
public class OrganizationalLogPerformanceMainPanel extends JPanel {

	public boolean testMode = false;

	private static final long serialVersionUID = -975010703302834231L;

	String fontWhite8 = "<font color='white' face='helvetica,arial,sans-serif' size='8'>";
	String fontWhite6 = "<font color='white' face='helvetica,arial,sans-serif' size='6'>";
	String fontWhite4 = "<font color='white' face='helvetica,arial,sans-serif' size='4'>";
	String fontBlack8 = "<font color='black' face='helvetica,arial,sans-serif' size='8'>";
	String fontBlack6 = "<font color='black' face='helvetica,arial,sans-serif' size='6'>";
	String fontBlack4 = "<font color='black' face='helvetica,arial,sans-serif' size='4'>";
	String fontBlack3 = "<font color='black' face='helvetica,arial,sans-serif' size='3'>";

	String organizationName = "";
	int selectedOrganizationID;
	int selectedOrganizationCluster;
	Map<Integer, Integer> clusterAssignments = new HashMap<Integer, Integer>();

	double performanceDifferentThresholdValue = 0;
	double refinedActivityThresholdValue = 0.05;

	ImageIcon gearsIcon32 = new ImageIcon(this.getClass().getResource(
			"/org/processmining/plugins/crossorg/img/gears-32.png"));
	ImageIcon gearsIcon64 = new ImageIcon(this.getClass().getResource(
			"/org/processmining/plugins/crossorg/img/gears-64.png"));
	ImageIcon dashboardIcon30 = new ImageIcon(this.getClass().getResource(
			"/org/processmining/plugins/crossorg/img/dashboard-30.png"));

	JComponent component = create(null);
	JLabel performanceThresholdValueLabel;
	StackedCardsTabbedPane tabbed;
	JComboBox<String> organizationCombo;
	ProMSplitPane splitPanel;
	SlickerFactory factory;

	/**
	 * Create the panel.
	 * 
	 * @return
	 */
	public JComponent create(OrganizationalLogs logs) {
		factory = SlickerFactory.instance();

		splitPanel = new ProMSplitPane(ProMSplitPane.VERTICAL_SPLIT);
		splitPanel.setBackground((Color.BLACK));
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
	 * @throws Exception
	 */
	private void analyzeClicked(OrganizationalLogs logs) throws Exception {

		IconVerticalTabbedPane tabbed = new IconVerticalTabbedPane(Color.GRAY, Color.BLACK, 45);

		// Get cluster vectors
		SimpleKMeans clusterData = logs.clusterings.get(logs.selectedClusterSize);
		Instances centroids = clusterData.getClusterCentroids();

		// Get cluster assignments
		for (int ca = 0; ca < clusterData.getAssignments().length; ca++) {
			clusterAssignments.put(ca + 1, clusterData.getAssignments()[ca] + 1);
		}

		// Set selected organization info
		selectedOrganizationID = logs.organizationNames.indexOf(organizationName);
		selectedOrganizationCluster = clusterAssignments.get(selectedOrganizationID);

		// For each cluster vector, make analysis
		for (int j = 0; j < centroids.numAttributes(); j++) {

			// Get attribute text
			String attributeText = centroids.attribute(j).name();
			attributeText = attributeText.replace("(Avg)", ";Average Time");
			attributeText = attributeText.replace("(Std)", ";Standard Deviation Time");
			attributeText = attributeText.replace("->", ";");
			String[] startEnd = attributeText.split(";");
			double[] values = centroids.attributeToDoubleArray(j);

			// Create a new panel
			ProMSplitPane panel = new ProMSplitPane(ProMSplitPane.VERTICAL_SPLIT);
			panel.setBackground(new Color(40, 40, 40));

			// Create analysis header and return passed clusters
			List<Integer> clustersPassed = createAnalysisHeaderPanel(logs, startEnd, values, panel);

			// Create cluster panel
			IconVerticalTabbedPane clusters = new IconVerticalTabbedPane(Color.GRAY, Color.BLACK, 85);
			clusters.setBackground(new Color(40, 40, 40));

			// Add analysis for each other cluster
			for (Integer i : clustersPassed) {

				int firstOrg = selectedOrganizationCluster;
				int secondOrg = i;
				JPanel clusterPanel = new JPanel(new GridBagLayout());
				clusterPanel.setBackground(new Color(40, 40, 40));

				GridBagConstraints c = new GridBagConstraints();

				ProMSplitPane splitPanelSkippedActivity = skippedActivityPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 1;
				if (splitPanelSkippedActivity != null)
					clusterPanel.add(splitPanelSkippedActivity, c);

				ProMSplitPane splitPanelRefinedActivity = refinedActivityPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 2;
				if (splitPanelRefinedActivity != null)
					clusterPanel.add(splitPanelRefinedActivity, c);

				ProMSplitPane splitPanelDifferentMoments = differentMomentsPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 3;
				if (splitPanelDifferentMoments != null)
					clusterPanel.add(splitPanelDifferentMoments, c);

				ProMSplitPane splitPanelDifferentMoments2 = differentConditionsPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 4;
				if (splitPanelDifferentMoments2 != null)
					clusterPanel.add(splitPanelDifferentMoments2, c);

				ProMSplitPane splitPanelDifferentMoments3 = differentDependencyPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 5;
				if (splitPanelDifferentMoments3 != null)
					clusterPanel.add(splitPanelDifferentMoments3, c);

				ProMSplitPane splitPanelDifferentMoments4 = additionalDependencyPanelCreator(logs, firstOrg, secondOrg,
						startEnd[0], startEnd[1]);
				c.gridy = 6;
				if (splitPanelDifferentMoments4 != null)
					clusterPanel.add(splitPanelDifferentMoments4, c);

				ProMScrollPane jScrollPane = new ProMScrollPane(clusterPanel);
				jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
				jScrollPane.setBackground(Color.black);

				clusters.addTab("Cluster #" + i, gearsIcon64.getImage(), jScrollPane);
			}

			panel.setBottomComponent(clusters);

			ProMScrollPane jScrollPane = new ProMScrollPane(panel);
			jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
			jScrollPane.setBackground(Color.black);

			if (clustersPassed.size() > 0 || testMode)
				tabbed.addTab("", dashboardIcon30.getImage(), jScrollPane);
		}

		splitPanel.setBottomComponent(tabbed);

	}

	/**
	 * 
	 * @param logs
	 * @param startEnd
	 * @param values
	 * @param panel
	 * @return
	 */
	private List<Integer> createAnalysisHeaderPanel(OrganizationalLogs logs, String[] startEnd, double[] values,
			ProMSplitPane panel) {

		// Header panel
		ProMSplitPane headerPanel = new ProMSplitPane(ProMSplitPane.HORIZONTAL_SPLIT);
		headerPanel.setBackground(new Color(40, 40, 40));

		// Explanation text
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setText(fontBlack6
				+ startEnd[2]
				+ " between \""
				+ startEnd[0]
				+ "\" and \""
				+ startEnd[1]
				+ "\"</font><hr>"
				+ fontBlack3
				+ "Different percentages for this performance indicator is listed on the table right. Difference percentage indicates how well(-) or worse(+) the other clusters operate for this performance indicator. <br>Mismatch patterns for the clusters with higher difference than threshold are listed in the tabs below.");
		textPane.setEditable(false);
		textPane.setBackground(null);
		ProMScrollPane headerExplanationScrollPane = new ProMScrollPane(textPane);
		headerExplanationScrollPane.setBackground(Color.black);
		headerExplanationScrollPane.setPreferredSize(new Dimension(1000, 100));
		headerPanel.setLeftComponent(headerExplanationScrollPane);

		// Create data for table
		String[] columns = new String[logs.selectedClusterSize + 1];
		columns[0] = "";
		Object[][] data = new Object[2][columns.length];
		data[0][0] = "Difference Percentage";
		data[1][0] = "Organizations";
		for (int i = 1; i <= logs.selectedClusterSize; i++) {

			if (i == selectedOrganizationCluster) {
				columns[i] = "Cluster #" + i + " (Selected)";

			} else {
				columns[i] = "Cluster #" + i;

			}
			data[0][i] = values[i - 1];

		}

		// Check for clusters according to performance threshold
		List<Integer> clustersPassed = new ArrayList<Integer>();
		double selectedValue = (double) data[0][selectedOrganizationCluster];
		for (int i = 1; i <= logs.selectedClusterSize; i++) {
			if (((double) data[0][i] - selectedValue) / selectedValue <= -performanceDifferentThresholdValue
					|| testMode) {
				clustersPassed.add(i);
				data[0][i] = String.format("%.0f%%", ((double) data[0][i] - selectedValue) / selectedValue * 100) + "*";
			} else {
				data[0][i] = String.format("%.0f%%", ((double) data[0][i] - selectedValue) / selectedValue * 100);
			}
			if (i == selectedOrganizationCluster) {
				data[0][i] = "-";
			}

			data[1][i] = getOrganizationsByCluster(logs, i);
		}

		// Convert data table to string data
		String[][] tableData = new String[2][columns.length];
		for (int i = 0; i < columns.length; i++) {
			tableData[0][i] = (String) data[0][i];
			tableData[1][i] = (String) data[1][i];
		}

		// Create table
		DefaultTableModel tbModel = new NonEditableModel(tableData, columns);
		JTable table = new JTable(tbModel) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				String value = (String) getModel().getValueAt(row, col);
				if (value.contains("*")) {
					comp.setBackground(Color.GREEN);

				} else {
					comp.setBackground(Color.WHITE);
				}

				return comp;
			}
		};
		table.setEnabled(false);

		// Header panel to hold explanation and table
		ProMScrollPane headerTableScrollPane = new ProMScrollPane(table);
		headerTableScrollPane.setBackground(Color.black);
		headerTableScrollPane.setPreferredSize(new Dimension(150, 75));
		headerTableScrollPane.setAutoscrolls(false);
		headerPanel.setRightComponent(headerTableScrollPane);

		// Add header to top
		panel.setTopComponent(headerPanel);

		// Return list of clusters that passed threshold
		return clustersPassed;
	}

	/**
	 * 
	 * @param logs
	 * @param cluster
	 * @return
	 */
	private String getOrganizationsByCluster(OrganizationalLogs logs, int cluster) {

		String gc = "";
		for (Entry<Integer, Integer> e : clusterAssignments.entrySet()) {
			if (e.getValue().equals(cluster)) {
				gc = gc + logs.organizationNames.get(e.getKey()) + "; ";
			}
		}
		return gc.substring(0, gc.length() - 2);
	}

	/**
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
							+ "Mismatch Pattern Analysis with Performance Clustering</font><hr>"
							+ fontBlack4
							+ " Select an organization and performance difference threshold from left and click \"Analyze\" to list mismatch patterns compared to other organizations.");
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
				final JSlider jSlider = factory.createSlider(SwingConstants.HORIZONTAL);
				GridBagConstraints jSliderConstraint = new GridBagConstraints();
				jSlider.setMaximum(500);
				jSlider.setMinimum(0);
				jSliderConstraint.gridx = 2;
				jSliderConstraint.gridy = 1;
				jSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						performanceDifferentThresholdValue = (float) (jSlider.getValue());
						performanceDifferentThresholdValue = performanceDifferentThresholdValue / 100;
						performanceThresholdValueLabel.setText(String.format("Threshold (%.2f)",
								performanceDifferentThresholdValue));
					}
				});
				headerLeftPanel.add(jSlider, jSliderConstraint);
			}
			{
				performanceThresholdValueLabel = factory.createLabel(String.format("Threshold (%.2f)",
						performanceDifferentThresholdValue));
				GridBagConstraints thresholdValueConstraint = new GridBagConstraints();
				thresholdValueConstraint.gridx = 2;
				thresholdValueConstraint.gridy = 0;
				headerLeftPanel.add(performanceThresholdValueLabel, thresholdValueConstraint);
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
							try {
								analyzeClicked(logs);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane skippedActivityPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelSkippedActivity = new ProMSplitPane();
		splitPanelSkippedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		if (firstOrg == secondOrg) {
			textField
					.setText(fontBlack6
							+ "Skipped Activities </font><hr>"
							+ fontBlack4
							+ " An activity exists in one process but no equivalent activity is found in the other process. In the list you can see the skipped activities in "
							+ logs.organizationNames.get(firstOrg) + ".");
		} else {
			textField
					.setText(fontBlack6
							+ "Skipped Activities </font><hr>"
							+ fontBlack4
							+ " An activity exists in one process but no equivalent activity is found in the other process. In the list you can see the skipped activities in "
							+ logs.organizationNames.get(secondOrg) + " compared to "
							+ logs.organizationNames.get(firstOrg) + ".");
		}
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));
		ProMTable comp = null;
		if (firstOrg == secondOrg) {
			comp = skippedActivityTableCreator(logs, firstOrg, start, end);
		} else {
			comp = skippedActivityTableCreator(logs, secondOrg, start, end);
		}
		if (comp == null)
			return null;
		splitPanelSkippedActivity.setLeftComponent(comp);
		splitPanelSkippedActivity.setRightComponent(scrollPane);
		return splitPanelSkippedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param i
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMTable skippedActivityTableCreator(OrganizationalLogs logs, int i, String start, String end) {

		SkippedActivityUnit sau = logs.mismatchPatterns.getSkippedActivities(start, end, i);
		if (sau == null)
			return null;
		List<String> skippedActivities = sau.skippedActivities;

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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane refinedActivityPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Refined Activities </font><hr>"
						+ fontBlack4
						+ "An activity exists in one process but, as an equivalent, a collection of activitiesare existing in the other process to achieve the same task. In the list you can see the refined activity in "
						+ logs.organizationNames.get(firstOrg) + " and a potential list of activities in "
						+ logs.organizationNames.get(secondOrg) + ".");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		ProMTable comp = refinedActivityTableCreator(logs, firstOrg, secondOrg, start, end);
		if (comp == null)
			return null;
		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMTable refinedActivityTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg, String start,
			String end) {
		List<RefinedActivityClusterUnit> refinedActivities = logs.mismatchPatterns.getRefinedActivityUnitsClustered(
				firstOrg, secondOrg, start, end);

		Map<String, String> dataMap = new HashMap<String, String>();

		int k = 0;
		for (RefinedActivityClusterUnit ru : refinedActivities) {

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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane differentMomentsPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Moments in Processes</font><hr>"
						+ fontBlack4
						+ "Set of activities are undertaken with different orders in different processes. In the list you can see different previous or next acitivities in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		ProMTable comp = differentMomentsTableCreator(logs, firstOrg, secondOrg, start, end);
		if (comp == null)
			return null;
		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMTable differentMomentsTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg, String start,
			String end) {

		List<DifferentMomentsClusterUnit> dmus = logs.mismatchPatterns.getDifferentMomentsCluster(firstOrg, secondOrg,
				start, end);

		if (dmus.size() == 0)
			return null;

		String[][] data = new String[dmus.size()][5];

		int i = 0;
		for (DifferentMomentsClusterUnit dmu : dmus) {
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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane differentConditionsPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Conditions for Occurrence</font><hr>"
						+ fontBlack4
						+ "Set of dependencies are same for two processes; however, occurrence condition is different in organizations. In the list you can see different occurrence conditions in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		ProMTable comp = differentConditionsTableCreator(logs, firstOrg, secondOrg, start, end);
		if (comp == null)
			return null;
		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMTable differentConditionsTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {

		List<DifferentConditionClusterUnit> dcus = logs.mismatchPatterns.getDifferentConditionsCluster(firstOrg,
				secondOrg, start, end);

		if (dcus.size() == 0)
			return null;
		String[][] data = new String[dcus.size()][3];

		int i = 0;
		for (DifferentConditionClusterUnit dcu : dcus) {
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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane differentDependencyPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Different Dependencies</font><hr>"
						+ fontBlack4
						+ "Dependency set of activities differ in different organizations. In the list you can see different dependency sets in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		ProMTable comp = differentDependencyTableCreator(logs, firstOrg, secondOrg, start, end);
		if (comp == null)
			return null;
		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(scrollPane);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMTable differentDependencyTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {

		List<DifferentDependencyClusterUnit> ddus = logs.mismatchPatterns.getDifferentDependenciesCluster(firstOrg,
				secondOrg, start, end);
		if (ddus.size() == 0)
			return null;

		String[][] data = new String[ddus.size()][3];

		int i = 0;
		for (DifferentDependencyClusterUnit ddu : ddus) {
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
	 * @param start
	 * @param end
	 * @return
	 */
	private ProMSplitPane additionalDependencyPanelCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {
		ProMSplitPane splitPanelRefinedActivity = new ProMSplitPane();
		splitPanelRefinedActivity.setBackground(null);
		JTextPane textField = new JTextPane();

		textField.setContentType("text/html");
		textField
				.setText(fontBlack6
						+ "Additional Dependencies</font><hr>"
						+ fontBlack4
						+ " This pattern is a special case of different dependencies where one set of activities includes the other and results with additional dependencies. In the list you can see additional dependency sets in two organizations as "
						+ logs.organizationNames.get(firstOrg) + "(Selected) and "
						+ logs.organizationNames.get(secondOrg) + "(Other).");
		textField.setEditable(false);
		textField.setBackground(new Color(192, 192, 192));
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(500, 0));

		ProMTable comp = additionalDependencyTableCreator(logs, firstOrg, secondOrg, start, end);
		if (comp == null)
			return null;
		splitPanelRefinedActivity.setLeftComponent(comp);
		splitPanelRefinedActivity.setRightComponent(textField);
		return splitPanelRefinedActivity;
	}

	/**
	 * 
	 * @param logs
	 * @param firstOrg
	 * @param secondOrg
	 * @param start
	 * @param end
	 * @return
	 */
	private static ProMTable additionalDependencyTableCreator(OrganizationalLogs logs, int firstOrg, int secondOrg,
			String start, String end) {

		List<DifferentDependencyClusterUnit> ddus = logs.mismatchPatterns.getDifferentDependenciesCluster(firstOrg,
				secondOrg, start, end);

		String[][] data = new String[ddus.size()][3];

		int i = 0;
		for (DifferentDependencyClusterUnit ddu : ddus) {
			if (ddu.additionalDependencyFlag) {
				data[i][0] = ddu.dependencyUnit_1.activityName;
				data[i][1] = Arrays.toString(ddu.dependencyUnit_1.previousActivities.toArray());
				data[i][2] = Arrays.toString(ddu.dependencyUnit_2.previousActivities.toArray());
				i++;
			}
		}
		if (data.length == 0 || data[0][0] == null)
			return null;

		DefaultTableModel tbModel = new NonEditableModel(data, new String[] { "Activity", "Dependency Set (Selected)",
				"Dependency Set (Other)" });
		ProMTable table = new ProMTable(tbModel);
		return table;
	}
}
