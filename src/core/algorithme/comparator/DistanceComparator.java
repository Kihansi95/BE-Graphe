package core.algorithme.comparator;

import java.util.Comparator;

import core.graphe.Liaison;

public class DistanceComparator implements Comparator<Liaison> {

	private final static float PRECISION = 1000f;
	
	@Override
	public int compare(Liaison first, Liaison second) {
		return (int) ( (first.getLongueur() - second.getLongueur())*PRECISION );
	}
	
}
