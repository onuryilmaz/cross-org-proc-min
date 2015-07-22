package org.processmining.plugins.crossorgprocmin.replayer.extended;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.finalmarkingprovider.FinalMarkingFactory;
import org.processmining.plugins.petrinet.finalmarkingprovider.MarkingEditorPanel;

/**
 * Extended for finding final marking
 * 
 * @author onuryilmaz
 *
 */
public class FinalMarkingFactoryExt extends FinalMarkingFactory {

	/**
	 * {@inheritDoc}
	 */
	public Object[] constructFinalMarking(UIPluginContext context, PetrinetGraph net) {
		MarkingEditorPanel editor = new MarkingEditorPanel("Final Marking");
		Marking finalMarking = new Marking();

		for (Place place : net.getPlaces()) {
			if (place.getLabel().equals("End")) {
				finalMarking.add(place);
			}
		}
		// return the first final marking
		return constructFinalMarking(context, net, finalMarking);
	}

	/**
	 * {@inheritDoc}
	 */
	public Marking[] constructFinalMarking2(UIPluginContext context, PetrinetGraph net) {
		MarkingEditorPanel editor = new MarkingEditorPanel("Final Marking");
		Marking finalMarking = new Marking();

		for (Place place : net.getPlaces()) {
			if (place.getLabel().equals("End")) {
				finalMarking.add(place);
			}
		}
		// return the first final marking
		return new Marking[] { finalMarking };
	}
}
