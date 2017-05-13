package core.algorithme;

import core.algo_duc.Label;
import core.graphe.Liaison;
import core.graphe.Noeud;

public class LabelStar extends Label {
	
	private double butEstimation;

	public LabelStar(Noeud sommetCourant) {
		super(sommetCourant);
	}
	
	public void update(LabelStar pere, Liaison liaison, LabelStar labelDestination)	{
		this.update(pere, liaison);
		
		Noeud courrant = this.getSommetCourant();
		Noeud destination = labelDestination.getSommetCourant();
		this.butEstimation = Math.sqrt((courrant.getLatitude() - destination.getLatitude()) + (courrant.getLongitude() - destination.getLongitude()));
	}
	
	@Override
	public int compareTo(Label label) { 
		return (int)((this.getCout() + butEstimation - label.getCout() - ((LabelStar) label).butEstimation)*1000f) ;
	}

}
