package core;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import base.Descripteur;
import base.Dessin;

public class Liaison {
	
	private float longueur;
	
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
	 * Verifier si le liason est arrete ou chemin
	 * @return true si arrete, falst si chemin
	 */
	public boolean isSensUnique()	{
		return descripteur.isSensUnique();
	}
	
	/**
	 * Compare longueur
	 * @param liaison
	 * @return > 0 si this > liaison.
	 */
	public float compareTo(Liaison liaison)	{
		return getLongueur() - liaison.getLongueur();
	}
	
	/**
	 * Dessiner la route.
	 * @param dessin 
	 * @param couleur : par default (null) YELLOW
	 */
	public void dessiner(Dessin dessin, Color couleur, int zone)	{
		if(dessin == null)
			throw new IllegalArgumentException("dessin null");
		
		float current_long = predecesseur.getLongitude();
		float current_lat = predecesseur.getLatitude();
		
		//dessin.setColor(couleur == null? Color.YELLOW : couleur);
		for(Segment s: segments)	{
			s.dessiner(dessin, current_long, current_lat);
			current_long += s.getDeltaLong();
			current_lat += s.getDeltaLat();
		}
		
		if (successeur.inZone(zone)) {
			dessin.drawLine(current_long, current_lat, successeur.getLongitude(), successeur.getLatitude());
		}
	}

}
