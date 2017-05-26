package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstramultidest.LabelCovoiturage;
import core.algorithme.dijkstramultidest.PccSetLabel;
import core.graphe.Critere;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Covoiturage {
	
	private Graphe graphe;
	private Voyagueur pieton;
	private Voyagueur automobile;
	
	private Noeud destination;
	
	private Critere critere;
	
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
		
		int numCritere = readarg.lireInt("Votre critere (0) Temps (1) Distance > ");
		if(numCritere < 0 && numCritere >= Critere.values().length)
			throw new InputMismatchException("Je ne comprends pas votre critere?");
		this.critere = Critere.values()[numCritere];
		
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
			// TODO check if retainAll will use equals() method or use directly the specified @
			List<Label> zoneRdv = null;
			
			// Dijkstra les noeuds
			PccSetLabel algo = new PccSetLabel(this.graphe, pieton.getDepart(), null, this.critere);
			algo.run();
			
			zoneRdv = algo.getLabelsMarques();
			List<Noeud> pietonReach = new ArrayList<Noeud>();
			
			for(Label point: zoneRdv){
				pietonReach.add(point.getSommetCourant());
			}
			
			algo.clearMarque();
			algo.setNoeudOrigine(automobile.getDepart());
			algo.setDestinations(pietonReach);
			algo.run();
			
			
			List<Label> zoneAutomobile = algo.getLabelsMarques();

			// intersect 2 zones and update cout to zoneRdv
			List<Noeud> noeudsRdv = new ArrayList<Noeud>();
			
			for(Label point1: zoneRdv)		{
				
				boolean updated = false;
				
				// if we find the common label: update cout and quit the loop
				for(Label point2: zoneAutomobile)	{
					
					if(point1.equals(point2))	{
						float coutUpdate = point1.getCout() + point2.getCout();
						point1.setCout(coutUpdate);
						updated = true;
						noeudsRdv.add(point1.getSommetCourant());
						break;
					}
					
				}
				
				// not updated == not common between set
				if(!updated)	{
					zoneRdv.remove(point1);
				}
				
			}
			
			// Pcc reverse de destination vers le rdv
			this.graphe.reverse();
			algo.setNoeudOrigine(this.destination);
			algo.setDestinations(noeudsRdv);
			algo.run();
			
			// get le point rdv le plus optimiser
			List<Label> zoneRdvFromDestination = algo.getLabelsMarques();
			
			for(Label point1: zoneRdv)		{
				
				boolean updated = false;
				
				// if we find the common label: update cout and quit the loop
				for(Label point2: zoneRdvFromDestination)	{
					
					if(point1.equals(point2))	{
						float coutUpdate = point1.getCout() + point2.getCout();
						point1.setCout(coutUpdate);
						updated = true;
						noeudsRdv.add(point1.getSommetCourant());
						break;
					}
					
				}
				
				// not updated == not common between set
				if(!updated)	{
					zoneRdv.remove(point1);
				}
				
			}
			
			
			
		} catch (SommetNonExisteException e) {
			e.printStackTrace();
		}

	}

}
