package core.algorithme.covoiturage;

import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Pcc;
import exceptions.SommetNonExisteException;

public class PccSetLabel extends Pcc {

	private Set<LabelCovoiturage> pointsAtteints;
	
	public PccSetLabel(Graphe gr, PrintStream sortie, Readarg readarg) throws SommetNonExisteException {
		super(gr, sortie, readarg);
		this.pointsAtteints = new TreeSet<LabelCovoiturage>();
	}

	public PccSetLabel contraintVision(Voyagueur pieton) {
		return this;
	}
	
	public Set<LabelCovoiturage> getPointsAtteints()	{
		return new TreeSet<LabelCovoiturage>(this.pointsAtteints);
	}
	
}
