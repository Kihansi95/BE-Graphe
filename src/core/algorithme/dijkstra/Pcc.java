package core.algorithme.dijkstra ;

import java.awt.Color;
import java.io.* ;
import java.util.InputMismatchException;
import java.util.Stack;

import base.Readarg ;
import core.Graphe;
import core.algorithme.AbstractPcc;
import core.graphe.Chemin;
import core.graphe.Critere;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;
import exceptions.SuccesseurNonExistantException;

public class Pcc extends AbstractPcc {
	
	// config couleur algo
    public Color couleurSuccesseurVisite()	{	return Color.GREEN;		}
    public Color couleurExplore()	{	return Color.BLUE;	}
    public Color couleurSolution()	{	return Color.DARK_GRAY; 	}
	
    private Noeud noeudOrigine;
    
    private Noeud noeudDestination;
    
    protected Chemin solution;
    
    protected boolean hasSolution;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg) ;
		this.noeudOrigine = null;
		this.noeudDestination = null;
		this.solution = null;

    	int origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
		this.noeudOrigine = this.graphe.getNoeud(origine);
		System.out.println("toto");
		
		// Demander la zone et le sommet destination.
		int destination = readarg.lireInt ("Numero du sommet destination ? ");
		this.noeudDestination = this.graphe.getNoeud(destination) ;
		
		int choice = readarg.lireInt("Votre critere a optimiser : (0) temps (1) distance (2) vitesse optimale pour chaque route\n> ");
		
		if(choice < 0 && choice >= Critere.values().length)
			throw new InputMismatchException("Je ne comprends pas votre critere?");
		this.critere = Critere.values()[choice];
		hasSolution = false;
    }
    
    public Pcc(Graphe gr, PrintStream sortie, Noeud origine, Noeud destination, Critere critere)	{
    	super(gr, sortie, null);
    	this.noeudOrigine = origine;
    	this.noeudDestination = destination;
    	this.critere = critere;
    }
    
    /**
     * Instancier le nouveau label
     * @param sommet
     * @return
     */
    protected Label newLabel(Noeud sommet)	{
    	return new Label(sommet);
    }
    
    public Noeud getOrigine()	{
    	return this.noeudOrigine;
    }
    
    public Noeud getDestination()	{
    	return this.noeudDestination;
    }
    
    public boolean hasSolution()	{
    	return this.hasSolution;
    }
    
    /**
     * get clone de chemin solution, la solution ne peut être modifiée à l'intérieur de l'algo
     * @return
     */
    public Chemin getSolution()	{
    	return new Chemin(solution);
    }
    
    /**
     * Nom de l'algorithme
     */
    public String toString()	{
    	return "Dijkstra";
    }
    
    /**
     * Set le point depart
     * @param noeud
     * @throws SommetNonExisteException
     */
    public void setNoeudOrigine(Noeud noeud) throws SommetNonExisteException	{
    	if(noeud == null || !graphe.isExistant(noeud))
    		throw new SommetNonExisteException();
    	this.noeudOrigine = noeud;
    }
    
    /**
     * Set le point d'arrive
     * @param noeud
     * @throws SommetNonExisteException
     */
    public void setNoeudDestination(Noeud noeud) throws SommetNonExisteException	{
    	if(noeud == null ||!graphe.isExistant(noeud))
    		throw new SommetNonExisteException();
    	this.noeudDestination = noeud;
    }
    
    // --------- Les methodes override
    
    protected void initialize()	{
    	System.out.println("Run "+ this.getClass().getSimpleName() +" de " + this.noeudOrigine + " vers " + this.noeudDestination) ;
		
    	// init label
		Label label_destination = this.newLabel(noeudDestination);
		Label label_origine = this.newLabel(noeudOrigine);
		label_origine.setCout(0f);

		
		this.tas.insert(label_origine);
		this.sommets.put(noeudOrigine,label_origine);
		this.sommets.put(noeudDestination, label_destination);
		
    }
    
    protected boolean conditionContinue()	{
    	return !this.tas.isEmpty() && !this.getLabel(noeudDestination).isMarque();
    }
    
    protected void processing()	{
    	if (noeudOrigine.equals(noeudDestination)){
    		System.out.println("la destination est le point de départ... je ne peux rien faire \n");
    	}
    	else {
    		super.processing();
    	}
    }
    
    protected void terminate()	{
    	
    	Label label_destination = this.getLabel(noeudDestination);
    	Label label_origine = this.getLabel(noeudOrigine);
    	
    	if(!label_destination.isMarque())	{
    		this.hasSolution = false;
			this.solution = buildChemin(this.labelActuel);
			System.out.println("Pas de route de "+label_origine.getSommetCourant() +" vers "+label_destination.getSommetCourant());
			System.out.println("Chemin actuellement trouve : " + this.solution);
			this.solution.dessiner(this.getDessin(), this.graphe.getZone(), this.couleurSolution());
		}	else	{
			this.hasSolution = true;
			this.solution = buildChemin(label_destination);
			System.out.println("Le chemin le plus cours: " + this.solution);
			this.solution.dessiner(this.getDessin(), this.graphe.getZone(), this.couleurSolution());
		}
    	
    	writeDown();
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
			float coutLiaison = Label.calculCout(liaisonOptimal, critere);
			
			if(successeur.getCout() > courant.getCout() + coutLiaison)	{
				successeur.update(courant, liaisonOptimal, this.critere);
			}
	    	
		} catch (SuccesseurNonExistantException e) {
			e.printStackTrace();	// jamais arrivé
		}
		
		return successeur;
    }
    

    
    /**
     * Ecrire la solution dans fichier de sortie.
     * @param solution
     */
    protected void writeDown()	{
    	
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
    		this.sortie.println("Temps d'execution (ms): \t"+getTempsExcec());
    		this.sortie.println("Nb noeud visites :\t\t\t" + getNbVisites());
    		this.sortie.println("Nb noeud marque :\t\t\t" + getnbMarque());
    		this.sortie.println("Nb max dans le tas :\t\t" + getMaxTas());
    	}
		this.sortie.flush();
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
    
}
