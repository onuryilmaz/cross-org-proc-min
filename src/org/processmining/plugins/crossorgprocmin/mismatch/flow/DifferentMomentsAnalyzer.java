package org.processmining.plugins.crossorgprocmin.mismatch.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatternAnalyzer;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.ActivityUnit;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Analyzer for {@link DifferentMomentsUnit} and
 * {@link DifferentMomentsClusterUnit}
 * 
 * @author onuryilmaz
 *
 */
public class DifferentMomentsAnalyzer implements MismatchPatternAnalyzer {

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs) {

		for (Entry<Integer, List<String>> process_1 : logs.mismatchPatterns.activityComplete.entrySet()) {
			for (Entry<Integer, List<String>> process_2 : logs.mismatchPatterns.activityComplete.entrySet()) {
				if (!process_1.getKey().equals(process_2.getKey())) {

					List<String> common = new ArrayList<String>(process_1.getValue());
					common.retainAll(process_2.getValue());

					for (String activityName_1 : common) {
						List<FlowUnit> flows_process_1 = logs.mismatchPatterns.getFlowsByEndProcess(process_1.getKey(),
								activityName_1);
						List<FlowUnit> flows_process_1_next = logs.mismatchPatterns.getFlowsByStartProcess(
								process_1.getKey(), activityName_1);
						if (flows_process_1 != null && flows_process_1.size() == 1 && flows_process_1_next != null
								&& flows_process_1_next.size() == 1) {
							List<FlowUnit> flows_process_2 = logs.mismatchPatterns.getFlowsByEndProcess(
									process_2.getKey(), activityName_1);
							List<FlowUnit> flows_process_2_next = logs.mismatchPatterns.getFlowsByStartProcess(
									process_2.getKey(), activityName_1);
							if (flows_process_2 != null && flows_process_2.size() == 1 && flows_process_2_next != null
									&& flows_process_2_next.size() == 1) {
								if (!flows_process_1.get(0).from.equals(flows_process_2.get(0).from)
										|| !flows_process_1_next.get(0).to.equals(flows_process_2_next.get(0).to)) {

									DifferentMomentsUnit differentMomentsUnit = new DifferentMomentsUnit(
											process_1.getKey(), process_2.getKey(), activityName_1,
											flows_process_1.get(0).from, flows_process_1_next.get(0).to,
											flows_process_2.get(0).from, flows_process_2_next.get(0).to);
									logs.mismatchPatterns.differentMomentsProcess.add(differentMomentsUnit);
								}
							}

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
				if (!cluster_1.cluster.equals(cluster_2.cluster)) {

					List<String> common = new ArrayList<String>(cluster_1.activities);
					common.retainAll(cluster_2.activities);

					for (String activityName_1 : common) {
						List<FlowUnit> flows_process_1 = logs.mismatchPatterns.getFlowsByEndCluster(cluster_1.cluster,
								activityName_1);
						List<FlowUnit> flows_process_1_next = logs.mismatchPatterns.getFlowsByStartCluster(
								cluster_1.cluster, activityName_1);
						if (flows_process_1 != null && flows_process_1.size() == 1 && flows_process_1_next != null
								&& flows_process_1_next.size() == 1) {
							List<FlowUnit> flows_process_2 = logs.mismatchPatterns.getFlowsByEndProcess(
									cluster_2.cluster, activityName_1);
							List<FlowUnit> flows_process_2_next = logs.mismatchPatterns.getFlowsByStartProcess(
									cluster_2.cluster, activityName_1);
							if (flows_process_2 != null && flows_process_2.size() == 1 && flows_process_2_next != null
									&& flows_process_2_next.size() == 1) {
								if (!flows_process_1.get(0).from.equals(flows_process_2.get(0).from)
										|| !flows_process_1_next.get(0).to.equals(flows_process_2_next.get(0).to)) {

									DifferentMomentsClusterUnit differentMomentsUnit = new DifferentMomentsClusterUnit(
											cluster_1.cluster, cluster_2.cluster, activityName_1,
											flows_process_1.get(0).from, flows_process_1_next.get(0).to,
											flows_process_2.get(0).from, flows_process_2_next.get(0).to, startLabel,
											endLabel);
									logs.mismatchPatterns.differentMomentsCluster.add(differentMomentsUnit);
								}
							}

						}
					}
				}
			}
		}
		return logs;

	}
}
