package org.processmining.plugins.crossorgprocmin.mismatch.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatternAnalyzer;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DependencyUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.GatewayTypeCode;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Analyzer for {@link SkippedActivityUnit}
 * 
 * @author onuryilmaz
 */
public class SkippedActivityAnalyzer implements MismatchPatternAnalyzer {

	public SkippedActivityAnalyzer() {
		gatewayTypes.put("Exclusive gateway", GatewayTypeCode.ExclusiveGateway);
		gatewayTypes.put("Parallel gateway", GatewayTypeCode.ParallelGateway);

	}

	Map<String, GatewayTypeCode> gatewayTypes = new HashMap<String, GatewayTypeCode>();

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs) {
		List<String> allActivities = new ArrayList<String>();
		Map<Integer, List<String>> activitiesInLogs = new HashMap<Integer, List<String>>();

		// Aggreagate all activites and in the logs
		int i = 1;
		for (BPMNDiagram diagram : logs.bpmnDiagrams.values()) {
			List<String> logActivities = new ArrayList<String>();
			for (Activity a : diagram.getActivities()) {

				String label = a.getLabel();
				if (!logActivities.contains(label))
					logActivities.add(label);

				if (!allActivities.contains(label))
					allActivities.add(label);
			}

			for (Flow f : diagram.getFlows()) {
				logs.mismatchPatterns.addFlow(i, f.getSource().getLabel(), f.getTarget().getLabel());
				gatewayAnalyzer(logs, diagram, f, i);
			}
			activitiesInLogs.put(i, logActivities);
			i++;

		}

		// Add all activities to handler
		for (Entry<Integer, List<String>> entry : activitiesInLogs.entrySet()) {
			logs.mismatchPatterns.activityComplete.put(entry.getKey(), entry.getValue());
		}

		// Check for skipped activities
		for (Entry<Integer, List<String>> entry : activitiesInLogs.entrySet()) {
			List<String> list = new ArrayList<String>();
			list.addAll(allActivities);

			list.removeAll(entry.getValue());
			if (list.size() > 0) {
				logs.mismatchPatterns.skippedActivityComplete.put(entry.getKey(), list);
			}
		}

		return logs;

	}

	/**
	 * {@inheritDoc}
	 */
	public OrganizationalLogs analyze(OrganizationalLogs logs, String startLabel, String endLabel,
			Map<Integer, Integer> clusterAssignments) {

		List<String> allActivities = new ArrayList<String>();
		Map<Integer, List<String>> clusteredActivities = new HashMap<Integer, List<String>>();

		int i = 1;
		// For each diagram
		for (BPMNDiagram diagram : logs.bpmnDiagrams.values()) {
			List<String> allActivitiesInDiagram = new ArrayList<String>();
			// Get the start and end nodes
			BPMNNode startNode = null;
			BPMNNode endNode = null;
			for (BPMNNode n : diagram.getNodes()) {
				if (n.getLabel().equals(startLabel)) {
					startNode = n;
				} else if (n.getLabel().equals(endLabel)) {
					endNode = n;
				}
			}

			// Get the starting flow
			List<Flow> starterFlows = new ArrayList<Flow>();
			for (Flow f : diagram.getFlows()) {
				if (f.getSource().equals(startNode)) {
					starterFlows.add(f);
					logs.mismatchPatterns.addFlowClustered(clusterAssignments.get(i), f.getSource().getLabel(), f
							.getTarget().getLabel());

				}
			}

			// Initiate path finder for each starter flow
			for (Flow starterFlow : starterFlows) {
				pathFinder(diagram, starterFlow, endNode, allActivitiesInDiagram, logs, clusterAssignments.get(i),
						startLabel, endLabel);

			}

			// Merge into total activities
			for (String s : allActivitiesInDiagram) {
				if (!allActivities.contains(s)) {
					allActivities.add(s);
				}
			}

			// Merge into all-cluster activities
			int clusterOfDiagram = clusterAssignments.get(i);
			if (clusteredActivities.containsKey(clusterOfDiagram)) {
				for (String s : allActivitiesInDiagram) {
					if (!clusteredActivities.get(clusterOfDiagram).contains(s)) {
						clusteredActivities.get(clusterOfDiagram).add(s);
					}
				}
			} else {
				clusteredActivities.put(clusterOfDiagram, allActivitiesInDiagram);
			}
			i++;
		}

		// Put all activities as clustered
		for (Entry<Integer, List<String>> tempList : clusteredActivities.entrySet()) {
			logs.mismatchPatterns.putActivities(startLabel, endLabel, tempList.getKey(), tempList.getValue());
		}

		// Remove all other clusters' activities
		for (Entry<Integer, List<String>> tempList : clusteredActivities.entrySet()) {
			List<String> allActivitiesToClear = new ArrayList<String>(allActivities);
			allActivitiesToClear.removeAll(tempList.getValue());

			logs.mismatchPatterns.putSkippedActivities(startLabel, endLabel, tempList.getKey(), allActivitiesToClear);
		}

		// Dependency unit handler
		for (DependencyUnit du : logs.mismatchPatterns.dependencyUnitProcess) {
			// If the main activity is included
			ActivityUnit activityUnit = logs.mismatchPatterns
					.getActivities(startLabel, endLabel, du.process_identifier);
			if (activityUnit != null) {
				List<String> activities = new ArrayList<String>(activityUnit.activities);
				activities.add(startLabel);
				activities.add(endLabel);
				if (activities.contains(du.activityName)) {
					// Check for previous activities
					List<String> activitiesInDU = new ArrayList<String>(du.previousActivities);
					// NOTE!!! activitiesInDU.retainAll(activities);
					if (activitiesInDU.size() > 0) {
						for (String previous_activity : activitiesInDU) {
							logs.mismatchPatterns.addDependencyUnitCluster(du.activityName, activitiesInDU, du.gateway,
									du.process_identifier, startLabel, endLabel);
						}
					}
				}
			}
		}

		return logs;
	}

	/**
	 * Helper method to find paths
	 * 
	 * @param diagram
	 * @param startingFlow
	 * @param endNode
	 * @param allActivitiesInDiagram
	 */
	private void pathFinder(BPMNDiagram diagram, Flow startingFlow, BPMNNode endNode,
			List<String> allActivitiesInDiagram, OrganizationalLogs logs, Integer clusterAssignment, String startLabel,
			String endLabel) {

		while (!startingFlow.getTarget().equals(endNode)) {
			String label = startingFlow.getTarget().getLabel();
			if (!allActivitiesInDiagram.contains(label) && startingFlow.getTarget().getClass().equals(Activity.class)) {
				allActivitiesInDiagram.add(label);
			}

			// Get the starting flow
			List<Flow> starterFlows = new ArrayList<Flow>();
			for (Flow f : diagram.getFlows()) {
				if (f.getSource().equals(startingFlow.getTarget())) {
					starterFlows.add(f);
					logs.mismatchPatterns.addFlowClustered(clusterAssignment, f.getSource().getLabel(), f.getTarget()
							.getLabel());
				}
			}

			// Check for reached to end node
			boolean reached = false;
			for (Flow ff : starterFlows) {
				if (ff.getTarget().equals(endNode)) {
					reached = true;
				}
			}
			// If not reached call over
			if (!reached) {
				for (Flow ff : starterFlows) {
					pathFinder(diagram, ff, endNode, allActivitiesInDiagram, logs, clusterAssignment, startLabel,
							endLabel);
				}
			}
			return;

		}
	}

	/**
	 * Helper method for analyzing gateway types
	 * 
	 * @param logs
	 * @param diagram
	 * @param startingFlow
	 * @param assignment
	 */
	private void gatewayAnalyzer(OrganizationalLogs logs, BPMNDiagram diagram, Flow startingFlow, Integer assignment) {

		if (startingFlow.getTarget().getClass().equals(Activity.class)
				&& startingFlow.getSource().getClass().equals(Gateway.class)) {

			Gateway gateway = (Gateway) startingFlow.getSource();

			List<Flow> source_flows = new ArrayList<Flow>();
			for (Flow f : diagram.getFlows()) {
				if (f.getTarget().equals(startingFlow.getSource())) {
					source_flows.add(f);
				}
			}

			List<String> source_activities = new ArrayList<String>();
			for (Flow f : source_flows) {
				source_activities.add(f.getSource().getLabel());
			}

			logs.mismatchPatterns.addDependencyUnitProcess(startingFlow.getTarget().getLabel(), source_activities,
					gatewayTypes.get(gateway.toString()), assignment);

		}
	}
}
