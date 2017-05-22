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
	
	/**
	 * @return la vitesse maximale
	 */
	public int getVitesseMax()	{
		return this.getDescripteur().vitesseMax();
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

	@Override
	public String toString()	{
		return "Liaison "+predecesseur + (descripteur.isSensUnique()?" -> ":" <-> ") + successeur+ " - "+descripteur;
	}
}
