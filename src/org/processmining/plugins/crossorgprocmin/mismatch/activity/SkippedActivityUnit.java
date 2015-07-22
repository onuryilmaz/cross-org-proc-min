package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles skipped activity related data
 * 
 * @author onuryilmaz
 *
 */
public class SkippedActivityUnit implements Serializable {

	private static final long serialVersionUID = 1847554331196129238L;

	public Integer cluster;

	public String from;

	public String to;

	public List<String> skippedActivities = new ArrayList<String>();

	/**
	 * Constructor
	 * 
	 * @param cluster
	 * @param from
	 * @param to
	 * @param skippedActivities
	 */
	public SkippedActivityUnit(Integer cluster, String from, String to, List<String> skippedActivities) {
		super();
		this.cluster = cluster;
		this.from = from;
		this.to = to;
		this.skippedActivities = skippedActivities;
	}

}
