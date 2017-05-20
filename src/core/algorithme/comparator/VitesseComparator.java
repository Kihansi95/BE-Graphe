package core.algorithme.comparator;

import java.util.Comparator;

import core.graphe.Liaison;

public class VitesseComparator implements Comparator<Liaison> {

	//@Override
	public int compare(Liaison first, Liaison second) {
		return second.getDescripteur().vitesseMax() - first.getDescripteur().vitesseMax();
	}

}
