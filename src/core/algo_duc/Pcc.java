package core.algo_duc ;

import java.io.* ;
import java.util.ArrayList;
import java.util.List;

import base.BinaryHeap;
import base.Readarg ;
import core.Algo;
import core.Graphe;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.PereAbsentException;

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
	
		// init lables
		BinaryHeap<Label> tas = new BinaryHeap<Label>();
		List<Noeud> noeuds = this.graphe.getNoeuds();
		for(Noeud noeud : noeuds)	{
			tas.insert(new Label(noeud));
		}
		Label destination = tas.get(this.destination);
		
		Label sommet_actuel = labels.get(this.origine);
		
		
		while(sommet_actuel != destination)	{
			List<Noeud> successeurs = sommet_actuel.getSommetCourant().getSuccesseurs();
			for(Noeud succ : successeurs)	{
				Label label_succ = labels.get(succ.getNumero());
				update(label_succ, sommet_actuel);
			}
			
			
			do	{
				sommet_actuel = tmp.deleteMin();
			}
		}
		
		
		
	
    }
    
    private Label getLabelSuivant(final BinaryHeap<Label> tas)	{
    	BinaryHeap<Label> tmp = new BinaryHeap<Label>(tas);
    	Label suivant = tmp.deleteMin();
		while	(sommet_actuel.isMarque)	{
			suivant = tmp.deleteMin();
		}
		return suivant;
    }
    
    /**
     * Mettre à jour le successeur par rapport à sommet courant. 
     * @param successeur
     * @param courant
     * @return
     */
    private Label update(Label successeur, Label courant)	{
    	if(successeur.getPere() == null)
    		successeur.setPere(courant);
    	else	{
    		float new_cout = courant.getCout() + Chemin.getLiaisonOptimal(courant.getSommetCourant(), successeur.getSommetCourant()).getLongueur();
    		if(successeur.getCout() > new_cout)
    			successeur.setPere(courant);
    	}
    	return successeur;
    }
    
    /**
     * Chauque fois quand on a fini à exploité un sommet, on le marquerais 
     * @param tas
     */
    private void exploite(BinaryHeap<Label> tas, Label label)	{
    	
    	tas.insert(label);
    	label.marquer();
    	label.getSommetCourant().dessiner(this.graphe.getDessin());
    }


}
