package application;

import core.graphe.Noeud;

public class Voyageur {
	private int vitesseMax;
	private String nom;
	private Noeud depart;
	
	public Voyageur(String nom, int vitesseMax, Noeud depart)	{
		this.nom = new String(nom);
		this.vitesseMax = vitesseMax;
		this.depart = depart;
	}
	
	public int getVitesseMax()	{
		return vitesseMax;
	}
	
	public String toSring()	{
		return nom;
	}

	/**
	 * @return the depart
	 */
	public Noeud getDepart() {
		return depart;
	}
}
