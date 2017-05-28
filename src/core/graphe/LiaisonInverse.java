package core.graphe;

import base.Descripteur;

public class LiaisonInverse extends Liaison {

	public LiaisonInverse(Noeud predecesseur, Noeud successeur, float longueur, Descripteur descripteur) {
		super(predecesseur, successeur, longueur, descripteur);
	}
	
	public void addSegment(Segment segment)	{
		if(segment == null)
			throw new IllegalArgumentException();
		segments.add(0,segment);
	}

}
