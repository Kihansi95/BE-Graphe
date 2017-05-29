package core.graphe.comparator;

import java.util.Comparator;

import core.graphe.Liaison;

public class TempsComparator implements Comparator<Liaison> {
	
	//private final static float PRECISION = 1000f;

	//@Override
	public int compare(Liaison first, Liaison second) {
		/*return (int) ((
				first.getLongueur()/ first.getVitesseMax() 		// first route time
				- second.getLongueur()/ second.getVitesseMax() 	// second route time
				)* PRECISION);	*/
		// precison before convert to int
		int res =1;
		float comp = (first.getLongueur()/ first.getVitesseMax())- (second.getLongueur()/ second.getVitesseMax());
		if (comp<0)
			res=-1;
		if (comp==0)
			res = 0;
		return res ;
	}
	
}
