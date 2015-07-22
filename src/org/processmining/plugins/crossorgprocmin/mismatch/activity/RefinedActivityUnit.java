package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles refined activity related data
 * 
 * @author onuryilmaz
 *
 */
public class RefinedActivityUnit implements Serializable {

	private static final long serialVersionUID = 5380381128497737857L;

	public RefinedActivityUnit(Integer process_1, Integer process_2, String activityName_1,
			Map<Double, String> activityNames_2) {
		super();
		this.process_1 = process_1;
		this.process_2 = process_2;
		this.activityName_1 = activityName_1;
		this.activityNames_2 = activityNames_2;
	}

	public Integer process_1;

	public Integer process_2;

	public String activityName_1;

	public Map<Double, String> activityNames_2 = new HashMap<Double, String>();
}
