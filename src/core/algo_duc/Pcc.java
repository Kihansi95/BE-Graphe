package core.algo_duc ;

import java.awt.Color;
import java.io.* ;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

import base.BinaryHeap;
import base.Dessin;
import base.Readarg ;
import core.Algo;
import core.Graphe;
import core.graphe.Liaison;
import core.graphe.Noeud;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
        
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
	
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
	
		// init label
		Chemin solution;
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
			if(noeud.getNumero() == this.origine)
				label_origine = label;
		}
		
		if(label_destination == null)
			throw new RuntimeException("Sommet destination not found, à vérifier l'algo");
		if(label_origine == null)
			throw new RuntimeException("Sommet origine not found, à vérifier l'algo");
		
		// init algo
		Label label_actuel = label_origine;
		solution = new Chemin(label_actuel.getSommetCourant());
		
		// algo procédure
		while(label_actuel != label_destination)	{
			List<Noeud> successeurs = label_actuel.getSommetCourant().getSuccesseurs();
			
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
			solution.addRoute(label_actuel.getLiaison());
			label_actuel.getSommetCourant().dessiner(this.getDessin(), Color.DARK_GRAY); 

			
			// label suivant
			label_actuel = visites.findMin();
		}	
		
    }
    
    /**
     * Mettre à jour le successeur par rapport à sommet courant. 
     * @param successeur
     * @param courant
     * @return
     */
    private Label updateSuccesseur(Label successeur, Label courant)	{
    	if(successeur.getPere() == null)	{
    		successeur.setPere(courant);
    		successeur.setCout(0f);
    	}
    	else	{
    		Liaison liaisonOptimal = Chemin.getLiaisonOptimal(courant.getSommetCourant(), successeur.getSommetCourant());
    		if(successeur.getCout() > courant.getCout() + liaisonOptimal.getLongueur())	{
    			successeur.update(courant, liaisonOptimal);
    		}
    	}
    	return successeur;
    }
    
    /**
     * Shortcut dessin
     * @return
     */
    private Dessin getDessin()	{
    	return this.graphe.getDessin();
    }

}
