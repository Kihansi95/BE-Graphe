package core.algorithme ;

import java.io.* ;
import base.Readarg ;
import core.Graphe;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
    	super(gr, sortie, readarg) ;
    }

    public void run() {

	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin A*
    }

}
