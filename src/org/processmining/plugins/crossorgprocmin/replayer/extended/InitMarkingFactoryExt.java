package org.processmining.plugins.crossorgprocmin.replayer.extended;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.finalmarkingprovider.MarkingEditorPanel;
import org.processmining.plugins.petrinet.initmarkingprovider.InitMarkingFactory;

/**
 * Extended for finding initial marking
 * 
 * @author onuryilmaz
 *
 */
public class InitMarkingFactoryExt extends InitMarkingFactory {

	/**
	 * {@inheritDoc}
	 */
	public Object[] constructInitMarking(UIPluginContext context, PetrinetGraph net) {
		MarkingEditorPanel editor = new MarkingEditorPanel("Initial Marking");
		Marking initMarking = new Marking();

		for (Place place : net.getPlaces()) {
			if (place.getLabel().equals("Start")) {
				initMarking.add(place);
			}
		}

		if (initMarking == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}

		// return the first final marking
		return constructInitMarking(context, net, initMarking);
	}

}