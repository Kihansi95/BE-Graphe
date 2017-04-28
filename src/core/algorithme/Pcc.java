package core.algorithme ;

import java.io.* ;
import java.util.ArrayList;
import java.util.List;

import base.BinaryHeap;
import base.Readarg ;
import core.Algo;
import core.Graphe;
import core.graphe.Chemin;
import core.graphe.Liaison;
import core.graphe.Noeud;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    // contiendra les labels (avec le cout des noeuds)
    private BinaryHeap<Label> tas;
    // Tableau qui contient tous les labels (choix d'implémentation)
    private Label tableau_labels[] ;
    
    private boolean choix_tps_dist = false // par defaut en distance ;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg, boolean choix) {
	super(gr, sortie, readarg) ;

	this.zoneOrigine = gr.getZone () ;
	this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.destination = readarg.lireInt ("Numero du sommet destination ? ");
	this.choix_tps_dist = choix ;
    }

    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin.
	
	// DEBUT IMPLEMENTATION DIJKSTRA 
	// LES VARIABLES
	Chemin chemin_final = new Chemin();
	// mon tas
	tas = new BinaryHeap<Label>() ;
	List<Noeud> liste_sommets = graphe.getNoeuds() ;
	int nbsommets = liste_sommets.size();
	tableau_labels = new Label[nbsommets] ;
	// c'est dans cette liste qu'on m.a.j. le cout, le marquage, ... pour chaque sommets.
	int nb_marques = 0;
	Noeud sorigine = liste_sommets.get(origine) ;
	Noeud sdest = liste_sommets.get(destination) ;
	
	// AFFICHAGE SI MODE TEMPS OU DISTANCE 
	if (choix_tps_dist){
		System.out.println("_____________Dijkstra fait en fonction du temps____________");
	}
	else {
		System.out.println("_____________Dijkstra fait en fonction de la distance____________");
	}
	
	// INIT 
	for (Noeud sommet : liste_sommets ){
		// a la creation du label, marquage a false, pere null et cout fixï¿½ ï¿½ max value
		tableau_labels[sommet.getNumero()] = new Label(sommet);
	}
	// on set le label de l'origine : cout et absence de pere
	// on insert le label de l'origine dans le tas
	tableau_labels[origine].setCout(0);
	tableau_labels[origine].setPere(-1); 
	tas.insert(tableau_labels[origine]) ;
	
	// ITERATION ( parcours)
	boolean destination_atteinte = false ;
	Label courant = null ;
	// test si origine == dest
	if (origine == destination){
		System.out.println("Sorry, you already made it to your destination. I can't do anything for you...\n");
	}
	else {
				ArrayList<Liaison> routes_vers_voisins = new ArrayList<Liaison>(); ;
		Label lab_next ; 
		
		while (!tas.isEmpty() && !destination_atteinte){
			courant = tas.findMin();
			tas.deleteMin() ;
			
			courant.setMarquage(true);
			if (liste_sommets.get(courant.getSommetCourant())== sdest){
				destination_atteinte = true ; // on sort de la boucle (on peut pas faire break ?)
			}
			
			routes_vers_voisins = liste_sommets.get(courant.getSommetCourant()).getLiaisons();
			// on regarde tous les successeurs 
			for ( Liaison rt : routes_vers_voisins){
				// TODO : traiter si on sort de la zone
				lab_next = tableau_labels[rt.getSuccesseur().getNumero()];// on recupere le label
				// CONDITION POUR DIJKSTRA7
				float coutAux;
				//suitvant le mode choisi 
				if (choix_tps_dist){ // en temporel 
					coutAux = courant.getCout() + rt.coutRoute(choix_tps_dist);
				}
				else{ // en distance 
					coutAux = courant.getCout()+rt.coutRoute(choix_tps_dist);
				}
				// on compare l'ancien et le nouveau coup 
				if (coutAux < lab_next.getCout()){
					// on Met à jour le label actuel  :
					lab_next.setCout(coutAux);
					lab_next.setPere(courant.getSommetCourant());
					// puis on l'insere ou l'actualise si déja présent dans le tas
					if (tas.existe(lab_next){ // TODO : methode exite dans binaryHeap
						tas.update(lab_next);
					}
					else{
						tas.insert(lab_next);
					}
					nb_marques++ ;
				}
			}// fin FOR
		}//fin WHILE
		
		// ACTUALISER LE CHEMIN SI le label courant est la destination :
		if (courant.getSommetCourant() == destination){
			// en partant de la destination et en retraçant à l'envers grace au père !!!
			// TODO !!!!
		}
		
		// TODO : separer Dijkstra en plusieurs méthodes pour plus de lisibilité !!
		
	}
	
    }


}
