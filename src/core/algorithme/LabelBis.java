package core.algorithme;

import core.graphe.*;

public class LabelBis implements Comparable < LabelBis > {
	private boolean marquage;
	private float cout;
	private int pere;
	private int courant;
	private float heuristique ;
	
	public LabelBis(boolean marquage, float cout, int pere, int courant, float heuristique)	{
		
		this.courant = courant;
		this.marquage = false;
		this.cout = Float.MAX_VALUE ;
		this.pere = 0 ; 
		this.heuristique = heuristique ;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMarque()	{
		return marquage;
	}
	
	/**
	 * getteur et setteurs
	 */
	
	public void setMarquage(boolean marq) {
		this.marquage = marq ;
	}
	
	public void setPere(int papa){
		this.pere = papa ;
	}
	public void setCout(float cout){
		this.cout = cout ;
	}
	public int getPere (){
		return this.pere ;
	}
	public boolean getMarquage(){
		return this.marquage ;
	}
	public float getCout(){
		return this.cout ;
	}
	public int getSommetCourant(){
		return this.courant ;
	}
	public float getHeuristique() {
		return heuristique;
	}

	public void setHeuristique(float heuristique) {
		this.heuristique = heuristique;
	}


	/**
	 * compareTo
	 * @param Le Label à comparer avec this
	 * @return Le cout en millième de minute
	 */
	// On multiplie par mille, au cas oÃ¹ le float serait trop petit, le cast en int donnerait la valeur 0
	public int compareTo(LabelBis other) {
		return (int)( ( (this.cout + this.heuristique)-(other.cout + other.heuristique) ) * 1000);
	}
	
	public String toString() {
		return "Sommet n°" + courant + "marqué : " + this.marquage +
				"\ncout en temps : " + this.cout +
				"\nSommet pere : " + this.pere +
				"\nSommet courant associé à  ce label :" + this.courant+
				"\n cout heuristique :"+ this.heuristique;
	}


	
}
