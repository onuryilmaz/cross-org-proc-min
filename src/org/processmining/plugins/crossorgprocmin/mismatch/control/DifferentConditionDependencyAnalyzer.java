package org.processmining.plugins.crossorgprocmin.mismatch.control;

import java.util.Map;

import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatternAnalyzer;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Analyzer for {@link DifferentConditionUnit} and
 * {@link DifferentConditionClusterUnit}
 * 
 * @author onuryilmaz
 *
 */
public class DifferentConditionDependencyAnalyzer implements MismatchPatternAnalyzer {

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs) {

		for (DependencyUnit du_1 : logs.mismatchPatterns.dependencyUnitProcess) {
			for (DependencyUnit du_2 : logs.mismatchPatterns.dependencyUnitProcess) {
				// Check for different processes
				if (!du_1.process_identifier.equals(du_2.process_identifier)) {

					// Same activity
					if (du_1.activityName.equals(du_2.activityName)) {

						// Same dependency set
						if (du_1.previousActivities.containsAll(du_2.previousActivities)) {
							// Different gateway
							if (!du_1.gateway.equals(du_2.gateway)) {
								DifferentConditionUnit dcu = new DifferentConditionUnit(du_1.process_identifier,
										du_2.process_identifier, du_1, du_2);
								logs.mismatchPatterns.differentConditionProcess.add(dcu);
							}
						}

						// Same gateway
						if (du_1.gateway.equals(du_2.gateway)) {

							if (du_1.previousActivities.containsAll(du_2.previousActivities)
									&& du_2.previousActivities.containsAll(du_1.previousActivities)) {
								// Then they are all same
							} else {
								DifferentDependencyUnit ddu = new DifferentDependencyUnit(du_1.process_identifier,
										du_2.process_identifier, du_1, du_2);
								if (du_1.previousActivities.containsAll(du_2.previousActivities)
										|| du_2.previousActivities.containsAll(du_1.previousActivities)) {
									// It is additional dependency
									ddu.additionalDependencyFlag = true;
								}

								logs.mismatchPatterns.differentDependencyProcess.add(ddu);

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
		for (DependencyUnit du_1 : logs.mismatchPatterns.getDependencyClusterUnitsByLabels(startLabel, endLabel)) {
			for (DependencyUnit du_2 : logs.mismatchPatterns.getDependencyClusterUnitsByLabels(startLabel, endLabel)) {
				// Check for different processes
				if (!du_1.process_identifier.equals(du_2.process_identifier)) {

					// Same activity
					if (du_1.activityName.equals(du_2.activityName)) {

						// Same dependency set
						if (du_1.previousActivities.containsAll(du_2.previousActivities)) {
							// Different gateway
							if (!du_1.gateway.equals(du_2.gateway)) {
								DifferentConditionClusterUnit dccu = new DifferentConditionClusterUnit(
										du_1.process_identifier, du_2.process_identifier, du_1, du_2, startLabel,
										endLabel);
								logs.mismatchPatterns.differentConditionCluster.add(dccu);
							}
						}

						// Same gateway
						if (du_1.gateway.equals(du_2.gateway)) {

							if (du_1.previousActivities.containsAll(du_2.previousActivities)
									&& du_2.previousActivities.containsAll(du_1.previousActivities)) {
								// Then they are all same
							} else {
								DifferentDependencyClusterUnit ddcu = new DifferentDependencyClusterUnit(
										du_1.process_identifier, du_2.process_identifier, du_1, du_2, startLabel,
										endLabel);
								if (du_1.previousActivities.containsAll(du_2.previousActivities)
										|| du_2.previousActivities.containsAll(du_1.previousActivities)) {
									// It is additional dependency
									ddcu.additionalDependencyFlag = true;
								}

								logs.mismatchPatterns.differentDependencyCluster.add(ddcu);

							}
						}

					}
				}
			}
		}
		return logs;
	}

}
