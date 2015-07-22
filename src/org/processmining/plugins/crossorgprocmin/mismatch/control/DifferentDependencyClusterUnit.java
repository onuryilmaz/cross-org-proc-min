package org.processmining.plugins.crossorgprocmin.mismatch.control;

/**
 * Handles different dependency data for clusters
 * 
 * @author onuryilmaz
 *
 */
public class DifferentDependencyClusterUnit extends DifferentDependencyUnit {

	public String startLabel;
	public String endLabel;

	private static final long serialVersionUID = 4159024050223496022L;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param dependencyUnit_1
	 * @param dependencyUnit_2
	 * @param startLabel
	 * @param endLabel
	 */
	public DifferentDependencyClusterUnit(Integer process_1, Integer process_2, DependencyUnit dependencyUnit_1,
			DependencyUnit dependencyUnit_2, String startLabel, String endLabel) {
		super(process_1, process_2, dependencyUnit_1, dependencyUnit_2);
		this.startLabel = startLabel;
		this.endLabel = endLabel;
	}

}
