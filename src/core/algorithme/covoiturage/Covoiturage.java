package core.algorithme.covoiturage;

import java.io.PrintStream;
import java.util.List;

import base.Readarg;
import core.Algo;
import core.Graphe;
import core.algorithme.dijkstra.Pcc;

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
		
		PccSetLabel pccModif = new PccSetLabel(this.graphe, sortie, null);
		
		// Dijkstra les noeuds
		pccModif.contraintVision(pieton).run();
		List<LabelCovoiturage> pointsPietons = pccModif.getSolution();
	}

}
