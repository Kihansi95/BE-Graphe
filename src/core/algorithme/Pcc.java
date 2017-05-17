package core.algorithme ;

import java.awt.Color;
import java.io.* ;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;

import base.BinaryHeap;
import base.Dessin;
import base.Readarg ;
import core.Algo;
import core.Graphe;
import core.graphe.Chemin;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    protected Critere critere;
    
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
	
		try {
			this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
			this.zoneOrigine = gr.getZone (this.origine) ;

			// Demander la zone et le sommet destination.
			this.destination = readarg.lireInt ("Numero du sommet destination ? ");
			this.zoneDestination = gr.getZone (this.destination) ;
			
			int choice = readarg.lireInt("Votre critere a optimiser : (0) temps (1) distance (2) vitesse optimale pour chaque route ");
			
			if(zoneOrigine != this.graphe.getZone() || zoneDestination != this.graphe.getZone())
				throw new SommetNonExisteException();
			
			if(choice < 0 && choice >= Critere.values().length)
				throw new InputMismatchException("Je ne comprends pas votre critere?");
			this.critere = Critere.values()[choice];
			
		} catch (SommetNonExisteException e) {
			System.err.println("Vous essayer de choisir un sommet n'existe pas sur la carte.");
		}
    }

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
		this.graphe.dessiner();
	
		// init label
		AbstractMap<Noeud, Label> sommets = new HashMap<Noeud, Label>();
		BinaryHeap<Label> visites = new BinaryHeap<Label>();
		Label label_destination = null;
		Label label_origine = null;

		for(Noeud noeud : this.graphe.getNoeuds())	{
			Label label = new Label(noeud);
			sommets.put(noeud, label);
			visites.insert(label);
			if(noeud.getNumero() == this.destination)
				label_destination = label;
			if(noeud.getNumero() == this.origine)	{
				label_origine = label;
				label.setCout(0f);
				visites.update(label);
			}
		}
		
		if(label_destination == null)
			throw new RuntimeException("Sommet destination not found, à vérifier l'algo");
		if(label_origine == null)
			throw new RuntimeException("Sommet origine not found, à vérifier l'algo");
		
		// init algo
		Label label_actuel = visites.deleteMin(); // pop the origine;
		
		// algo procédure
		while(!label_actuel.equals(label_destination))	{
			List<Noeud> successeurs = label_actuel.getSommetCourant().getSuccesseurs();
			
			// si non successeurs: no route
			if(successeurs.isEmpty())
				break;
			
			// visiter chaque successeur
			for(Noeud succ : successeurs)	{		
				Label label_succ = sommets.get(succ);
				if(!label_succ.isMarque())	{					
					updateSuccesseur(label_succ, label_actuel);
					visites.update(label_succ);
					label_succ.getSommetCourant().dessiner(this.getDessin(), Color.GREEN);
				}
			}
			
			// fin de la visite du sommet actuel, marquer et place dans la solution
			label_actuel.marquer();
			label_actuel.getSommetCourant().dessiner(this.getDessin(), Color.ORANGE); 

			// label suivant
			label_actuel = visites.deleteMin();
		}	
		
		Chemin solution = buildChemin(label_actuel);
		
		if(!label_actuel.equals(label_destination))	{
			System.out.println("Pas de route de "+label_origine.getSommetCourant() +" vers "+label_destination.getSommetCourant());
		}	else	{
			System.out.println("Le chemin le plus cours: "+solution);
			solution.dessiner(getDessin(), this.graphe.getZone(), Color.DARK_GRAY);
		}
		
		writeDown(solution);
    }
    
    /**
     * Reconstruire le Chemin grace a la notion de label
     * @param destination: Le dernier label apres l'algo
     * @return Chemin: chemin de solution
     */
    protected Chemin buildChemin(Label destination)	{
    	
    	Stack<Liaison> tmp = new Stack<Liaison>();
    	
    	while(destination.getLiaison() != null)	{
    		tmp.push(destination.getLiaison());
    		destination = destination.getPere();
    	}
    	
    	Chemin chemin = new Chemin(destination.getSommetCourant());
    	while(!tmp.isEmpty()) chemin.addRoute(tmp.pop());
    	
    	return chemin;
    }
    
    /**
     * Mettre à jour le successeur par rapport à sommet courant. 
     * @param successeur
     * @param courant
     * @return
     */
    protected Label updateSuccesseur(Label successeur, Label courant)	{

		Liaison liaisonOptimal = Chemin.getLiaisonOptimal(courant.getSommetCourant(), successeur.getSommetCourant(), this.critere);
		if(successeur.getCout() > courant.getCout() + liaisonOptimal.getLongueur())	{
			successeur.update(courant, liaisonOptimal, this.critere);
		}
    	
    	return successeur;
    }
    
    /**
     * Shortcut dessin
     * @return
     */
    protected Dessin getDessin()	{
    	return this.graphe.getDessin();
    }
    
    /**
     * Ecrire la solution dans fichier de sortie.
     * @param solution
     */
    protected void writeDown(Chemin solution)	{
    	this.sortie.println("Solution de Dijkstra de "+zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination);
    	this.sortie.println(solution);
		this.sortie.flush();
    }

}
