package org.processmining.plugins.crossorgprocmin.mismatch.control;

import java.io.Serializable;

/**
 * Handles different dependency data
 * 
 * @author onuryilmaz
 *
 */
public class DifferentDependencyUnit implements Serializable {

	private static final long serialVersionUID = 3680146530491595143L;

	public Integer process_1;
	public Integer process_2;

	public DependencyUnit dependencyUnit_1;
	public DependencyUnit dependencyUnit_2;

	public boolean additionalDependencyFlag = false;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param dependencyUnit_1
	 * @param dependencyUnit_2
	 */
	public DifferentDependencyUnit(Integer process_1, Integer process_2, DependencyUnit dependencyUnit_1,
			DependencyUnit dependencyUnit_2) {
		super();
		this.process_1 = process_1;
		this.process_2 = process_2;
		this.dependencyUnit_1 = dependencyUnit_1;
		this.dependencyUnit_2 = dependencyUnit_2;
	}

}
