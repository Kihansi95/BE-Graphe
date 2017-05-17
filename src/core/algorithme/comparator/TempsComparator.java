package core.algorithme.comparator;

import java.util.Comparator;

import core.graphe.Liaison;

public class TempsComparator implements Comparator<Liaison> {
	
	private final static float PRECISION = 1000f;

	@Override
	public int compare(Liaison first, Liaison second) {
		return (int) ((
				first.getLongueur()/ first.getDescripteur().vitesseMax() 		// first route time
				- second.getLongueur()/ second.getDescripteur().vitesseMax() 	// second route time
				)* PRECISION);															// precison before convert to int
	}
	
}
