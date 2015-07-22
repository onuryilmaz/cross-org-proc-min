package org.processmining.plugins.crossorgprocmin.replayer.performance;

import java.io.Serializable;

/**
 * Core performance value handler
 * 
 * @author onuryilmaz
 *
 */
public class PerformanceCore implements Serializable {

	private static final long serialVersionUID = 1182742765339370590L;

	public String from;

	public String to;

	public Double value;

	public PerformanceCore() {

	}

	public PerformanceCore(String from, String to, Double value) {

		this.from = from;
		this.to = to;
		this.value = value;
	}

	public String toString() {

		return from + "->" + to + "," + value;
	}
}
