package org.processmining.plugins.crossorgprocmin.replayer.performance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Summary object for performance values
 * 
 * @author onuryilmaz
 *
 */
public class PerformanceSummary implements Serializable {

	private static final long serialVersionUID = 1988088103241744425L;

	public List<String> labels = new ArrayList<String>();

	public List<PerformanceCore> averageTimesList = new ArrayList<PerformanceCore>();
	public List<PerformanceCore> maximumTimesList = new ArrayList<PerformanceCore>();
	public List<PerformanceCore> minimumTimesList = new ArrayList<PerformanceCore>();
	public List<PerformanceCore> stdTimesList = new ArrayList<PerformanceCore>();

	/**
	 * {@inheritDoc}
	 */
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("\n");
		for (PerformanceCore pc : averageTimesList) {
			builder.append("Avg" + pc.toString() + "\n");
		}

		builder.append("\n");
		for (PerformanceCore pc : stdTimesList) {
			builder.append("Std" + pc.toString() + "\n");
		}

		return builder.toString();

	}
}
