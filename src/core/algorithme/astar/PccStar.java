package core.algorithme.astar ;

import java.awt.Color;
import java.io.* ;

import base.Readarg ;
import core.Graphe;
import core.algorithme.dijkstra.Label;
import core.algorithme.dijkstra.Pcc;
import core.graphe.Critere;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class PccStar extends Pcc {
	
	private static final Color COULEUR_SOLUTION = Color.ORANGE;

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
    	super(gr, sortie, readarg) ;
    }
    
    public PccStar(Graphe gr, PrintStream sortie, Noeud origine, Noeud destination, Critere critere)	{
    	super(gr, sortie, origine, destination, critere);
    }
    
    public Color couleurSuccesseurVisite()	{	return Color.GREEN;		}
    public Color couleurExplore()	{	return Color.MAGENTA;	}
    public Color couleurSolution()	{	return Color.CYAN; 	}
    
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

