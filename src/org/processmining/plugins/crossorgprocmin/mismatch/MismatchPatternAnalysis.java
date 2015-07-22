package org.processmining.plugins.crossorgprocmin.mismatch;

import java.util.HashMap;
import java.util.Map;

import org.processmining.plugins.crossorgprocmin.mismatch.activity.RefinedActivityAnalyzer;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.SkippedActivityAnalyzer;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentConditionDependencyAnalyzer;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.DifferentMomentsAnalyzer;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 * Analyzer for complete set of mismatch patterns
 * 
 * @author onuryilmaz
 *
 */
public class MismatchPatternAnalysis implements MismatchPatternAnalyzer {

	SkippedActivityAnalyzer skippedActivityAnalyzer = new SkippedActivityAnalyzer();
	RefinedActivityAnalyzer refinedActivityAnalyzer = new RefinedActivityAnalyzer();
	DifferentMomentsAnalyzer differentMomentsAnalyzer = new DifferentMomentsAnalyzer();
	DifferentConditionDependencyAnalyzer differentConditionAnalyzer = new DifferentConditionDependencyAnalyzer();

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs) {

		logs = skippedActivityAnalyzer.analyze(logs);

		logs = refinedActivityAnalyzer.analyze(logs);

		logs = differentMomentsAnalyzer.analyze(logs);

		logs = differentConditionAnalyzer.analyze(logs);

		// Get cluster vectors
		SimpleKMeans clusterData = logs.clusterings.get(logs.selectedClusterSize);
		Instances centroids = clusterData.getClusterCentroids();

		// Get cluster assignments
		Map<Integer, Integer> clusterAssignments = new HashMap<Integer, Integer>();
		try {
			for (int ca = 0; ca < clusterData.getAssignments().length; ca++) {
				clusterAssignments.put(ca + 1, clusterData.getAssignments()[ca] + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// For each cluster vector, make analysis

		for (int j = 0; j < centroids.numAttributes(); j++) {
			String attributeText = centroids.attribute(j).name();
			if (attributeText.contains("(Std)"))
				continue;
			attributeText = attributeText.replace("(Avg)", "");
			attributeText = attributeText.replace("(Std)", "");
			attributeText = attributeText.replace("->", ";");
			String[] startEnd = attributeText.split(";");
			logs = analyze(logs, startEnd[0], startEnd[1], clusterAssignments);

		}

		return logs;
	}

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs, String startLabel, String endLabel,
			Map<Integer, Integer> clusterAssignments) {

		skippedActivityAnalyzer.analyze(logs, startLabel, endLabel, clusterAssignments);

		refinedActivityAnalyzer.analyze(logs, startLabel, endLabel, clusterAssignments);

		differentMomentsAnalyzer.analyze(logs, startLabel, endLabel, clusterAssignments);

		differentConditionAnalyzer.analyze(logs, startLabel, endLabel, clusterAssignments);

		return logs;
	}
}
