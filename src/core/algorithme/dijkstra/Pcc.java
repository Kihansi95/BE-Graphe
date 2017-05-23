package core.algorithme.dijkstra ;

import java.awt.Color;
import java.io.* ;
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
import core.graphe.Critere;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;
import exceptions.SuccesseurNonExistantException;

public class Pcc extends Algo {
	
	// config couleur algo
    public Color couleurSuccesseurVisite()	{	return Color.GREEN;		}
    public Color couleurExplore()	{	return Color.BLUE;	}
    public Color couleurSolution()	{	return Color.DARK_GRAY; 	}
	
    private Noeud noeudOrigine;
    
    private Noeud noeudDestination;
    
    private Chemin solution;
	
    /**
     * Dictionnaire de Noeud - Label, empty si run n'est pas appelé
     */
    private HashMap<Noeud, Label> sommets;
    
    protected Critere critere;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg) ;
		this.noeudOrigine = null;
		this.noeudDestination = null;
		this.solution = null;

		int origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
		this.noeudOrigine = gr.getNoeud(origine);

		// Demander la zone et le sommet destination.
		int destination = readarg.lireInt ("Numero du sommet destination ? ");
		this.noeudDestination = gr.getNoeud(destination) ;
		
		int choice = readarg.lireInt("Votre critere a optimiser : (0) temps (1) distance (2) vitesse optimale pour chaque route\n> ");
		
		if(choice < 0 && choice >= Critere.values().length)
			throw new InputMismatchException("Je ne comprends pas votre critere?");
		this.critere = Critere.values()[choice];
		
		sommets = null;
    }
    
    public Noeud getOrigine()	{
    	return this.noeudOrigine;
    }
    
    public Noeud getDestination()	{
    	return this.noeudDestination;
    }
    
    /**
     * get clone de chemin solution, la solution ne peut être modifiée à l'intérieur de l'algo
     * @return
     */
    public Chemin getSolution()	{
    	return new Chemin(solution);
    }
    
    /**
     * Instancier le nouveau label
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
		
		// INIT ALGORITHME
		
		// init perfomance
		long startTime = System.currentTimeMillis();
		int nbVisites = 0; 
		int nbMarque = 0; 
		int maxTas = 0;
	
		// init label
		this.sommets = new HashMap<Noeud, Label>();
		BinaryHeap<Label> visites = new BinaryHeap<Label>();
		Label label_destination = this.newLabel(noeudDestination);
		Label label_origine = this.newLabel(noeudOrigine);
		label_origine.setCout(0f);

		
		visites.insert(label_origine);
		sommets.put(noeudOrigine,label_origine);
		sommets.put(noeudDestination, label_destination);

		if (noeudOrigine.equals(noeudDestination)){
			System.out.println("la destination est le point de départ... je ne peux rien faire \n");
		
		}
		else {
			List<Noeud> successeurs ;
			Label label_actuel = null ;
	
			// PROCEDURE ALGORITHME
			// TODO : changer la consition du while : tas vide
			while(!visites.isEmpty() && !label_destination.isMarque())	{
				
				label_actuel = visites.deleteMin() ; // au 1er while on delete l'origine
				label_actuel.marquer();
				nbMarque++;	
				
				successeurs = label_actuel.getSommetCourant().getSuccesseurs();
				
			
				// visiter chaque successeur
				for(Noeud succ : successeurs){
					Label label_succ ;
					if (!sommets.containsKey(succ)){
					label_succ = this.newLabel(succ);
					sommets.put(succ, label_succ);
					}
					else {
						label_succ = sommets.get(succ);
					}
					if(!label_succ.isMarque())	{					
						updateSuccesseur(label_succ, label_actuel);
						if(visites.existe(label_succ))
							visites.update(label_succ);
						else
							visites.insert(label_succ);
						nbVisites++;
						label_succ.getSommetCourant().dessiner(this.getDessin(), this.couleurSuccesseurVisite());
					}
				}
				
				// fin de la visite du sommet actuel, marquer et place dans la solution
				label_actuel.getSommetCourant().dessiner(this.getDessin(), this.couleurExplore()); 
	
				if(maxTas < visites.size())
					maxTas = visites.size();
		
		}	
		
		// FIN ALGORITHME
		
		if(label_destination.isMarque())	{
			this.solution = buildChemin(label_actuel);
			System.out.println("Pas de route de "+label_origine.getSommetCourant() +" vers "+label_destination.getSommetCourant());
			System.out.println("Chemin actuellement trouve : " + this.solution);
			this.solution.dessiner(getDessin(), this.graphe.getZone(), this.couleurSolution());
		}	else	{
			this.solution = buildChemin(label_destination);
			System.out.println("Le chemin le plus cours: " + this.solution);
			this.solution.dessiner(getDessin(), this.graphe.getZone(), this.couleurSolution());
		}
		
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

		Liaison liaisonOptimal;
		try {
			liaisonOptimal = courant.getSommetCourant().getLiaisonOptimal(successeur.getSommetCourant(), this.critere);
			if(successeur.getCout() > courant.getCout() + liaisonOptimal.getLongueur())	{
				successeur.update(courant, liaisonOptimal, this.critere);
			}
	    	
		} catch (SuccesseurNonExistantException e) {
			e.printStackTrace();	// jamais arrivé
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
    		
    	}	else if(!this.solution.getDestinataire().equals(this.noeudDestination))	{
    		
    		this.sortie.println("Chemin trouvé: ");
    		this.sortie.println(this.solution);
    		
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
    
    /*******************************************************
     * 
     * Le services suivantes sont en protected: Reserve pour
     * les classes filles.
     * 
     ********************************************************/
    
    protected void setNoeudOrigine(int numNoeud) throws SommetNonExisteException	{
    	Noeud noeud = this.graphe.getNoeud(numNoeud);
    	this.noeudOrigine = noeud;
    }
    
    protected void setNoeudDestination(int numNoeud) throws SommetNonExisteException	{
    	Noeud noeud = this.graphe.getNoeud(numNoeud);
    	this.noeudDestination = noeud;
    }
    
    /**
     * Initialiser argument de l'utilisateur. Chaque algo va avoir son propre input
     * @param readarg
     * @throws Exception 
     */
    protected void initUserChoice(Readarg readarg) throws Exception	{
		int destination = readarg.lireInt ("Numero du sommet destination ? ");
		this.noeudDestination = this.graphe.getNoeud(destination) ;
		
		int choice = readarg.lireInt("Votre critere a optimiser : (0) temps (1) distance (2) vitesse optimale pour chaque route\n> ");
		
		if(choice < 0 && choice >= Critere.values().length)
			throw new InputMismatchException("Je ne comprends pas votre critere?");
		this.critere = Critere.values()[choice];
    }
}
