package core.algorithme.dijkstramultidest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import core.Graphe;
import core.algorithme.dijkstra.*;
import core.graphe.Critere;
import core.graphe.Noeud;

public class PccSetLabel extends Pcc {

	private Set<Noeud> destinations;
	
	public PccSetLabel(Graphe gr, Noeud origine, Set<Noeud> destinations, Critere critere)	{
		super(gr, null, origine, null, critere);
		setDestinations(destinations);
	}
	
	public void setDestinations(Set<Noeud> destinations)	{
		this.destinations = destinations == null? new HashSet<Noeud>() : destinations;
	}
	
	public Set<Noeud> getDestinations()	{
		return this.destinations;
	}

	protected Label newLabel(Noeud sommet) {
		return new LabelCovoiturage(sommet);
	}

	@Override
	protected void initialize() {
		System.out.println("Run "+ this.getClass().getSimpleName() +" de " + this.getOrigine() ) ;
		
    	// init label
		Label label_origine = new Label(this.getOrigine(), 0f);
		
		this.tas.insert(label_origine);
		this.sommets.put(this.getOrigine(),label_origine);
		for(Noeud dest: this.destinations)	{
			this.sommets.put(dest, this.newLabel(dest));			
		}
	}

	@Override
	protected void terminate() {}	// eviter de construire le chemin

	@Override
	protected boolean conditionContinue() {
		
		// parcours tous les noeuds
		if(this.destinations.isEmpty())
			return !this.tas.isEmpty();
		
		// parcours jusque les dest soient marque
		boolean destAtteint = false;
		
		for(Iterator<Noeud> it = destinations.iterator(); it.hasNext() && !destAtteint;)	{
			Noeud dest = it.next();
			destAtteint &= this.getLabel(dest).isMarque();
		}
		
		return !this.tas.isEmpty() && !destAtteint;
	}
	
	public String toString()	{
		return "Dijkstra 1 vers n";
	}
	
	/**
	 * @retun clone map de label marque
	 */
	public Map<Noeud, Label> getLabelsMarques()	{
		Map<Noeud, Label> labelMarques = new HashMap<Noeud,Label>();
		for(Map.Entry<Noeud, Label> entry : this.sommets.entrySet()) {
		    Label label = entry.getValue();
		    if(label.isMarque())
		    	labelMarques.put(entry.getKey(), entry.getValue());
		}
		
		return labelMarques;
	}
}
