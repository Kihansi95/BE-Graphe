package core.algorithme;

import java.awt.Color;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BinaryHeap;
import base.Dessin;
import base.Readarg;
import core.Algo;
import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.graphe.Critere;
import core.graphe.Noeud;
import exceptions.CheminNullException;

/**
 * La classe qui conserve le principe de Dijkstra. Chaque version de Pcc va modifier en fonction de son besoin mais pas cette classe
 * @author Duc Hau NGUYEN, Anais RABARY
 */
public abstract class AbstractPcc extends Algo {
	
    /**
     *  Dictionnaire de Noeud - Label, empty si run n'est pas appele
     */
    protected Map<Noeud, Label> sommets;
    
    /**
     * Critere d'optimisation de la route
     */
    protected Critere critere;
    
    /**
     * Temps d'excecution en ms
     */
	private long tempsExec = 0;
	
	/**
	 * Nombre de successeur visité
	 */
	private int nbVisites = 0; 
	
	/**
	 * Nombre de sommet visité
	 */
	private int nbMarque = 0; 
	
	/**
	 * Nombre de sommet maximal dans le tas
	 */
	private int maxTas = 0;
	
	protected AbstractPcc(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);
		this.sommets = null;
	}
	
	// abstract: need to s'adapter a chaque version
	
	/**
	 * Declare version de label nécessaire pour le fonctionnement
	 * @param sommet
	 * @return core.algorithme.dijkstra.Label
	 */
	abstract protected Label newLabel(Noeud sommet);
	
	/**
	 * Callback avant processing
	 */
	abstract protected void initialize() ;
	
	/**
	 * Callback apres processing
	 */
	abstract protected void terminate();
	
	// configuration de couleur
	abstract protected Color couleurSuccesseurVisite();
	abstract protected Color couleurExplore();
	
	/**
	 * L'invserse de condition d'arrêt
	 * @return boolean false pour arrêt le parcours
	 */
	abstract protected boolean conditionContinue();
	
	abstract protected Label updateSuccesseur(Label successeur, Label courant);
	
	public long getTempsExcec()	{ return tempsExec; }
	public long getNbVisites() {	return nbVisites; }
	public int getnbMarque() { return nbMarque; }
	public int getMaxTas() { return maxTas; }
	
	public Label getLabel(Noeud noeud)	{
		return sommets.get(noeud);
	}
	
    public Map<Noeud, Label> getSommets()	{
    	return new HashMap<Noeud, Label>(sommets);
    }
    
    public void clearMarque()	{
    	for(Map.Entry<Noeud, Label> entry : this.sommets.entrySet())	{
    		entry.getValue().setMarquage(false);
    	}
    }

    protected Label labelActuel;
    protected  BinaryHeap<Label> tas; 
    
	public void run()	{
		
		long startTime = System.currentTimeMillis();
		this.nbVisites = 0; 
		this.nbMarque = 0; 
		this.maxTas = 0;
		
		this.sommets = new HashMap<Noeud, Label>();
		this.tas = new BinaryHeap<Label>();
		
		initialize();
		try {
		processing();
		this.tempsExec = System.currentTimeMillis() - startTime;
		terminate();
		} catch (CheminNullException e){
			System.err.println("la destination est le point de depart... je ne peux rien faire \n");
			System.exit(1);
		}   catch (NullPointerException e2){
			System.err.println("pointeur null, je m'arr�te");
			System.exit(1);
		}
		
		
	}
	
	protected void processing() throws CheminNullException	{
		this.labelActuel = null ;
		
		// PROCEDURE ALGORITHME
		while(this.conditionContinue())	{
			
			labelActuel = tas.deleteMin() ; // au 1er while on delete l'origine
			labelActuel.setMarquage(true);
			this.nbMarque++;	
			
			List<Noeud> successeurs = labelActuel.getSommetCourant().getSuccesseurs();
			
		
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
					updateSuccesseur(label_succ, labelActuel);
					if(tas.existe(label_succ))
						tas.update(label_succ);
					else
						tas.insert(label_succ);
					this.nbVisites++;
					label_succ.getSommetCourant().dessiner(this.getDessin(), this.couleurSuccesseurVisite());
				}
			}
			
			// fin de la visite du sommet actuel
			labelActuel.getSommetCourant().dessiner(this.getDessin(), this.couleurExplore()); 
			if(maxTas < tas.size())
				maxTas = tas.size();
		}
	}
	
    /**
     * Shortcut dessin
     * @return
     */
    protected Dessin getDessin()	{
    	return this.graphe.getDessin();
    }
	
}
