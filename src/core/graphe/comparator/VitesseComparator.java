package core.graphe.comparator;

import java.util.Comparator;

import core.algorithme.astar.LabelStar;
import core.graphe.Liaison;

public class VitesseComparator implements Comparator<Liaison> {

	//@Override
	public int compare(Liaison first, Liaison second) {
		int res =1;
		int comp = second.getVitesseMax() - first.getVitesseMax() ;
		if (comp<0)
			res=-1;
		if (comp==0)
			res = 0;
		return res ;
		//return second.getVitesseMax() - first.getVitesseMax();
	}

}
