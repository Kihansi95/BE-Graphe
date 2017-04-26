package core.algorithme;

import core.graphe.*;

public class Label {
	private boolean marquage;
	private float cout;
	private Noeud pere;
	private Noeud sommetCourant;
	
	public Label(Noeud sommetCourant, boolean marquage)	{
		if(sommetCourant == null)
			throw new IllegalArgumentException("sommet courant est null\n");
		
		this.sommetCourant = sommetCourant;
		this.marquage = marquage;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMarque()	{
		return marquage;
	}
}
