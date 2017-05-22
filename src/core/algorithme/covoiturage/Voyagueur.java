package core.algorithme.covoiturage;

import core.algorithme.dijkstra.Label;

public class Voyagueur {
	private int vitesseMax;
	private String nom;
	private Label depart;
	
	public Voyagueur(String nom, int vitesseMax, Label depart)	{
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
	public Label getDepart() {
		return depart;
	}
}
