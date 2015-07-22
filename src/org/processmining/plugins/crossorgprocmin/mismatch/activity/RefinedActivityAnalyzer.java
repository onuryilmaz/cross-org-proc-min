package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatternAnalyzer;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * 
 * Analyzer for {@link RefinedActivityUnit} and
 * {@link RefinedActivityClusterUnit}
 * 
 * @author onuryilmaz
 *
 */
public class RefinedActivityAnalyzer implements MismatchPatternAnalyzer {

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs) {

		for (Entry<Integer, List<String>> process_1 : logs.mismatchPatterns.activityComplete.entrySet()) {
			for (Entry<Integer, List<String>> process_2 : logs.mismatchPatterns.activityComplete.entrySet()) {
				if (!process_1.getKey().equals(process_2.getKey())) {

					for (String activityName_1 : process_1.getValue()) {

						if (!process_2.getValue().contains(activityName_1)) {
							Map<Double, String> similarityValues = new TreeMap<Double, String>();

							for (String activityName_2 : process_2.getValue()) {

								similarityValues.put(StringSimilarity.similarity(activityName_1, activityName_2),
										activityName_2);
							}
							RefinedActivityUnit refinedActivityUnit = new RefinedActivityUnit(process_1.getKey(),
									process_2.getKey(), activityName_1, similarityValues);
							logs.mismatchPatterns.refinedActivityUnitsComplete.add(refinedActivityUnit);
						}
					}
				}
			}
		}

		return logs;
	}

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs, String startLabel, String endLabel,
			Map<Integer, Integer> clusterAssignments) {

		for (ActivityUnit cluster_1 : logs.mismatchPatterns.getActivities(startLabel, endLabel)) {
			List<String> cluster_1_activities = cluster_1.activities;

			for (ActivityUnit cluster_2 : logs.mismatchPatterns.getActivities(startLabel, endLabel)) {
				List<String> cluster_2_activities = cluster_2.activities;
				if (!cluster_1.cluster.equals(cluster_2.cluster))

					for (String activityName_1 : cluster_1_activities) {

						if (!cluster_2_activities.contains(activityName_1)) {
							Map<Double, String> similarityValues = new TreeMap<Double, String>();

							for (String activityName_2 : cluster_2_activities) {

								similarityValues.put(StringSimilarity.similarity(activityName_1, activityName_2),
										activityName_2);
							}
							RefinedActivityClusterUnit refinedActivityUnit = new RefinedActivityClusterUnit(
									cluster_1.cluster, cluster_2.cluster, activityName_1, similarityValues, startLabel,
									endLabel);
							logs.mismatchPatterns.refinedActivityUnitsClustered.add(refinedActivityUnit);

						}
					}
			}
		}

		return logs;
	}
}
