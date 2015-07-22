package org.processmining.plugins.crossorgprocmin.mismatch.flow;

/**
 * Handles different moments data for clusters
 * 
 * @author onuryilmaz
 *
 */
public class DifferentMomentsClusterUnit extends DifferentMomentsUnit {

	private static final long serialVersionUID = -1782894071743453890L;

	public String from;
	public String to;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param activityName
	 * @param previous_1
	 * @param next_1
	 * @param previous_2
	 * @param next_2
	 * @param from
	 * @param label
	 */
	public DifferentMomentsClusterUnit(Integer process_1, Integer process_2, String activityName, String previous_1,
			String next_1, String previous_2, String next_2, String from, String label) {
		super(process_1, process_2, activityName, previous_1, next_1, previous_2, next_2);
		this.from = from;
		this.to = label;
	}

}
