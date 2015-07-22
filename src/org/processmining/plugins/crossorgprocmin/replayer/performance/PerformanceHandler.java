package org.processmining.plugins.crossorgprocmin.replayer.performance;

import java.io.BufferedWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.plugins.manifestanalysis.visualization.performancematrix.PerfMatrixManifestExporter;
import org.processmining.plugins.manifestanalysis.visualization.performancematrix.PerfMatrixManifestProvider;
import org.processmining.plugins.petrinet.manifestreplayer.transclassifier.TransClass;
import org.processmining.plugins.petrinet.manifestreplayresult.Manifest;
import org.processmining.plugins.petrinet.manifestreplayresult.ManifestEvClassPattern;
import org.processmining.plugins.pnalignanalysis.visualization.performancematrix.PerfMatrixStats;

/**
 * Performance handler from manifest files
 * 
 * @author onuryilmaz
 *
 */
public class PerformanceHandler {

	private PerfMatrixManifestExporter exporter;

	private Method privateStringMethod;

	/**
	 * Read summary from manifest file
	 * 
	 * @param manifest
	 * @return
	 * @throws Exception
	 */
	public PerformanceSummary summary(Manifest manifest) throws Exception {

		PerfMatrixManifestProvider provider = new PerfMatrixManifestProvider((ManifestEvClassPattern) manifest);

		Map<String, String> summaryMap = new HashMap<String, String>();

		double divisor = 1.0;

		TransClass[] desiredOrder = provider.getInt2TransClasses();

		Map<TransClass, Integer> map = provider.constructMapTransClass2Int();
		int[] orderInProvider = new int[desiredOrder.length];
		String[] labels = new String[desiredOrder.length];
		for (int i = 0; i < orderInProvider.length; i++) {
			orderInProvider[i] = map.get(desiredOrder[i]);
			labels[i] = desiredOrder[i].getId();
		}

		exporter = new PerfMatrixManifestExporter();
		privateStringMethod = exporter.getClass().getDeclaredMethod("writeStatsMatrix", String[].class, int[].class,
				PerfMatrixManifestProvider.class, BufferedWriter.class, PerfMatrixStats.class, double.class);
		privateStringMethod.setAccessible(true);

		PerformanceSummary performanceSummary = new PerformanceSummary();

		List<String> labelList = new ArrayList<String>();
		for (String l : labels) {
			labelList.add(l);
		}

		performanceSummary.labels = labelList;

		performanceSummary.averageTimesList = summarizePerformance(labels, orderInProvider, provider,
				PerfMatrixStats.AVG, divisor);

		performanceSummary.maximumTimesList = summarizePerformance(labels, orderInProvider, provider,
				PerfMatrixStats.MAX, divisor);

		performanceSummary.minimumTimesList = summarizePerformance(labels, orderInProvider, provider,
				PerfMatrixStats.MIN, divisor);

		performanceSummary.stdTimesList = summarizePerformance(labels, orderInProvider, provider,
				PerfMatrixStats.STDDEV, divisor);

		return performanceSummary;

	}

	/**
	 * Summarize performance
	 * 
	 * @param labels
	 * @param orderInProvider
	 * @param provider
	 * @param metric
	 * @param divisor
	 * @return
	 * @throws Exception
	 */
	private List<PerformanceCore> summarizePerformance(String[] labels, int[] orderInProvider,
			PerfMatrixManifestProvider provider, PerfMatrixStats metric, double divisor) throws Exception {

		List<PerformanceCore> list = new ArrayList<PerformanceCore>();

		int i = 0;
		for (String label : labels) {
			for (int j : orderInProvider) {
				PerformanceCore pc = new PerformanceCore();
				pc.from = label;
				pc.to = labels[orderInProvider[j]];
				pc.value = provider.getTimeBetween(orderInProvider[i], j, metric) / divisor;
				list.add(pc);
			}

			i++;
		}

		return list;

	}
}
