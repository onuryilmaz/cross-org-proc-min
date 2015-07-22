package org.processmining.plugins.crossorgprocmin.cluster;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Map.Entry;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceCore;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceSummary;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 * 
 * K-means clusterer based on performance indicators of organizations
 * 
 * @author onuryilmaz
 */
public class KMeansPerformanceClusterer {

	public OrganizationalLogs cluster(UIPluginContext context, OrganizationalLogs organizationalLogs) throws Exception {

		StringBuffer buffer = createARRF(organizationalLogs);

		BufferedReader datafile = new BufferedReader(new StringReader(buffer.toString()));
		Instances data = new Instances(datafile);

		for (int i = 1; i <= organizationalLogs.logs.size(); i++) {
			SimpleKMeans kmeans = new SimpleKMeans();

			kmeans.setSeed(Calendar.MILLISECOND);
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(i);

			kmeans.buildClusterer(data);
			organizationalLogs.clusterings.put(i, kmeans);
		}

		if (organizationalLogs.logs.size() < 2) {
			organizationalLogs.selectedClusterSize = 1;
		} else {
			KMeansPerformanceClustererDialog dialog = new KMeansPerformanceClustererDialog(organizationalLogs);
			InteractionResult result = context.showConfiguration("Select Number of Clusters (k)", dialog);

			organizationalLogs.selectedClusterSize = dialog.getSelectedNumberOfCluster();

		}

		return organizationalLogs;
	}

	/**
	 * Create ARRF data from performance values
	 * 
	 * @param logs
	 * @return
	 */
	private StringBuffer createARRF(OrganizationalLogs logs) {
		StringBuffer buffer = new StringBuffer();

		for (Entry<Integer, PerformanceSummary> summary : logs.performanceSummariesCleaned.entrySet()) {

			PerformanceSummary perfSummary = summary.getValue();

			buffer.append("@relation crossorgdata");
			buffer.append("\n");

			for (PerformanceCore pc : perfSummary.averageTimesList) {
				buffer.append("@attribute" + " \"" + pc.from + "->" + pc.to + "(Avg)\"" + " " + "numeric");
				buffer.append("\n");
			}
			for (PerformanceCore pc : perfSummary.stdTimesList) {
				buffer.append("@attribute" + " \"" + pc.from + "->" + pc.to + "(Std)\"" + " " + "numeric");
				buffer.append("\n");
			}

			buffer.append("\n");
			buffer.append("@data");
			buffer.append("\n");
			break;

		}

		for (Entry<Integer, PerformanceSummary> summary : logs.performanceSummariesCleaned.entrySet()) {

			PerformanceSummary perfSummary = summary.getValue();

			for (PerformanceCore pc : perfSummary.averageTimesList) {
				buffer.append(pc.value);
				buffer.append(",");

			}
			for (PerformanceCore pc : perfSummary.stdTimesList) {
				buffer.append(pc.value);
				buffer.append(",");

			}
			buffer.append("\n");
		}

		return buffer;

	}
}
