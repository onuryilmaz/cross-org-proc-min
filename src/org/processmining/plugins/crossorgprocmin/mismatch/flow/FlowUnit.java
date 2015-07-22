package org.processmining.plugins.crossorgprocmin.mismatch.flow;

import java.io.Serializable;

/**
 * Handles flow unit data
 * 
 * @author onuryilmaz
 *
 */
public class FlowUnit implements Serializable {

	private static final long serialVersionUID = 7443766338809735857L;

	public String from;
	public String to;

	/**
	 * 
	 * @param from
	 * @param to
	 */
	public FlowUnit(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	/**
	 * Check equality of flow units
	 * 
	 * @param flow
	 * @return
	 */
	public boolean equals(FlowUnit flow) {
		if (this.from.equals(flow.from) && this.to.equals(flow.to))
			return true;

		return false;
	}
}
