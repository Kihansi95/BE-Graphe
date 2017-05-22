package core.algorithme.covoiturage;

import java.io.PrintStream;

import base.Readarg;
import core.Graphe;
import core.algorithme.astar.PccStar;
import exceptions.SommetNonExisteException;

public class PccStarAdaptation extends PccStar {

	private LabelCovoiturage depart;
	private LabelCovoiturage destination;

	public PccStarAdaptation(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg);	
	}
	
	public PccStarAdaptation(Graphe gr, PrintStream sortie, LabelCovoiturage depart, Readarg readarg, LabelCovoiturage destination) throws SommetNonExisteException	{
		super(gr, sortie, readarg);	
		this.graphe = gr;
		this.sortie = sortie;
		this.depart = depart;
		this.destination = destination;
	}

}
