package core.graphe;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import base.Couleur;
import base.Descripteur;
import base.Dessin;

public class Liaison implements Comparable<Liaison>{
	
	private float longueur; // en metres !!!!!
	
	private Descripteur descripteur;
	private List<Segment> segments;
	
	private Noeud successeur;
	private Noeud predecesseur;
	
	/**
	 * 
	 * @param successeur
	 * @param predecesseur
	 * @param longueur
	 * @param descripteur
	 * @param segments
	 */
	public Liaison(Noeud predecesseur, Noeud successeur, float longueur, Descripteur descripteur, List<Segment> segments)	{
		if(successeur == null)
			throw new IllegalArgumentException("Liaison doit savoir son successeur");
		if(predecesseur == null)
			throw new IllegalArgumentException("Liaison doit savoir son predecesseur");
		if(descripteur == null)
			throw new IllegalArgumentException("Liaison doit avoir son propre information");
		
		this.successeur = successeur;
		this.predecesseur = predecesseur;
		this.predecesseur.addLiaison(this);
		this.longueur = longueur;
		this.descripteur = descripteur;
		this.segments = segments;
	}
	
	/**
	 * Shortcut constructor
	 * @param successeur
	 * @param predecesseur
	 * @param longueur
	 * @param descripteur
	 */
	public Liaison(Noeud predecesseur, Noeud successeur, float longueur, Descripteur descripteur)	{
		this(predecesseur, successeur, longueur, descripteur, new LinkedList<Segment>());
	}
	
	/**
	 * @return the longueur
	 */
	public float getLongueur() {
		return longueur;
	}
	
	public void addSegment(Segment segment)	{
		if(segment == null)
			throw new IllegalArgumentException();
		segments.add(segment);
	}
	public List<Segment> getSegments (){
		return segments ;
	}

	/**
	 * @return the descripteurs
	 */
	public Descripteur getDescripteur() {
		return descripteur;
	}

	/**
	 * @return the successeur
	 */
	public Noeud getSuccesseur() {
		return successeur;
	}
	
	/**
	 * @return le prédécesseur de cette liaison
	 */
	public Noeud getPredecesseur()	{
		return predecesseur;
	}
	
	/**
	 * Compare longueur
	 * @param liaison
	 * @return > 0 si this > liaison.
	 */
	public int compareTo(Liaison liaison)	{
		return (int) ((getLongueur() - liaison.getLongueur())*PRECISION);
	}
	private static final int PRECISION = 10000000;
	
	/**
	 * Dessiner la route.
	 * @param dessin 
	 * @param int: numéro de zone dans lequel on dessine
	 */
	public void dessiner(Dessin dessin, int zone )	{
		
		Couleur.set(dessin, getDescripteur().getType()) ;	// couleur par définition de la route
		this.dessiner(dessin, zone, null);
	}
	
	/**
	 * Dessiner la route avec couleur
	 * @param dessin
	 * @param zone
	 * @param color
	 */
	public void dessiner(Dessin dessin, int zone, Color color)	{
		if(color != null)
			dessin.setColor(color);
		if(dessin == null)
			throw new IllegalArgumentException("dessin null");
		
		float current_long = predecesseur.getLongitude();
		float current_lat = predecesseur.getLatitude();
		
		for(Segment s: segments)	{
			s.dessiner(dessin, current_long, current_lat);
			current_long += s.getDeltaLong();
			current_lat += s.getDeltaLat();
		}
		
		if (successeur.inZone(zone)) {
			dessin.drawLine(current_long, current_lat, successeur.getLongitude(), successeur.getLatitude());
		}
	}
	
	public float coutRoute(Boolean choix){
		float cout = 0.0f ;
		if (choix==true){
			// on calcul le cout temporel
			// la vitesse est en km/h
			//la longueur en m.
			// donc on met la vitesse en m/h 
			// et pour plus de lisibilit�, on met le temps en minute
			cout = (this.getLongueur()/(this.getDescripteur().vitesseMax()* 1000))*60;
		}
		else {
			// on calcul le cout en distance 
			cout = this.getLongueur();
		}
		
		return cout ;
	}

	@Override
	public String toString()	{
		return "Liaison "+predecesseur + (descripteur.isSensUnique()?" -> ":" <-> ") + successeur+ ". Description:\n"+descripteur;
	}
}
