package org.processmining.plugins.crossorgprocmin.mismatch.control;

import java.io.Serializable;

/**
 * Handles different condition data
 * 
 * @author onuryilmaz
 *
 */
public class DifferentConditionUnit implements Serializable {

	private static final long serialVersionUID = -7165186514101532400L;

	public Integer process_1;
	public Integer process_2;
	public DependencyUnit dependencyUnit_1;

	public DependencyUnit dependencyUnit_2;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param dependencyUnit_1
	 * @param dependencyUnit_2
	 */
	public DifferentConditionUnit(Integer process_1, Integer process_2, DependencyUnit dependencyUnit_1,
			DependencyUnit dependencyUnit_2) {
		super();
		this.process_1 = process_1;
		this.process_2 = process_2;
		this.dependencyUnit_1 = dependencyUnit_1;
		this.dependencyUnit_2 = dependencyUnit_2;
	}

}
