package core.algorithme.covoiturage;

import java.io.PrintStream;

import base.Readarg;
import core.Algo;
import core.Graphe;

public final class Covoiturage extends Algo {
	
	private Voyageur pieton;
	private Voyageur voiture;
	
	protected Covoiturage(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);
	}
	
	public void run()	{
		
		// init voyageurs
		Voyageur pieton = new Voyageur("U1", 10);
	}

}
