package core.algorithme.covoiturage;

public class Voyageur {
	private int vitesseMax;
	private String nom;
	private float latitude;
	private float longitude;
	
	public Voyageur(String nom, int vitessemax)	{
		this.nom = new String(nom);
		this.vitesseMax = vitessemax;
	}
	
	public int getVitesseMax()	{
		return vitesseMax;
	}
	
	public String toSring()	{
		return nom;
	}
}
