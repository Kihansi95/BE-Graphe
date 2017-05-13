package core.algorithme;

import core.graphe.*;

public class Label implements Comparable < Label > {
	private boolean marquage;
	private float cout;
	private int pere;
	private int sommetCourant;
	
	public Label(Noeud sommetCourant)	{
		if(sommetCourant == null)
			throw new IllegalArgumentException("sommet courant est null\n");
		
		this.sommetCourant = sommetCourant.getNumero();
		this.marquage = false;
		this.cout = Float.MAX_VALUE ;
		this.pere = 0 ; 
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
		return this.sommetCourant ;
	}

	public int compareTo(Label label) { 
		// on multiplie par mille pour prendre en compte les chiffres deri�re la virgule
		return (int)((this.cout - label.cout)*1000) ;
	}
}
