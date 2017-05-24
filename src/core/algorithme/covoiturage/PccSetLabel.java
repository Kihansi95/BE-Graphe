package core.algorithme.covoiturage;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstra.Pcc;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class PccSetLabel extends Pcc {

	private List<Noeud> destinations;
	
	public PccSetLabel(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg);
		destinations = null;	// non setter
	}

	public PccSetLabel contraintVision(Voyagueur pieton) {
		return this;
	}
	
	public void setEnsembleDestinations(List<Noeud> destinations)	{
		this.destinations = destinations;
	}
	
	public List<Noeud> getEnsembleDestinations()	{
		return this.destinations;
	}
	
	/**
	 * Retourne une liste clone des labels clone marque. 
	 * Cette list est consultable seulement.
	 * @return List<Label>
	 */
	protected List<Label> getLabelMarque()	{
		
		List<Label> labelMarque = new LinkedList<Label>();
		
		for(Map.Entry<Noeud, Label> entry : this.getSommets().entrySet())	{
    		Label label = entry.getValue();
    		if(label.isMarque())
    			labelMarque.add(new Label(label));
    	}
		
		return labelMarque;
	}
	
}
