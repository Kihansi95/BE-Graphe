package application;

import java.awt.Color;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstramultidest.PccSetLabel;
import core.graphe.Chemin;
import core.graphe.Critere;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Covoiturage {
	
	private Graphe graphe;
	private Voyageur pieton;
	private Voyageur automobile;
	
	private Noeud destination;
	
	private Critere critere;
	
	public Covoiturage(Graphe gr, Readarg readarg) throws SommetNonExisteException {
		
		this.graphe = gr;
		Noeud departPieton = null;
		Noeud departAuto = null;
		
		
		int choice = readarg.lireInt("Choissez votre location par (0) en tapant num de noeud, (1) en cliquant sur la carte\n> ");
		
		switch(choice)	{
			
		case 0:
			int numNoeud = readarg.lireInt("Noeud U1 > ");
			departPieton = this.graphe.getNoeud(numNoeud);
			graphe.getDessin().putText(departPieton.getLongitude(), departPieton.getLatitude(), "U1");
			
			numNoeud = readarg.lireInt("Noeud U2 > ");
			departAuto = this.graphe.getNoeud(numNoeud);
			graphe.getDessin().putText(departAuto.getLongitude(), departAuto.getLatitude(), "U2");
			
			numNoeud = readarg.lireInt("Noeud destination > ");
			this.destination = this.graphe.getNoeud(numNoeud);
			graphe.getDessin().putText(destination.getLongitude(), destination.getLatitude(), "D");
			break;
			
		case 1:
			System.out.println("Cliquez pour U1...");
			departPieton = this.graphe.getNoeudByClick();
			graphe.getDessin().putText(departPieton.getLongitude(), departPieton.getLatitude(), "U1");
			
			System.out.println("Cliquez pour U2...");
			departAuto = this.graphe.getNoeudByClick();
			graphe.getDessin().putText(departAuto.getLongitude(), departAuto.getLatitude(), "U2");
			
			System.out.println("Cliquez pour destination...");
			this.destination = this.graphe.getNoeudByClick();
			graphe.getDessin().putText(destination.getLongitude(), destination.getLatitude(), "D");
			break;
			
		default:
			System.err.println("Selection inconnu");
			throw new RuntimeException();	
		}
		
		int numCritere = readarg.lireInt("Votre critere (0) Temps (1) Distance > ");
		if(numCritere < 0 && numCritere >= Critere.values().length)
			throw new InputMismatchException("Je ne comprends pas votre critere?");
		
		this.pieton = new Voyageur("U1", 20, departPieton);
		this.automobile = new Voyageur("U2", 200, departAuto);
		this.critere = Critere.values()[numCritere];
		
	}
	
	public void run()  	{
	
		try {
			Map<Noeud, Label> reachableFromPieton 		= null;		// labels sauvegardes la trace accessible par U1
			Map<Noeud, Label> reachableFromAuto 		= null;		// labels sauvegardes la trace accessible par U1
			Map<Noeud, Label> reachableFromDestination 	= null;		// labels sauvegardes la trace accessible par Dest (graphe inverse)
			Set<Noeud> pointRdv = null;								// intersect 2 set labels
			PccSetLabel algo = null;								// algo
			
			// cote pieton
			algo = new PccSetLabel(this.graphe, pieton.getDepart(), null, this.critere);
			algo.run();			
			reachableFromPieton = algo.getLabelsMarques();
			
			pointRdv = reachableFromPieton.keySet();
			
			// cote automobile
			algo.clearMarque();
			algo.setNoeudOrigine(automobile.getDepart());
			algo.setDestinations(pointRdv);
			algo.run();			
			reachableFromAuto = algo.getLabelsMarques();

			// intersect 2 zones and update cout to zoneRdv
			pointRdv = intersect(reachableFromPieton, reachableFromAuto);
			
			// Pcc reverse de destination vers le rdv
			this.graphe.reverse();
			algo.setNoeudOrigine(this.destination);
			algo.setDestinations(pointRdv);
			algo.run();
			reachableFromDestination = algo.getLabelsMarques();
			
			// reduce zone touche par U1 et U2
			intersect(reachableFromDestination, reachableFromPieton);
			intersect(reachableFromDestination, reachableFromAuto);
			
			if(reachableFromDestination.isEmpty())	{
				System.out.println("Pas de solution");
				return;
			}
			
			//get point optimal
			float coutMin = Float.MAX_VALUE;
			Label fromDest = null, fromPieton = null, fromAuto = null;
			for(Map.Entry<Noeud, Label> entry: reachableFromDestination.entrySet())	{
				Noeud key = entry.getKey();
				Label pieton = reachableFromPieton.get(key);
				Label auto = reachableFromAuto.get(key);
				float coutActuel = entry.getValue().getCout() + pieton.getCout() + auto.getCout();
				
				if(coutActuel < coutMin)	{
					coutMin = coutActuel;
					fromDest = entry.getValue();
					fromPieton = pieton;
					fromAuto = auto;
				}
			}
			
			// visualize solution
			this.graphe.reverse();
			Chemin cheminDePieton = buildChemin(fromPieton);
			Chemin cheminDeAuto = this.buildChemin(fromAuto);
			Chemin cheminCommun = this.buildCheminCommun(fromDest);
			
			cheminDePieton.dessiner(graphe.getDessin(), graphe.getZone(), Color.GREEN);
			cheminDeAuto.dessiner(graphe.getDessin(), graphe.getZone(), Color.MAGENTA);
			cheminCommun.dessiner(graphe.getDessin(), graphe.getZone(), Color.DARK_GRAY);
			
			System.out.println("De U1 a rdv: "+cheminDePieton);
			System.out.println("De U2 a rdv: "+cheminDeAuto);
			System.out.println("De rdv jusqu'à la destination: "+cheminCommun);
			
			
		} catch (SommetNonExisteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Afficher le chemin grace a Label.
	 * @param destination
	 * @param color
	 */
	private void afficherChemin(Label destination, Color color) {
		for(Label tmp = destination; tmp != null; tmp = tmp.getPere())	{
			tmp.getSommetCourant().dessiner(graphe.getDessin(), color);
			if(tmp.getLiaison() != null) tmp.getLiaison().dessiner(graphe.getDessin(), this.graphe.getZone(),color );
		}
	}

	private Chemin buildChemin(Label destination)	{
    	
    	Stack<Liaison> tmp = new Stack<Liaison>();
    	
    	while(destination.getLiaison() != null)	{
    		tmp.push(destination.getLiaison());
    		destination = destination.getPere();
    	}
    	
    	Chemin chemin = new Chemin(destination.getSommetCourant());
    	while(!tmp.isEmpty()) chemin.addRoute(tmp.pop());
    	
    	return chemin;
    }
	
	private Chemin buildCheminCommun(Label rdv)	{
		Chemin chemin = new Chemin(rdv.getSommetCourant());
		for(Label tmp = rdv.getPere(); tmp!= null; tmp = tmp.getPere())	{
			chemin.addSommet(tmp.getSommetCourant(), this.critere);
		}
		return chemin;
	}
	
	/**
	 * Pour chaque ensemble ne garde que les labels possede le noeud commun.
	 * @param set1
	 * @param set2
	 * @return les noeuds dans l'intersection.
	 */
	private Set<Noeud> intersect(Map<Noeud, Label> set1, Map<Noeud, Label> set2)	{
		
		set1.keySet().retainAll(set2.keySet());
		set2.keySet().retainAll(set1.keySet());
		
		return set1.keySet();
	}

}
