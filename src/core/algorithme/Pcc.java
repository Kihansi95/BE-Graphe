package core.algorithme ;

import java.awt.Color;
import java.io.* ;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import base.BinaryHeap;
import base.Dessin;
import base.Readarg ;
import core.Algo;
import core.Graphe;
import core.graphe.Chemin;
import core.graphe.Critere;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Pcc extends Algo {

    private Noeud noeudOrigine;
    
    private Noeud noeudDestination;
    
    private Chemin solution;
	
    /**
     * Dictionnaire de Noeud - Label, empty si run n'est pas appelé
     */
    private HashMap<Noeud, Label> sommets;
    
    protected Critere critere;

    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		this.noeudOrigine = null;
		this.noeudDestination = null;
		this.solution = null;
		
		try {
			int origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
			this.noeudOrigine = gr.getNoeud(origine);

			// Demander la zone et le sommet destination.
			int destination = readarg.lireInt ("Numero du sommet destination ? ");
			this.noeudDestination = gr.getNoeud(destination) ;
			
			int choice = readarg.lireInt("Votre critere a optimiser : (0) temps (1) distance (2) vitesse optimale pour chaque route ");
			
			if(choice < 0 && choice >= Critere.values().length)
				throw new InputMismatchException("Je ne comprends pas votre critere?");
			this.critere = Critere.values()[choice];
			
		} catch (SommetNonExisteException e) {
			System.err.println("Vous essayer de choisir un sommet n'existe pas sur la carte.");
		}
		
		sommets = null;
    }
    
    public Noeud getOrigine()	{
    	return this.noeudOrigine;
    }
    
    public Noeud getDestination()	{
    	return this.noeudDestination;
    }
    
    /**
     * get clone de chemin solution, la solution ne peut être modifié à l'intérieur de l'algo
     * @return
     */
    public Chemin getSolution()	{
    	return new Chemin(solution);
    }
    
    /**
     * Instancier le nouvel label
     * @param sommet
     * @return
     */
    protected Label newLabel(Noeud sommet)	{
    	return new Label(sommet);
    }
    
    /**
     * Get label depuis le dictionnaire
     * @param noeud
     * @return
     */
    protected Label getLabel(Noeud noeud)	{
    	return this.sommets.get(noeud);
    }
    
    /**
     * nom de l'algorithme
     */
    public String toString()	{
    	return "Dijkstra";
    }

    public void run() {

    	System.out.println("Run "+ this.getClass().getSimpleName() +" de " + this.noeudOrigine + " vers " + this.noeudDestination) ;
		this.graphe.dessiner();
		
		// init perfomance
		long startTime = System.currentTimeMillis();
		int nbVisites = 0; 
		int nbMarque = 0; 
		int maxTas = 0;
	
		// init label
		this.sommets = new HashMap<Noeud, Label>();
		BinaryHeap<Label> visites = new BinaryHeap<Label>();
		Label label_destination = null;
		Label label_origine = null;

		for(Noeud noeud : this.graphe.getNoeuds())	{
			Label label = this.newLabel(noeud);
			sommets.put(noeud, label);
			if(noeud.equals(this.noeudDestination))
				label_destination = label;
			if(noeud.equals(this.noeudOrigine))	{
				label_origine = label;
				label.setCout(0f);
			}
		}
		
		if(label_destination == null)
			throw new RuntimeException("Sommet destination non existant");
		if(label_origine == null)
			throw new RuntimeException("Sommet origine non existant");
		
		// init algo
		Label label_actuel = this.getLabel(this.noeudOrigine); // pop the origine;
		
		// algo procédure
		while(!label_actuel.equals(label_destination))	{
			List<Noeud> successeurs = label_actuel.getSommetCourant().getSuccesseurs();
			
			// si non successeurs: no route
			if(successeurs.isEmpty())
				break;
			
			// visiter chaque successeur
			for(Noeud succ : successeurs)	{		
				Label label_succ = getLabel(succ);
				if(!label_succ.isMarque())	{					
					updateSuccesseur(label_succ, label_actuel);
					visites.insert(label_succ);
					nbVisites++;
					label_succ.getSommetCourant().dessiner(this.getDessin(), Color.GREEN);
				}
			}
			
			// fin de la visite du sommet actuel, marquer et place dans la solution
			label_actuel.marquer();
			nbMarque++;
			label_actuel.getSommetCourant().dessiner(this.getDessin(), Color.BLUE); 

			// label suivant
			if(maxTas < visites.size())
				maxTas = visites.size();
			label_actuel = visites.deleteMin();
			
		}	
		
		this.solution = buildChemin(label_actuel);
		
		if(!label_actuel.equals(label_destination))	{
			System.out.println("Pas de route de "+label_origine.getSommetCourant() +" vers "+label_destination.getSommetCourant());
		}	else	{
			System.out.println("Le chemin le plus cours: "+this.solution);
			solution.dessiner(getDessin(), this.graphe.getZone(), Color.DARK_GRAY);
		}
		
		writeDown(nbVisites, nbMarque, maxTas, System.currentTimeMillis() - startTime);
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
    protected void writeDown(int nbVisites, int nbMarque, int maxTas, long tempsExec)	{
    	
    	this.sortie.println(">>> Algorithme "+this +" de "+this.noeudOrigine+ " vers "+this.noeudDestination +" <<<\n");
    	
    	if(this.solution == null)	{
    		this.sortie.println("Solution n'existe pas ou run() n'est pas appellé");
    	}	else	{
    		this.sortie.println("Solution de Dijkstra de "+ this.noeudOrigine + " vers " + this.noeudDestination);
    		this.sortie.println(this.solution);
    		this.sortie.println("=============================================================================================");
    		this.sortie.println("Performance: ");
    		this.sortie.println("Temps d'execution (ns): \t"+tempsExec);
    		this.sortie.println("Nb noeud visites :\t\t\t" + nbVisites);
    		this.sortie.println("Nb noeud marque :\t\t\t" + nbMarque);
    		this.sortie.println("Nb max dans le tas :\t\t" + maxTas);
    	}
		this.sortie.flush();
    }

}
