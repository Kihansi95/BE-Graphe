package core.graphe;

import java.awt.Color;

import base.Dessin;

public class InvisibleNoeud extends Noeud {

	public InvisibleNoeud(int numero, int zone) {
		super(numero, -1, -1, zone);
	}
	
	public void dessiner(Dessin dessin, Color color){
	}

}
