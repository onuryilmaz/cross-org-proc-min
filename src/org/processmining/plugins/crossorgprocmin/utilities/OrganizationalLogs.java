package org.processmining.plugins.crossorgprocmin.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.annotations.AuthoredType;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatterns;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceSummary;
import org.processmining.plugins.petrinet.manifestreplayresult.Manifest;

import weka.clusterers.SimpleKMeans;

/**
 * Handles data for organizational logs
 * 
 * @author onuryilmaz
 *
 */
@AuthoredType(typeName = "Cross-Organizational Analysis", affiliation = "METU", author = "Onur Yilmaz", email = "yilmaz.onur@metu.edu.tr")
public class OrganizationalLogs implements Serializable {

	private static final long serialVersionUID = 6669992513938243194L;

	transient public Map<Integer, XLog> logs = new HashMap<Integer, XLog>();

	transient public Map<Integer, Petrinet> petriNets = new HashMap<Integer, Petrinet>();

	transient public Map<Integer, BPMNDiagram> bpmnDiagrams = new HashMap<Integer, BPMNDiagram>();

	transient public Map<Integer, Manifest> manifests = new HashMap<Integer, Manifest>();

	public MismatchPatterns mismatchPatterns = new MismatchPatterns();

	public Map<Integer, PerformanceSummary> performanceSummaries = new HashMap<Integer, PerformanceSummary>();

	public Map<Integer, PerformanceSummary> performanceSummariesAligned = new HashMap<Integer, PerformanceSummary>();

	public Map<Integer, PerformanceSummary> performanceSummariesCleaned = new HashMap<Integer, PerformanceSummary>();

	public Map<Integer, SimpleKMeans> clusterings = new HashMap<Integer, SimpleKMeans>();

	public List<String> organizationNames = new ArrayList<String>(100);

	public int selectedClusterSize = 2;

}
