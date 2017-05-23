package core.graphe;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import base.Couleur;
import base.Descripteur;
import base.Dessin;

public class Liaison{
	
	private float longueur; // en metres !!!!!
	
	private Descripteur descripteur;
	private List<Segment> segments;
	
	private Noeud successeur;
	private Noeud predecesseur;
	
	private float longitude;
	private float latitude;
	
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
		
		this.longitude = (predecesseur.getLongitude() + successeur.getLongitude())/2;
		this.latitude = (predecesseur.getLatitude() + successeur.getLatitude())/2;
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
	
	public float getLongitude()	{
		return this.longitude;
	}
	
	public float getLatitude()	{
		return this.latitude;
	}
	
	/**
	 * @return the longueur
	 */
	public float getLongueur() {
		return longueur;
	}
	
	/**
	 * @return la vitesse maximale
	 */
	public int getVitesseMax()	{
		return descripteur.vitesseMax();
	}
	
	public void addSegment(Segment segment)	{
		if(segment == null)
			throw new IllegalArgumentException();
		segments.add(segment);
	}

	/**
	 * @return the descripteurs
	 *//*
	public Descripteur getDescripteur() {
		return descripteur;
	}*/
	
	public boolean isSensUnique()	{
		return descripteur.isSensUnique();
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
	 * Dessiner la route.
	 * @param dessin 
	 * @param int: numéro de zone dans lequel on dessine
	 */
	public void dessiner(Dessin dessin, int zone )	{
		Couleur.set(dessin, descripteur.getType()) ;	// couleur par définition de la route
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

	@Override
	public String toString()	{
		return "Liaison "+predecesseur + (descripteur.isSensUnique()?" -> ":" <-> ") + successeur+ " - "+descripteur;
	}

	/**
	 * Renverser le sens de liaison
	 */
	public void reverse() {
		this.predecesseur.removeLiaison(this);
		this.successeur.addLiaison(this);
		
		Noeud tmp = this.predecesseur;		//TODO dangereux, check si ca ne cree pas de boucle
		this.predecesseur = this.successeur;
		this.successeur = tmp;
	}
}
