package core.algorithme.covoiturage;

import java.io.PrintStream;

import base.Readarg;
import core.Algo;
import core.Graphe;

public final class Covoiturage extends Algo {
	
	private Voyagueur pieton;
	private Voyagueur voiture;
	
	protected Covoiturage(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);
	}
	
	public void run()	{
		
		// init voyageurs
		Voyagueur pieton = new Voyagueur("U1", 10);
		Voyagueur voiture = new Voyagueur("U2", 100);
		
		
	}

}
