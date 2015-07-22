package org.processmining.plugins.crossorgprocmin.cluster;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMHeaderPanel;
import org.processmining.framework.util.ui.widgets.ProMTable;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.framework.util.ui.widgets.WidgetColors;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

import weka.clusterers.SimpleKMeans;

import com.fluxicon.slickerbox.components.SlickerTabbedPane;
import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * Dialog for selecting number of clusters based on within-SSE plot
 * 
 * @author onuryilmaz
 *
 */
public class KMeansPerformanceClustererDialog extends JPanel {

	private static final long serialVersionUID = -4352495747802798069L;

	private int selectedNumberOfCluster = 2;

	/**
	 * Create panel in constructor
	 * 
	 * @param orgLogs
	 */
	public KMeansPerformanceClustererDialog(OrganizationalLogs orgLogs) {

		HashMap<Integer, SimpleKMeans> map = (HashMap<Integer, SimpleKMeans>) orgLogs.clusterings;
		Double[][] arr = new Double[map.size()][2];
		Set entries = map.entrySet();
		Iterator entriesIterator = entries.iterator();

		int i = 0;
		while (entriesIterator.hasNext()) {

			Entry<Integer, SimpleKMeans> mapping = (Entry<Integer, SimpleKMeans>) entriesIterator.next();

			arr[i][0] = new Double(mapping.getKey());
			arr[i][1] = mapping.getValue().getSquaredError();

			i++;
		}

		setLayout(new GridLayout(1, 0, 0, 0));

		{

			SlickerTabbedPane tabbedPane = SlickerFactory.instance().createTabbedPane("", WidgetColors.COLOR_LIST_BG,
					WidgetColors.COLOR_LIST_FG, Color.GREEN);

			add(tabbedPane);

			{
				ProMHeaderPanel panel_1 = new ProMHeaderPanel("");
				final XYDataset dataset = createDataset(arr);
				final JFreeChart chart = createChart(dataset);

				final ChartPanel chartPanel = new ChartPanel(chart);
				panel_1.add(chartPanel);

				ProMTextField txtpnInfoOnSelecting = new ProMTextField();
				txtpnInfoOnSelecting
						.setText("Please select number of clusters (k) based on the SSE information provided above:");
				txtpnInfoOnSelecting.setEditable(false);

				Integer[] comboItems = new Integer[orgLogs.logs.size()];

				for (int i1 = 0; i1 < orgLogs.logs.size(); i1++) {
					comboItems[i1] = i1 + 1;
				}
				final ProMComboBox<Integer> comboBox = new ProMComboBox<Integer>(comboItems);
				comboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Integer selected = (Integer) comboBox.getSelectedItem();
						setSelectedNumberOfCluster(selected);
					}
				});

				panel_1.add(txtpnInfoOnSelecting);
				panel_1.add(comboBox);
				tabbedPane.addTab("Chart Data", panel_1);
			}

			{

				ProMHeaderPanel scrollPane = new ProMHeaderPanel("");

				tabbedPane.addTab("Table Data", scrollPane);

				DefaultTableModel tbModel = new DefaultTableModel(arr, new String[] { "Number of Clusters",
						"Sum of Squared Error (SSE)" });
				ProMTable table = new ProMTable(tbModel);
				scrollPane.add(table);
			}

		}

	}

	/**
	 * Get selected number of cluster
	 * 
	 * @return
	 */
	public int getSelectedNumberOfCluster() {
		return selectedNumberOfCluster;
	}

	/**
	 * Set selected number of cluster
	 * 
	 * @param selectedNumberOfCluster
	 */
	public void setSelectedNumberOfCluster(int selectedNumberOfCluster) {
		this.selectedNumberOfCluster = selectedNumberOfCluster;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("SSE Levels by Number of Clusters", // chart
																									// title
				"Number of Clusters", // x axis label
				"SSE", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
				);

		return chart;

	}

	/**
	 * Create dataset
	 * 
	 * @param data
	 * @return
	 */
	private XYDataset createDataset(Double[][] data) {

		final XYSeries series1 = new XYSeries("SSE");

		for (int i = 0; i < data.length; i++) {
			series1.add(data[i][0].intValue(), data[i][1]);

		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		return dataset;

	}
}
