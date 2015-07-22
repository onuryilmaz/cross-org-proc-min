package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles activity related data
 * 
 * @author onuryilmaz
 *
 */
public class ActivityUnit implements Serializable {

	public Integer cluster;

	public String from;

	public String to;

	public List<String> activities = new ArrayList<String>();

	/**
	 * Constructor
	 * 
	 * @param cluster
	 * @param from
	 * @param to
	 * @param skippedActivities
	 */
	public ActivityUnit(Integer cluster, String from, String to, List<String> activities) {
		super();
		this.cluster = cluster;
		this.from = from;
		this.to = to;
		this.activities = activities;
	}
}
