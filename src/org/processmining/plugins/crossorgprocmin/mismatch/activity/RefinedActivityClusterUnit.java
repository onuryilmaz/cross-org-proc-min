package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.util.Map;

/**
 * Handles refined activity data for clusters
 * 
 * @author I313226
 */
public class RefinedActivityClusterUnit extends RefinedActivityUnit {
	public String from;
	public String to;

	public RefinedActivityClusterUnit(Integer process_1, Integer process_2, String activityName_1,
			Map<Double, String> activityNames_2, String from, String to) {
		super(process_1, process_2, activityName_1, activityNames_2);
		this.from = from;
		this.to = to;
	}

}
