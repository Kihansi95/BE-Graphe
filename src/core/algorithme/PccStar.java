package core.algorithme ;

import java.io.* ;

import base.Readarg ;
import core.Graphe;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
    	super(gr, sortie, readarg) ;

    }
    
    @Override
    protected Label newLabel(Noeud sommet)	{
    	return new LabelStar(sommet);
    }
    
    public String toString()	{
    	return "A*";
    }
    
    @Override
    protected Label updateSuccesseur(Label successeur, Label courant)	{
    	// fonctionne comme PCC mais ajouter l'heuristique en plus.
    	Label labelSuccesseur = super.updateSuccesseur(successeur, courant);	
    	Label labelDestination = this.getLabel(this.getDestination());
    	((LabelStar) labelSuccesseur).updateEstimation(labelDestination, this.critere, this.graphe);
    	return labelSuccesseur;
    }

}

