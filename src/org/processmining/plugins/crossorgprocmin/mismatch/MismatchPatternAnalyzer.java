package org.processmining.plugins.crossorgprocmin.mismatch;

import java.util.Map;

import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Mismatch Analyzer
 * 
 * @author onuryilmaz
 *
 */
public interface MismatchPatternAnalyzer {

	/**
	 * Analyze without any performance clustering
	 * 
	 * @param logs
	 * @return
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs);

	/**
	 * Analyze with performance clustering
	 * 
	 * @param logs
	 * @param startLabel
	 * @param endLabel
	 * @param clusterAssignments
	 * @return
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs, String startLabel, String endLabel,
			Map<Integer, Integer> clusterAssignments);
}
