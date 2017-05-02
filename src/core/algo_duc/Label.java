package core.algo_duc;

import core.graphe.*;
import exceptions.PereAbsentException;

public class Label implements Comparable < Label > {
	private boolean marquage;
	private float cout;
	private Label pere;
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
	
	public void marquer()	{
		marquage = true;
	}
	
	public void setPere(Label pere){
		this.pere = pere ;
		updateCout();
	}
	
	/**
	 * Calcul le coût optimal de sommet vers sommet
	 * @param cout
	 * @throws Exception 
	 */
	private void updateCout() {
		if(pere == this)	{	// cas sommet d'origine
			cout = 0f;
		}	else	{			// autre: cout = cout_precedent + cout chemin
			Liaison optimal = Chemin.getLiaisonOptimal(this.getSommetCourant(), pere.getSommetCourant());
			cout = optimal.getLongueur() + pere.getCout();
		}
	}
	
	public void setCout(float cout){
		this.cout = cout ;
	}
	
	
	
	public Label getPere (){
		return this.pere ;
	}
	
	public float getCout(){
		return this.cout ;
	}
	public Noeud getSommetCourant(){
		return sommetCourant ;
	}

	public int compareTo(Label label) { 
		return (int)((this.cout - label.cout)*1000) ;
	}
}
