package org.processmining.plugins.crossorgprocmin.replayer.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Merging performance values between organizations
 * 
 * @author onuryilmaz
 *
 */
public class PerformanceMerger {

	/**
	 * Merge labels
	 * 
	 * @param logs
	 * @return
	 */
	public List<String> mergeLabels(OrganizationalLogs logs) {

		Set<String> set = new TreeSet<String>();

		List<String> completeLabels = new ArrayList<String>();
		for (PerformanceSummary summary : logs.performanceSummaries.values()) {
			set.addAll(summary.labels);
		}

		return new ArrayList<String>(set);

	}

	/**
	 * Merge performance values
	 * 
	 * @param logs
	 */
	public void merge(OrganizationalLogs logs) {

		List<String> completeLabels = mergeLabels(logs);

		for (Entry<Integer, PerformanceSummary> summary : logs.performanceSummaries.entrySet()) {

			PerformanceSummary summaryAligned = new PerformanceSummary();
			summaryAligned.labels = completeLabels;

			for (String from : completeLabels) {

				for (String to : completeLabels) {

					double contains = contains(summary.getValue().averageTimesList, from, to);
					summaryAligned.averageTimesList.add(new PerformanceCore(from, to, contains));

					contains = contains(summary.getValue().minimumTimesList, from, to);
					summaryAligned.minimumTimesList.add(new PerformanceCore(from, to, contains));

					contains = contains(summary.getValue().maximumTimesList, from, to);
					summaryAligned.maximumTimesList.add(new PerformanceCore(from, to, contains));

					contains = contains(summary.getValue().stdTimesList, from, to);
					summaryAligned.stdTimesList.add(new PerformanceCore(from, to, contains));
				}

			}

			logs.performanceSummariesAligned.put(summary.getKey(), summaryAligned);

		}
	}

	/**
	 * Check for containing performance values
	 * 
	 * @param list
	 * @param from
	 * @param to
	 * @return
	 */
	public double contains(List<PerformanceCore> list, String from, String to) {

		double foundValue = 0;

		for (PerformanceCore pc : list) {
			if (pc.to.equals(to) && pc.from.equals(from)) {

				foundValue = pc.value;
			}
		}

		return foundValue;
	}

	/**
	 * Clean performance values
	 * 
	 * @param logs
	 */
	public void clean(OrganizationalLogs logs) {

		int attributeSize = logs.performanceSummariesAligned.get(1).labels.size();
		int size = attributeSize * attributeSize;
		List<Boolean> deleteAverageTimesList = Arrays.asList(new Boolean[size]);
		Collections.fill(deleteAverageTimesList, new Boolean(false));

		List<Boolean> deleteStdTimesList = Arrays.asList(new Boolean[size]);
		Collections.fill(deleteStdTimesList, new Boolean(false));

		for (Entry<Integer, PerformanceSummary> summary : logs.performanceSummariesAligned.entrySet()) {

			for (int i = 0; i < size; i++) {
				if (summary.getValue().averageTimesList.get(i).value.isNaN()
						|| summary.getValue().averageTimesList.get(i).value.equals(new Double(0))) {
					deleteAverageTimesList.set(i, true);

				}
				if (summary.getValue().stdTimesList.get(i).value.isNaN()
						|| summary.getValue().stdTimesList.get(i).value.equals(new Double(0))) {
					deleteStdTimesList.set(i, true);

				}
			}
		}

		for (Entry<Integer, PerformanceSummary> summary : logs.performanceSummariesAligned.entrySet()) {

			PerformanceSummary summaryClean = new PerformanceSummary();

			for (int i = 0; i < size; i++) {
				if (deleteAverageTimesList.get(i) == false) {
					summaryClean.averageTimesList.add(summary.getValue().averageTimesList.get(i));
				}
				if (deleteStdTimesList.get(i) == false) {
					summaryClean.stdTimesList.add(summary.getValue().stdTimesList.get(i));
				}
			}

			summaryClean.labels = null;
			logs.performanceSummariesCleaned.put(summary.getKey(), summaryClean);
		}

	}

	//	public List<String> mergeCleanLabels(OrganizationalLogs logs) {
	//
	//		Set<String> set = new TreeSet<String>();
	//
	//		List<String> completeLabels = new ArrayList<String>();
	//		for (PerformanceSummary summary : logs.performanceSummariesCleaned.values()) {
	//			set.addAll(summary.labels);
	//		}
	//
	//		return new ArrayList<String>(set);
	//
	//	}

}
