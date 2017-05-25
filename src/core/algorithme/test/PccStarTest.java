package core.algorithme.test;

import core.Graphe;
import core.algorithme.astar.PccStar;
import core.algorithme.dijkstra.Label;
import core.graphe.Critere;
import core.graphe.Noeud;

public class PccStarTest extends PccStar{
	
	public PccStarTest(Graphe gr, Noeud origine, Noeud destination, Critere critere)	{
		super(gr, null, origine, destination, critere);	
		hasSolution = false;
	}
	
    protected void terminate()	{
    	
    	Label label_destination = this.getLabel(this.getDestination());
    	
    	if(!label_destination.isMarque())	{
			this.hasSolution = false;
		}	else	{
			this.hasSolution = true;
			this.solution = buildChemin(label_destination);
		}
    	
    }

}
