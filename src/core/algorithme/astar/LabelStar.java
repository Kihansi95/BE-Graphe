package core.algorithme.astar;

import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.graphe.Critere;
import core.graphe.Noeud;

public class LabelStar extends Label {
	
	private double heuristique;

	public LabelStar(Noeud sommetCourant) {
		super(sommetCourant);
		this.heuristique = 0f; // pas besoin de cout heuristique mtn
	}
	
	protected void updateEstimation(final Label destination, Critere critere, Graphe graphe)	{
		
		if(!(destination instanceof LabelStar))
			return;
		
		Noeud noeud_courant = this.getSommetCourant();
		Noeud noeud_dest = destination.getSommetCourant();
		
		double distance_estime = graphe.distance_sommets(noeud_courant.getNumero(), noeud_dest.getNumero());
		
		switch(critere)	{
		case DISTANCE:
			this.heuristique = distance_estime;
			break;
		case TEMPS:
			this.heuristique = (distance_estime * 60f )/ (graphe.getVitesseMax() * 1000f );
			break;
		default:
			System.out.println("Critere non supporte pour cet algorithme");
		}
	}
	
	
	@Override
	public int compareTo(Label label) { 
		int res =1;
		double comp = ((this.getCout() + this.heuristique) - (label.getCout() + ((LabelStar) label).heuristique)) ;
		
		if (comp<0)
			res=-1;
		if (comp==0)
			res = 0;
		return res ;
		//return (int)(((this.getCout() + this.heuristique) - (label.getCout() + ((LabelStar) label).heuristique))*PRECISION) ;
	}
	
	@Override
	public String toString()	{
		return super.toString() + ", heuristique: " + this.heuristique;
	}

}
