package core.graphe;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import base.Couleur;
import base.Descripteur;
import base.Dessin;

/**
 * Représenter partiellement la route. 
 * Liaison n'est que dans le sens unique, si la route est dans double sens, liaison doit être doublés.
 * @author Anais RABARY
 */
public class Liaison{
	
	/**
	 * Longueur de laision en mettre
	 */
	private float longueur;
	
	/**
	 * @see base.Descripteur
	 */
	private Descripteur descripteur;
	
	/**
	 * L'ensemble de segments d'une liaison
	 */
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
	
	/**
	 * Longtiude du noeud sur la carte (Graphe)
	 * @return float
	 */
	public float getLongitude()	{
		return this.longitude;
	}
	
	public float getLatitude()	{
		return this.latitude;
	}
	
	/**
	 * Longueur de la liaison en m
	 * @return longueur
	 */
	public float getLongueur() {
		return longueur;
	}
	
	/**
	 * Vitesse maximale sur une liaison en km/h
	 * @return la vitesse maximale
	 */
	public int getVitesseMax()	{
		return descripteur.vitesseMax();
	}
	
	/**
	 * Ajouter un segment dans une liaison
	 * @param segment nouveau segment
	 */
	public void addSegment(Segment segment)	{
		if(segment == null)
			throw new IllegalArgumentException();
		segments.add(segment);
	}
	
	/**
	 * Permet de savoir si la route est il unique
	 * @return true s'il n'existe pas de liaison dans le sens inverse
	 * @see base.descripteur#isSensUnique
	 */
	public boolean isSensUnique()	{
		return descripteur.isSensUnique();
	}

	/**
	 * @return le successeur
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
	 * Dessiner la route avec couleur par defaut
	 * @param dessin 
	 * @param int: numéro de zone dans lequel on dessine
	 * @see core.graphe.Liaison#dessiner(Dessin, int, Color)
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
	 * @see core.graphe.Segment#dessiner(Dessin, float, float)
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
		
		if (successeur.inZone(zone) && !successeur.equals(predecesseur)) {
			dessin.drawLine(current_long, current_lat, successeur.getLongitude(), successeur.getLatitude());
		}
	}

	@Override
	public String toString()	{
		return "Liaison "+predecesseur + (descripteur.isSensUnique()?" -> ":" <-> ") + successeur+ " - "+descripteur;
	}
	
	/**
	 * @return type de la route
	 * @see base.Descripteur#showType
	 */
	public TypeRoute getType()	{
		switch(this.descripteur.getType())	{
		case 'a': 
			return TypeRoute.AUTOROUTE; 
		case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': 
			return TypeRoute.ROUTE; 
		case 'z': 
			return TypeRoute.COTE;
		default : 
			return TypeRoute.UNKNOWN;
		}
	}
	
	public boolean isType(TypeRoute... types)	{
		if(types.length == 0)
			return true;
		for(TypeRoute type : types)
			if(type == TypeRoute.ALL || getType() == type)
				return true;
		return false;
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
