package core.algorithme ;

import java.awt.Color;
import java.io.* ;

import base.Readarg ;
import core.Graphe;
import core.graphe.Liaison;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    	this.coul_recherche = Color.CYAN ;
    	this.coul_chemin = Color.BLACK;
    	this.choix_tps_dist = false ;

    }
    
    public void run() {

	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
	
	// A vous d'implementer la recherche de plus court chemin A*
	/**
	 * On fera toujours A* en fonction de la distance. En effet, pour le mode temps, 
	 * il faudrait faire un temps estimé à  vol d'oiseau, chose assée difficile.
	 */
	System.out.println("------------------------PCC STAR ------------------\n");
	this.initialiser();
	
	this.parcours();
	
	//this.afficher_resultat();
    }


	// on init avec la méthode de PCC, 
    // on veut juste setter l'estimation différemment.
	/**
	 *  surcharge de méthode set_estimation dans pcc_star
	 * @param numero_sommet
	 * @return le cout de l'estimation en Float
	 */
	protected double setEstimation(int sommet ){
		double estimation = graphe.distance_sommets(sommet, destination) ;
		// TODO : Ask prof comment faire estimation vol d'oiseau en temps ...
		return estimation;
	}
	
	 protected void updatecout( boolean choix_tps_dist, LabelBis courant, Liaison rt, LabelBis lab_next){
	    // choix distance quoi qu'il arrive
		 float coutReel =  courant.getCout()+rt.coutRoute(false);
		 int coutAux = courant.compareTo(lab_next) ;
		 if (coutAux>0) { //ie. distance courant vers dest est plus grand.
			 // on choisi lab_next
			 // on verifie que l'estimation est une borne inférieure !!
			 lab_next.setCout(coutReel);
			 lab_next.setPere(courant.getSommetCourant());
			 if (tas.existe(lab_next)){ 
			 	 tas.update(lab_next);
		 	 }
			 else{
				 tas.insert(lab_next);
			 }
		 }
	 }
	 // TODO : faire Actualiser_chemin avec prise en compte de la borne inf !!!
	 //if (coutReel >= lab_next.getHeuristique()){
	 
}

