package org.processmining.plugins.crossorgprocmin.mismatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.plugins.crossorgprocmin.mismatch.activity.ActivityUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.RefinedActivityClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.RefinedActivityUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.activity.SkippedActivityUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DependencyClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DependencyUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentConditionClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentConditionUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentDependencyClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.DifferentDependencyUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.control.GatewayTypeCode;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.DifferentMomentsClusterUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.DifferentMomentsUnit;
import org.processmining.plugins.crossorgprocmin.mismatch.flow.FlowUnit;

/**
 * Mismatch Patterns for both processes and clusters
 * 
 * @author onuryilmaz
 *
 */
public class MismatchPatterns implements Serializable {

	private static final long serialVersionUID = 2908137875288362993L;

	public Map<Integer, List<String>> activityComplete = new HashMap<Integer, List<String>>();
	public List<ActivityUnit> activityClustered = new ArrayList<ActivityUnit>();

	public Map<Integer, List<FlowUnit>> flowsComplete = new HashMap<Integer, List<FlowUnit>>();
	public Map<Integer, List<FlowUnit>> flowsClustered = new HashMap<Integer, List<FlowUnit>>();

	public List<DifferentMomentsUnit> differentMomentsProcess = new ArrayList<DifferentMomentsUnit>();
	public List<DifferentMomentsClusterUnit> differentMomentsCluster = new ArrayList<DifferentMomentsClusterUnit>();

	public List<DependencyUnit> dependencyUnitProcess = new ArrayList<DependencyUnit>();
	public List<DependencyClusterUnit> dependencyUnitCluster = new ArrayList<DependencyClusterUnit>();

	public List<DifferentConditionUnit> differentConditionProcess = new ArrayList<DifferentConditionUnit>();
	public List<DifferentConditionClusterUnit> differentConditionCluster = new ArrayList<DifferentConditionClusterUnit>();

	public List<DifferentDependencyUnit> differentDependencyProcess = new ArrayList<DifferentDependencyUnit>();
	public List<DifferentDependencyClusterUnit> differentDependencyCluster = new ArrayList<DifferentDependencyClusterUnit>();

	public Map<Integer, List<String>> skippedActivityComplete = new HashMap<Integer, List<String>>();
	public List<SkippedActivityUnit> skippedActivityClustered = new ArrayList<SkippedActivityUnit>();

	public List<RefinedActivityUnit> refinedActivityUnitsComplete = new ArrayList<RefinedActivityUnit>();
	public List<RefinedActivityClusterUnit> refinedActivityUnitsClustered = new ArrayList<RefinedActivityClusterUnit>();

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @return
	 */
	public List<DifferentDependencyUnit> getDifferentDependencies(Integer process_1, Integer process_2) {
		List<DifferentDependencyUnit> returnList = new ArrayList<DifferentDependencyUnit>();

		for (DifferentDependencyUnit ddu : differentDependencyProcess) {
			if (ddu.process_1.equals(process_1) && ddu.process_2.equals(process_2)) {
				returnList.add(ddu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * 
	 * @param process_1
	 * @param process_2
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DifferentDependencyClusterUnit> getDifferentDependenciesCluster(Integer process_1, Integer process_2,
			String start, String end) {
		List<DifferentDependencyClusterUnit> returnList = new ArrayList<DifferentDependencyClusterUnit>();

		for (DifferentDependencyClusterUnit ddu : differentDependencyCluster) {
			if (ddu.process_1.equals(process_1) && ddu.process_2.equals(process_2) && ddu.startLabel.equals(start)
					&& ddu.endLabel.equals(end)) {
				returnList.add(ddu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DifferentConditionClusterUnit> getDifferentConditionsCluster(Integer process_1, Integer process_2,
			String start, String end) {
		List<DifferentConditionClusterUnit> returnList = new ArrayList<DifferentConditionClusterUnit>();

		for (DifferentConditionClusterUnit dmu : differentConditionCluster) {
			if (dmu.process_1.equals(process_1) && dmu.process_2.equals(process_2) && dmu.startLabel.equals(start)
					&& dmu.endLabel.equals(end)) {
				returnList.add(dmu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @return
	 */
	public List<DifferentConditionUnit> getDifferentConditions(Integer process_1, Integer process_2) {
		List<DifferentConditionUnit> returnList = new ArrayList<DifferentConditionUnit>();

		for (DifferentConditionUnit dmu : differentConditionProcess) {
			if (dmu.process_1.equals(process_1) && dmu.process_2.equals(process_2)) {
				returnList.add(dmu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DifferentMomentsClusterUnit> getDifferentMomentsCluster(Integer process_1, Integer process_2,
			String start, String end) {
		List<DifferentMomentsClusterUnit> returnList = new ArrayList<DifferentMomentsClusterUnit>();

		for (DifferentMomentsClusterUnit dmu : differentMomentsCluster) {
			if (dmu.process_1.equals(process_1) && dmu.process_2.equals(process_2) && dmu.from.equals(start)
					&& dmu.to.equals(end)) {
				returnList.add(dmu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @return
	 */
	public List<DifferentMomentsUnit> getDifferentMoments(Integer process_1, Integer process_2) {
		List<DifferentMomentsUnit> returnList = new ArrayList<DifferentMomentsUnit>();

		for (DifferentMomentsUnit dmu : differentMomentsProcess) {
			if (dmu.process_1.equals(process_1) && dmu.process_2.equals(process_2)) {
				returnList.add(dmu);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param activityName_1
	 * @return
	 */
	public RefinedActivityUnit getRefinedActivityUnitsComplete(Integer process_1, Integer process_2,
			String activityName_1) {

		for (RefinedActivityUnit ru : refinedActivityUnitsComplete) {
			if (ru.process_1.equals(process_1) && ru.process_2.equals(process_2)
					&& ru.activityName_1.equals(activityName_1)) {
				return ru;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @return
	 */
	public List<RefinedActivityUnit> getRefinedActivityUnitsComplete(Integer process_1, Integer process_2) {

		List<RefinedActivityUnit> returnList = new ArrayList<RefinedActivityUnit>();
		for (RefinedActivityUnit ru : refinedActivityUnitsComplete) {
			if (ru.process_1.equals(process_1) && ru.process_2.equals(process_2)) {
				returnList.add(ru);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param process_1
	 * @param process_2
	 * @param start
	 * @param end
	 * @return
	 */
	public List<RefinedActivityClusterUnit> getRefinedActivityUnitsClustered(Integer process_1, Integer process_2,
			String start, String end) {

		List<RefinedActivityClusterUnit> returnList = new ArrayList<RefinedActivityClusterUnit>();

		for (RefinedActivityClusterUnit ru : refinedActivityUnitsClustered) {
			if (ru.process_1.equals(process_1) && ru.process_2.equals(process_2) && ru.from.equals(start)
					&& ru.to.equals(end)) {
				returnList.add(ru);
			}
		}
		return returnList;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param cluster
	 * @return
	 */
	public SkippedActivityUnit getSkippedActivities(String from, String to, Integer cluster) {

		for (SkippedActivityUnit s : skippedActivityClustered) {
			if (s.from.equals(from) && s.to.equals(to) && s.cluster.equals(cluster))
				return s;
		}
		return null;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param cluster
	 * @param activities
	 */
	public void putSkippedActivities(String from, String to, Integer cluster, List<String> activities) {
		SkippedActivityUnit activityUnit = new SkippedActivityUnit(cluster, from, to, activities);
		skippedActivityClustered.add(activityUnit);
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param cluster
	 * @return
	 */
	public ActivityUnit getActivities(String from, String to, Integer cluster) {

		for (ActivityUnit s : activityClustered) {
			if (s.from.equals(from) && s.to.equals(to) && s.cluster.equals(cluster))
				return s;
		}
		return null;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param cluster
	 * @return
	 */
	public List<ActivityUnit> getActivities(String from, String to) {

		List<ActivityUnit> activities = new ArrayList<ActivityUnit>();

		for (ActivityUnit s : activityClustered) {
			if (s.from.equals(from) && s.to.equals(to))
				activities.add(s);
		}
		return activities;
	}

	/**
	 * 
	 * @param process
	 * @param from
	 * @param to
	 */
	public void addFlow(Integer process, String from, String to) {

		FlowUnit fu_add = new FlowUnit(from, to);

		if (flowsComplete.containsKey(process)) {

			if (!flowsComplete.get(process).contains(fu_add)) {
				flowsComplete.get(process).add(fu_add);
			}
		} else {
			List<FlowUnit> fus = new ArrayList<FlowUnit>();
			fus.add(fu_add);
			flowsComplete.put(process, fus);

		}

	}

	/**
	 * 
	 * @param cluster
	 * @param from
	 * @param to
	 */
	public void addFlowClustered(Integer cluster, String from, String to) {

		FlowUnit fu_add = new FlowUnit(from, to);

		if (flowsClustered.containsKey(cluster)) {

			if (!flowsClustered.get(cluster).contains(fu_add)) {
				flowsClustered.get(cluster).add(fu_add);
			}
		} else {
			List<FlowUnit> fus = new ArrayList<FlowUnit>();
			fus.add(fu_add);
			flowsClustered.put(cluster, fus);

		}

	}

	/**
	 * 
	 * @param process
	 * @param from
	 * @return
	 */
	public List<FlowUnit> getFlowsByStartProcess(Integer process, String from) {

		List<FlowUnit> returnList = new ArrayList<FlowUnit>();

		for (FlowUnit fu : flowsComplete.get(process)) {
			if (fu.from.equals(from))
				returnList.add(new FlowUnit(fu.from, fu.to));
		}

		return returnList;
	}

	/**
	 * 
	 * @param process
	 * @param to
	 * @return
	 */
	public List<FlowUnit> getFlowsByEndProcess(Integer process, String to) {

		List<FlowUnit> returnList = new ArrayList<FlowUnit>();

		for (FlowUnit fu : flowsComplete.get(process)) {
			if (fu.to.equals(to))
				returnList.add(new FlowUnit(fu.from, fu.to));
		}

		return returnList;
	}

	/**
	 * 
	 * @param cluster
	 * @param from
	 * @return
	 */
	public List<FlowUnit> getFlowsByStartCluster(Integer cluster, String from) {

		List<FlowUnit> returnList = new ArrayList<FlowUnit>();

		for (FlowUnit fu : flowsClustered.get(cluster)) {
			if (fu.from.equals(from))
				returnList.add(new FlowUnit(fu.from, fu.to));
		}

		return returnList;
	}

	/**
	 * 
	 * @param cluster
	 * @param to
	 * @return
	 */
	public List<FlowUnit> getFlowsByEndCluster(Integer cluster, String to) {

		List<FlowUnit> returnList = new ArrayList<FlowUnit>();

		for (FlowUnit fu : flowsComplete.get(cluster)) {
			if (fu.to.equals(to))
				returnList.add(new FlowUnit(fu.from, fu.to));
		}

		return returnList;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param cluster
	 * @param activities
	 */
	public void putActivities(String from, String to, Integer cluster, List<String> activities) {
		ActivityUnit activityUnit = new ActivityUnit(cluster, from, to, activities);
		activityClustered.add(activityUnit);
	}

	/**
	 * 
	 * @param label
	 * @param source_activities
	 * @param gateway
	 * @param process
	 */
	public void addDependencyUnitProcess(String label, List<String> source_activities, GatewayTypeCode gateway,
			Integer process) {

		DependencyUnit dependencyUnit = new DependencyUnit(label, source_activities, gateway, process);
		dependencyUnitProcess.add(dependencyUnit);

	}

	/**
	 * 
	 * @param label
	 * @param source_activities
	 * @param gateway
	 * @param process
	 * @param startLabel
	 * @param endLabel
	 */
	public void addDependencyUnitCluster(String label, List<String> source_activities, GatewayTypeCode gateway,
			Integer process, String startLabel, String endLabel) {

		DependencyClusterUnit dependencyUnit = new DependencyClusterUnit(label, source_activities, gateway, process,
				startLabel, endLabel);
		dependencyUnitCluster.add(dependencyUnit);

	}

	/**
	 * 
	 * @param startLabel
	 * @param endLabel
	 * @return
	 */
	public List<DependencyClusterUnit> getDependencyClusterUnitsByLabels(String startLabel, String endLabel) {

		List<DependencyClusterUnit> returnList = new ArrayList<DependencyClusterUnit>();

		for (DependencyClusterUnit dcu : dependencyUnitCluster) {
			if (dcu.startLabel.equals(startLabel) && dcu.endLabel.equals(endLabel)) {
				returnList.add(dcu);
			}
		}
		return returnList;

	}
}
