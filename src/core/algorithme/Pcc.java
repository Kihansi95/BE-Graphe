package core.algorithme ;

import java.awt.Color;
import java.io.* ;
import java.util.ArrayList;
import java.util.List;

import base.BinaryHeap;
import base.Dessin;
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
    
    private boolean choix_tps_dist = false; // par defaut en distance ;
    
    
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
	
	// POUR DESSINER APRES :
	
	Dessin dessin = this.graphe.getDessin();
	dessin.setColor(Color.GREEN);
	dessin.setWidth(3);
	
	// DEBUT IMPLEMENTATION DIJKSTRA 
	// LES VARIABLES
	Chemin chemin_final = new Chemin();
	// contiendra les labels (avec le cout des noeuds)
    BinaryHeap<Label> tas = new BinaryHeap<Label>() ;
    // Tableau qui contient tous les labels (choix d'implï¿½mentation)
   ArrayList<Label>liste_labels = new ArrayList<Label>() ;
 // c'est dans cette liste qu'on m.a.j. le cout, le marquage, ... pour chaque sommets.
    
	ArrayList<Noeud> liste_sommets = (ArrayList<Noeud>) graphe.getNoeuds() ;
	int nbsommets = liste_sommets.size();
	
	int nb_marques = 0;

	
	// AFFICHAGE SI MODE TEMPS OU DISTANCE 
	if (choix_tps_dist){
		System.out.println("_____________Dijkstra fait en fonction du temps____________");
	}
	else {
		System.out.println("_____________Dijkstra fait en fonction de la distance____________");
	}
	
	// INIT 
	for (Noeud sommet : liste_sommets ){
		Label lab = new Label(sommet);
		// a la creation du label, marquage a false, pere null et cout fixï¿½ ï¿½ max value
		liste_labels.add(sommet.getNumero(), lab);
	}
	// on set le label de l'origine : cout et absence de pere
	// on insert le label de l'origine dans le tas
	liste_labels.get(origine).setCout(0);
	liste_labels.get(origine).setPere(-1); 
	tas.insert(liste_labels.get(origine)) ;
	
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
			courant = tas.deleteMin();
			chemin_final.addSommet(liste_sommets.get(courant.getSommetCourant()));

			
			courant.setMarquage(true);
			if (liste_sommets.get(courant.getSommetCourant())== liste_sommets.get(destination)){
				destination_atteinte = true ; // on sort de la boucle (on peut pas faire break ?)
			}
			
			routes_vers_voisins = liste_sommets.get(courant.getSommetCourant()).getLiaisons();
			// on regarde tous les successeurs 
			for ( Liaison rt : routes_vers_voisins){
				// TODO : traiter si on sort de la zone
				lab_next = liste_labels.get(rt.getSuccesseur().getNumero());// on recupere le label
				// CONDITION POUR DIJKSTRA
				float coutAux;
				if (!lab_next.getMarquage()){
					//suivant le mode choisi 
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
						nb_marques++ ;
						chemin_final.addSommet(liste_sommets.get(lab_next.getSommetCourant()));
						chemin_final.addRoute(rt);
					}
				}
			}// fin FOR
		}//fin WHILE
		
		System.out.println("je suis ici ! \n" + "destination atteinte : " + destination_atteinte + "\n");
		// ACTUALISER LE CHEMIN SI le label courant est la destination :
		if (liste_sommets.get(courant.getSommetCourant()) == liste_sommets.get(destination)){
			/*// en se servant du tas 
			System.out.println("je suis là ! : \n");
			tas.printSorted();

			Label position; 
			Noeud sommet_pos ;
			Noeud next ;
			
			while (tas.isEmpty()== false){
				
				System.out.println("il y a " + tas.size()+ " sommets dans le tas \n");
				position = tas.deleteMin();
				sommet_pos = liste_sommets.get(position.getSommetCourant());
				next = liste_sommets.get(tas.findMin().getSommetCourant());
				chemin_final.addSommet(sommet_pos);
				chemin_final.addRoute(next, choix_tps_dist);
			}
			
			
			*/
			/*System.out.println("je suis là ! \n");
			// en partant de la destination et en retournant a l'envers grace au pere !!!
			int position = destination ;
			Label lab_pos;
			Noeud sommet_pos;	
			while(position!=origine){
				sommet_pos = liste_sommets.get(position) ;
				chemin_final.addSommet(sommet_pos);
				lab_pos = liste_labels.get(position);
				chemin_final.addRoute(liste_sommets.get(lab_pos.getPere()), choix_tps_dist);
				// on remonte sur le noeud precedent
				position = lab_pos.getPere();
			}
			// enfin on ajoute le noeud origine 
			// la liaison vers l'origine a deja ete ajoutee
			chemin_final.addSommet(liste_sommets.get(origine));
			*/
		}
		
		// TODO : separer Dijkstra en plusieurs mï¿½thodes pour plus de lisibilitï¿½ !!
		System.out.println(" === Longueur du chemin : "+chemin_final.getDistanceTotale()+" metres");
		System.out.println(" === temps du chemin : "+chemin_final.getTempsTotal()+" minutes");
		chemin_final.dessiner(dessin, graphe.getZone());

		}
	
	}
    
}
