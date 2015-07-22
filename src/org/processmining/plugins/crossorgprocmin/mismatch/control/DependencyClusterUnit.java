package org.processmining.plugins.crossorgprocmin.mismatch.control;

import java.util.List;

/**
 * Handles dependency data for clusters
 * 
 * @author onuryilmaz
 */
public class DependencyClusterUnit extends DependencyUnit {

	private static final long serialVersionUID = -3555013208593203378L;

	public String startLabel;
	public String endLabel;

	/**
	 * 
	 * @param activityName
	 * @param previousActivities
	 * @param gateway
	 * @param process_identifier
	 * @param startLabel
	 * @param endLabel
	 */
	public DependencyClusterUnit(String activityName, List<String> previousActivities, GatewayTypeCode gateway,
			Integer process_identifier, String startLabel, String endLabel) {
		super(activityName, previousActivities, gateway, process_identifier);
		this.startLabel = startLabel;
		this.endLabel = endLabel;
	}

}
