package org.processmining.plugins.crossorgprocmin.mismatch.control;

import java.io.Serializable;
import java.util.List;

/**
 * Handles dependency unit data
 * 
 * @author onuryilmaz
 *
 */
public class DependencyUnit implements Serializable {

	private static final long serialVersionUID = 1125396062616002081L;

	public String activityName;
	public List<String> previousActivities;
	public GatewayTypeCode gateway;
	public Integer process_identifier;

	/**
	 * 
	 * @param activityName
	 * @param previousActivities
	 * @param gateway
	 * @param process_identifier
	 */
	public DependencyUnit(String activityName, List<String> previousActivities, GatewayTypeCode gateway,
			Integer process_identifier) {
		this.activityName = activityName;
		this.previousActivities = previousActivities;
		this.gateway = gateway;
		this.process_identifier = process_identifier;
	}

}
