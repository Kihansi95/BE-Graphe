package core.graphe.comparator;

import java.util.Comparator;

import core.graphe.Liaison;

public class DistanceComparator implements Comparator<Liaison> {

	//private final static float PRECISION = 1000f;
	
	//@Override
	public int compare(Liaison first, Liaison second) {
		//return (int) ( (first.getLongueur() - second.getLongueur())*PRECISION );
		int res =1;
		float comp =(first.getLongueur() - second.getLongueur()) ;
		if (comp<0)
			res=-1;
		if (comp==0)
			res = 0;
		return res ;	
	}
}
