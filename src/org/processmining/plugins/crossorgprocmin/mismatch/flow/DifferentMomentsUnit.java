package org.processmining.plugins.crossorgprocmin.mismatch.flow;

import java.io.Serializable;

/**
 * Handles different moments data
 * 
 * @author onuryilmaz
 */
public class DifferentMomentsUnit implements Serializable {

	private static final long serialVersionUID = 3466229925467396161L;

	public Integer process_1;
	public Integer process_2;
	public String activityName;
	public String previous_1;
	public String next_1;
	public String previous_2;
	public String next_2;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param activityName
	 * @param previous_1
	 * @param next_1
	 * @param previous_2
	 * @param next_2
	 */
	public DifferentMomentsUnit(Integer process_1, Integer process_2, String activityName, String previous_1,
			String next_1, String previous_2, String next_2) {
		super();
		this.process_1 = process_1;
		this.process_2 = process_2;
		this.activityName = activityName;
		this.previous_1 = previous_1;
		this.next_1 = next_1;
		this.previous_2 = previous_2;
		this.next_2 = next_2;
	}

}
