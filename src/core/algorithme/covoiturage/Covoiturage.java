package core.algorithme.covoiturage;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import base.Readarg;
import core.Algo;
import core.Graphe;
import core.algorithme.astar.PccStar;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstra.Pcc;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Covoiturage extends PccSetLabel {
	
	private Voyagueur pieton;
	private Voyagueur automobile;
	
	private Noeud destination;
	
	private PccSetLabel pccModif;
	
	public Covoiturage(Graphe gr, PrintStream fichierSortie, Readarg readarg) throws SommetNonExisteException, OperationNotSupportedException {
		super(gr, fichierSortie, readarg);

		
		this.pccModif = new PccSetLabel(this.graphe, sortie, null);
		
		int choice = readarg.lireInt("Choissez votre location par (0) en tapant coordonne (non implemente), (1) en tapant num de noeud, (2) en cliquant sur la carte\n> ");
		Noeud departPieton = null;
		Noeud departAuto = null;
		
		switch(choice)	{
		case 0:
			System.err.println("Non implemente");
			throw new OperationNotSupportedException();
			
		case 1:
			int numNoeud = readarg.lireInt("Noeud U1 > ");
			departPieton = this.graphe.getNoeud(numNoeud);
			
			numNoeud = readarg.lireInt("Noeud U2 > ");
			departAuto = this.graphe.getNoeud(numNoeud);
			
			numNoeud = readarg.lireInt("Noeud destination > ");
			this.destination = this.graphe.getNoeud(numNoeud);
			break;
			
		case 2:
			System.out.println("Cliquez pour U1...");
			departPieton = this.graphe.getNoeudByClick();
			
			System.out.println("Cliquez pour U2...");
			departAuto = this.graphe.getNoeudByClick();
			
			System.out.println("Cliquez pour destination...");
			this.destination = this.graphe.getNoeudByClick();
			break;
			
		default:
			System.err.println("Selection inconnu");
			throw new RuntimeException();	
		}
		
		this.pieton = new Voyagueur("U1", 10, this.getLabel(departPieton)); 
		this.automobile = new Voyagueur("U2", 500, this.getLabel(departAuto));
	}
	
	/**
     * Instancier le nouveau label
     * @param sommet
     * @return
     */
    protected Label newLabel(Noeud sommet)	{
    	return new LabelCovoiturage(sommet);
    }
	
	public void run()  	{
	
		try {
			// Dijkstra les noeuds
			
			this.setNoeudOrigine(pieton.getDepart());
			this.setNoeudDestination(this.destination);
			super.run();
			
			List<Label> pietonReach = this.getLabelMarque();
			
			this.setNoeudOrigine(automobile.getDepart());
			this.setNoeudDestination(this.destination);
			super.run();
			
			List<Label> automobileReach = this.getLabelMarque();
			
			// intersection les 2 ensembles => points de rdv
			List<Noeud> pointsRdv = new LinkedList<Noeud>();
			for(Label intersectPoint: pietonReach)
				if(automobileReach.contains(intersectPoint))
					pointsRdv.add(intersectPoint.getSommetCourant());
			
			// Pcc reverse de destination vers origine
			this.graphe.reverse();
			this.setNoeudOrigine(this.destination);
			this.setEnsembleDestinations(pointsRdv);
			super.run();
			
			// get le point rdv le plus optimiser
			/*
			float minCout = Float.MAX_VALUE;
			Noeud pointOptimiser
			for(Noeud rdv: pointsRdv)	{
				if(this.getLabel(rdv).getCout() < minCout)
					
			}*/
			
		} catch (SommetNonExisteException e) {
			e.printStackTrace();
		}

	}

}
