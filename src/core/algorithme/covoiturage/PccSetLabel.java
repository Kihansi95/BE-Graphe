package core.algorithme.covoiturage;

import java.io.PrintStream;

import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Pcc;
import exceptions.SommetNonExisteException;

public class PccSetLabel extends Pcc {

	
	public PccSetLabel(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg);
	}

	public PccSetLabel contraintVision(Voyagueur pieton) {
		// TODO Auto-generated method stub
		return this;
	}
	
	public LabelCovoiturage getSolution()	{
		return ;
	}
	
}
