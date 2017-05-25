package application;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import base.Readarg;
import core.Algo;
import core.Graphe;
import core.algorithme.AbstractPcc;
import core.algorithme.astar.PccStar;
import core.algorithme.covoiturage.LabelCovoiturage;
import core.algorithme.covoiturage.PccSetLabel;
import core.algorithme.covoiturage.Voyagueur;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstra.Pcc;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Covoiturage {
	
	private Graphe graphe;
	private Voyagueur pieton;
	private Voyagueur automobile;
	
	private Noeud destination;
	
	private Pcc algo;
	
	public Covoiturage(Graphe gr, Readarg readarg) throws SommetNonExisteException, OperationNotSupportedException {
		
		int choice = readarg.lireInt("Choissez votre location par (0) en tapant num de noeud, (1) en cliquant sur la carte\n> ");
		Noeud departPieton = null;
		Noeud departAuto = null;
		
		switch(choice)	{
			
		case 0:
			int numNoeud = readarg.lireInt("Noeud U1 > ");
			departPieton = this.graphe.getNoeud(numNoeud);
			
			numNoeud = readarg.lireInt("Noeud U2 > ");
			departAuto = this.graphe.getNoeud(numNoeud);
			
			numNoeud = readarg.lireInt("Noeud destination > ");
			this.destination = this.graphe.getNoeud(numNoeud);
			break;
			
		case 1:
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
			
			algo.setNoeudOrigine(pieton.getDepart());
			algo.setNoeudDestination(this.destination);
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
