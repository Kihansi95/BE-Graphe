package core.algorithme;

import core.algo_duc.Label;
import core.graphe.Liaison;
import core.graphe.Noeud;

public class LabelStar extends Label {
	
	private double butEstimation;

	public LabelStar(Noeud sommetCourant) {
		super(sommetCourant);
	}
	
	protected void updateEstimation(final LabelStar destination)	{
		Noeud courant = this.getSommetCourant();
		Noeud dest = destination.getSommetCourant();
		this.butEstimation = Math.sqrt(
					Math.pow(courant.getLatitude() - dest.getLatitude(),2d) 
					+ Math.pow(courant.getLongitude() - dest.getLongitude(),2d)
				);
	}
	
	@Override
	public int compareTo(Label label) { 
		return (int)((this.getCout() + butEstimation - label.getCout() - ((LabelStar) label).butEstimation)*1000f) ;
	}

}
