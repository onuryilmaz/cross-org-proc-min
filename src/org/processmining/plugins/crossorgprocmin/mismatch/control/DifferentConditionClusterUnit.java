package org.processmining.plugins.crossorgprocmin.mismatch.control;

/**
 * Handles different condition data for clusters
 * 
 * @author onuryilmaz
 */
public class DifferentConditionClusterUnit extends DifferentConditionUnit {

	private static final long serialVersionUID = -1692626720187408087L;

	public String startLabel;
	public String endLabel;

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param dependencyUnit_1
	 * @param dependencyUnit_2
	 * @param startLabel
	 * @param endLabel
	 */
	public DifferentConditionClusterUnit(Integer process_1, Integer process_2, DependencyUnit dependencyUnit_1,
			DependencyUnit dependencyUnit_2, String startLabel, String endLabel) {
		super(process_1, process_2, dependencyUnit_1, dependencyUnit_2);
		this.startLabel = startLabel;
		this.endLabel = endLabel;
	}

}
