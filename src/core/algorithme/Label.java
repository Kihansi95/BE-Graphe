package core.algorithme;

import core.graphe.*;

public class Label {
	private boolean marquage;
	private float cout;
	private Noeud pere;
	private Noeud sommetCourant;
	
	public Label(Noeud sommetCourant)	{
		if(sommetCourant == null)
			throw new IllegalArgumentException("sommet courant est null\n");
		
		this.sommetCourant = sommetCourant;
		this.marquage = false;
		this.cout = Float.MAX_VALUE ;
		this.pere = null ; 
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
	
	public void setPere(Noeud papa){
		this.pere = papa ;
	}
	public void setCout(float cout){
		this.cout = cout ;
	}
	public Noeud getPere (){
		return this.pere ;
	}
	public boolean getMarquage(){
		return this.marquage ;
	}
	public float getCout(){
		return this.cout ;
	}
	public Noeud getSommetCourant(){
		return this.sommetCourant ;
	}
}
