package core.algorithme ;

import java.awt.Color;
import java.io.* ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JSpinner.ListEditor;

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
 
    
    protected boolean choix_tps_dist = false; // par defaut en distance ;
	protected BinaryHeap<LabelBis> tas ;
	protected Chemin chemin_recherche ;
	protected Chemin chemin_final;
	protected Color coul_recherche = Color.GREEN ;
	protected Color coul_chemin = Color.BLUE ;
	protected HashMap<Noeud,LabelBis> assoc ;

    
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;

	this.zoneOrigine = gr.getZone () ;
	try {
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone (this.origine) ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		this.zoneDestination = gr.getZone(this.destination);
		int entier = readarg.lireInt("tapez 1 pour un calcul avec temps plus court, ou un autre entier pour une distance plus courte :");
		boolean choix ;
		if (entier == 1)
			choix = true ;
		else {
			choix = false ; 
			}
		this.choix_tps_dist = choix ;
		} catch (SommetNonExisteException e)
		{
			System.err.println("vous avez choisi un sommet hors de la port�e de la carte" );
		}
    }
    
    

    public void run() {
    	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
    	// A vous d'implementer la recherche de plus court chemin.
    	
    	this.initialiser() ;
    	
    	this.parcours();
    	
    	this.afficher_resultat();
    }

	
	
	/**
	 * initialisation de dijkstra
	 */
	public void initialiser(){
		// LES VARIABLES
		//...BinaryHeap<Label> tas = new BinaryHeap<Label>() ;
		tas = new BinaryHeap<LabelBis>() ;
		//...HashMap<Noeud,Label> assoc = new HashMap<Noeud, Label>();
		assoc = new HashMap<Noeud, LabelBis>();
		// c'est dans la HashMap qu'on m.a.j. le cout, le marquage, ... pour chaque sommets.

		//Noeud sorigine = liste_sommets.get(origine);
		//Noeud sdestination = liste_sommets.get(destination);
		//...Label laborigine = null ;
		//...Label labdestination = null ;	
		LabelBis laborigine = null ;
		LabelBis labdestination = null ;	

		// AFFICHAGE SI MODE TEMPS OU DISTANCE 
		if (choix_tps_dist){
			System.out.println("_____________Dijkstra fait en fonction du temps____________");
		}
		else {
			System.out.println("_____________Dijkstra fait en fonction de la distance____________");
		}
		
		// INIT 
		for (Noeud sommet : this.graphe.getNoeuds() ){
			//...Label lab = new Label(sommet);
			double heuristique = setEstimation(sommet.getNumero());
			LabelBis lab = new LabelBis(false, Float.MAX_VALUE , -2, sommet.getNumero(), heuristique);
			// a la creation du label, marquage a false, pere -2 et cout fixe a max value
			assoc.put(sommet,lab);
			if (sommet.getNumero() == this.destination){
				labdestination = lab ;
			}
			if (sommet.getNumero() == this.origine){
				// on set le label de l'origine : cout et absence de pere
				// on insert le label de l'origine dans le tas
				lab.setCout(0f);
				lab.setPere(-1);
				laborigine = lab ;
				tas.insert(laborigine);
			}
		}
		if(labdestination == null)
			throw new RuntimeException("Sommet destination not found, à vérifier l'algo");
		if(laborigine == null)
			throw new RuntimeException("Sommet origine not found, à vérifier l'algo");
	}
	/**
	 * set estimation (pour que ce soit facile dans PCCstar
	 * @param sommet
	 * @return 0 par défaut dans dijkstra standard
	 */
	protected double setEstimation(int sommet){
		return 0;
	}
	/**
	 * le parcours de dijkstra standard
	 */
	public void parcours(){

    	// ITERATION ( parcours)
    	boolean destination_atteinte = false ;
    	//...	Label courant = null ;
    	LabelBis courant = null ;
		Chemin chemin_recherche = new Chemin();

    	// test si origine == dest
    	if (origine == destination){
    		System.out.println("Sorry, you already made it to your destination. I can't do anything for you...\n");
    	}
    	else {
    		ArrayList<Liaison> routes_vers_voisins = new ArrayList<Liaison>(); ;
    		//...Label lab_next ;
    		LabelBis lab_next ;
    		while (!tas.isEmpty() && !destination_atteinte){ 
    			courant = tas.deleteMin();
    			Noeud ajout = graphe.getNoeudInt(courant.getSommetCourant());
    			chemin_recherche.addSommet(ajout); 
    			// on ajoute le sommet au tas et on le marque
			
    			courant.setMarquage(true);
    			if (courant.getSommetCourant() == destination){
    				destination_atteinte = true ; 
    			}
    			routes_vers_voisins = ajout.getLiaisons();
    			// on regarde tous les successeurs 
    			for ( Liaison rt : routes_vers_voisins){	
    				lab_next = assoc.get(rt.getSuccesseur());// on recupere le label
    				// CONDITION POUR DIJKSTRA
    				float coutAux;
    				if (!lab_next.getMarquage()){ // si marquage false
    					//suivant le mode choisi 
    					updatecout(choix_tps_dist, courant, rt, lab_next);
    					chemin_recherche.addSommet(graphe.getNoeudInt(lab_next.getSommetCourant()));
    					chemin_recherche.addRoute(rt);
    				}
    					// else on garde le chemin le plus court ou égal déja choisi (choix)
    			}
    		}// fin FOR
    	}//fin WHILE
    		
    	System.out.println( "destination atteinte : " + destination_atteinte + "\n");
    	Dessin dessin = this.graphe.getDessin();
    	   dessin.setWidth(3);
    	chemin_recherche.dessiner(dessin, graphe.getZone(), coul_recherche);
    	// ACTUALISER LE CHEMIN SI le label courant est la destination :
    	if (courant.getSommetCourant() == destination){		
    		actualiser_chemin();	
    	}
    }
    	

	/**
	 * Pour fabriquer le chemin final à partir du label de la destination
	 */
		
	private void actualiser_chemin(){
		chemin_final = new Chemin();
		// en partant de la destination et en retournant a l'envers grace au pere !!!
		int position = destination ;
		//...Label lab_pos = null;
		LabelBis lab_pos = null;
		Noeud sommet_pere = null ;
		Noeud sommet_pos = null;	
		Liaison liaison_aux = null;
		while(position!=origine){
			lab_pos = assoc.get(graphe.getNoeudInt(position));
			sommet_pos = graphe.getNoeudInt(position) ;
			sommet_pere = graphe.getNoeudInt(lab_pos.getPere());
			liaison_aux = sommet_pere.getLiaisonOptimal(sommet_pos);
			chemin_final.addRoute(liaison_aux);
			chemin_final.addSommet(sommet_pos);
			// on remonte sur le noeud precedent
			position = lab_pos.getPere();
		}
		// enfin on ajoute le noeud origine 
		// la liaison vers l'origine a deja ete ajoutee
		chemin_final.addSommet( graphe.getNoeudInt(origine));
		chemin_final.reverse();
	}
		
	/**
	* Pour afficher le chemin sur le graphe
	*/
	protected void afficher_resultat(){
		Dessin dessin = this.graphe.getDessin();
	    dessin.setWidth(3);
		System.out.println(" === Longueur du chemin final: "+chemin_final.getDistanceTotale()+" metres");
		System.out.println(" === temps du chemin final: "+chemin_final.getTempsTotal()+" minutes");
		chemin_final.dessiner(dessin, graphe.getZone(), coul_chemin);
	}
	
    /**
     * calcul le cout pour dijkstra
     * @param choix_tps_dist
     * @param courant
     * @param rt
     * @return coutAux
     */
    //...private float updatecout( boolean choix_tps_dist, Label courant, Liaison rt){
    protected void updatecout( boolean choix_tps_dist, LabelBis courant, Liaison rt, LabelBis lab_next){
    	float coutAux ;
    	if (choix_tps_dist){ // en temporel 
			coutAux = courant.getCout() + rt.coutRoute(choix_tps_dist);
		}
		else{ // en distance 
			coutAux = courant.getCout()+rt.coutRoute(choix_tps_dist);
		}
    	// on compare l'ancien et le nouveau coup 
		if (coutAux < lab_next.getCout()){
			// on Met a jour le label actuel  :
			lab_next.setCout(coutAux);
			lab_next.setPere(courant.getSommetCourant());
			// puis on l'insere ou l'actualise si deja present dans le tas
			if (tas.existe(lab_next)){ 
				tas.update(lab_next);
			}
			else{
				tas.insert(lab_next);
			}
		}
    }
    
    public Chemin getCheminRecherche(){
    	return this.chemin_recherche ;
    }
}
