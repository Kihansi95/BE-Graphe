package core.algorithme ;

import java.io.* ;
import java.util.ArrayList;
import java.util.List;




import base.BinaryHeap;
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
    
    private BinaryHeap<Noeud> tas;

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

	// A vous d'implementer la recherche de plus court chemin.
	
	// DEBUT IMPLEMENTATION DIJKSTRA 
	// mon tas
	BinaryHeap<Label> tas = new BinaryHeap<Label>() ;
	
	List<Noeud> liste_sommets = graphe.getNoeuds() ;
	int nbsommets = liste_sommets.size();
	ArrayList<Label> liste_labels = new ArrayList<Label>() ;
	// c'est dans cette liste qu'on m.a.j. le cout, le marquage, ... pour chaque sommets.
	int nb_marques = 0;
	Noeud sorigine = liste_sommets.get(origine) ;
	Noeud sdest = liste_sommets.get(destination) ;
	Label courant ;

	// INIT 
	for (Noeud sommet : liste_sommets ){
		Label lab = new Label(sommet);
		// a la cration du label, marquage � false, pere null et cout fix� � max value
		liste_labels.add(lab) ; // on ajoute le labell � notre liste de label
	}
	// on set le label de l'origine)
	// on insert le label de l'origine dans le tas
	liste_labels.get(origine).setCout(0);
	tas.insert(liste_labels.get(origine)) ;
	
	// ITERATION
	boolean som_non_marques = true ;
	ArrayList<Noeud> successeurs = new ArrayList<Noeud>() ;
	Liaison Route_actuelle ;
	Label y ; 
	float coutAux ;
	while (som_non_marques){
		courant = tas.findMin();
		courant.setMarquage(true);
		successeurs = courant.getSommetCourant().getSuccesseurs() ;
		if (x.getSommetCourant()== sdest){
			som_non_marques= true ; // on sort de la boucle (on peut pas faire break ?)
		}
		for (Noeud suc : successeurs){
			// on regarde tous les successeurs 
			y = getLabel(suc, liste_labels);
			if (y.getMarquage()){
				coutAux
			}
		}
		
	}
	
    }
    
    private Label getLabel(Noeud sommet, List<Label> lists)	{
    	return lists.get(sommet.getNumero());
    }

}
