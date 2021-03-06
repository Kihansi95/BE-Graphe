package core.graphe;

import java.util.Comparator;

import core.algorithme.comparator.DistanceComparator;
import core.algorithme.comparator.TempsComparator;
import core.algorithme.comparator.VitesseComparator;

public class ComparatorFactory {
	public final static Comparator<Liaison> getComparator(Critere critere)	{
		switch(critere)	{
		case TEMPS:
			return new TempsComparator();
		case DISTANCE:
			return new DistanceComparator();
		case VITESSE:
			return new VitesseComparator();
		default: 
			throw new IllegalArgumentException("Critere inconnu : "+critere);
		}
	}
}
