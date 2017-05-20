package core.graphe;


import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import base.Couleur;
import base.Dessin;
import core.algorithme.ComparatorFactory;
import exceptions.PropreSuccesseurException;
import exceptions.SuccesseurNonExistantException;

public class Noeud {
	
	public final static int RAD = 10; //radian de noeud
	
	// variables
	private float longitude ;
	private float latitude ;
	private int numero ;
	private ArrayList<Liaison> liaisons ;
	private int zone;
	
	// constructeur 
	public Noeud (int numero, float longitude, float latitude, int zone){
		this.latitude = latitude ;
		this.longitude = longitude ;
		this.numero = numero;
		this.liaisons = new ArrayList<Liaison>();
		this.zone = zone;				// zone == -1 : zone not defined
	}
	
	public Noeud(int numero, float longitude, float latitude)	{
		this(numero, longitude, latitude, -1);
	}
	
	// getteurs
	
	public int getNumero()	{
		return numero;
	}
	
	/**
	 * @return la latitude
	 */
	public float getLongitude(){
		return this.longitude ;
	}
	/**
	 * @return la latitude
	 */
	public float getLatitude() {
		return this.latitude ;
	}
	
	// methodes en lien avec les successeurs
	
	/**
	 * @return le noeud successeur
	 */
	public Noeud getSuccesseur (Liaison l) {
		return l.getSuccesseur() ;
	}
	
	public void addLiaison(Liaison liaison)	{
		if(liaison == null)
			throw new IllegalArgumentException();
		liaisons.add(liaison);
	}
	
	/*
	 * getteur de la liste des liaisons
	 */
	public ArrayList<Liaison> getLiaisons(){
		return new ArrayList<Liaison> (this.liaisons);
	}
	
	/**
	 * get Liaison qui va vers destination (dest)
	 * @param dest: le noeud destinatire
	 * @return List
	 */
	public List<Liaison> getLiaisons(Noeud dest)	{
		List<Liaison> liaisons_possibles = new ArrayList<Liaison>();
		for(Liaison l : liaisons)
			if(l.getSuccesseur().equals(dest))
				liaisons_possibles.add(l);
		return liaisons_possibles;
	}
	
	/**
	 * Trouver la liaison le plus optimal parmi tous
	 * @param dest
	 * @param critere
	 * @return
	 * @throws SuccesseurNonExistantException 
	 */
	public Liaison getLiaisonOptimal(Noeud dest, Critere critere) throws SuccesseurNonExistantException	{
		
		// asset
		if(!this.getSuccesseurs().contains(dest))	
			throw new SuccesseurNonExistantException("Noeud " + dest + " n'est pas successeur de noeud "+this);
		
		List<Liaison> routes = this.getLiaisons(dest);
		routes.sort(ComparatorFactory.getComparator(critere));
		return routes.get(0);
	}
	/**
	 * @return le nombre de successeur
	 * @throws PropreSuccesseurException
	 */
	public int getNbSuccesseur() throws PropreSuccesseurException{
		Iterator <Liaison> ite = liaisons.iterator();
    	int nbSucc = 0 ;
    	while (ite.hasNext()){
      		Liaison l = ite.next();
    		Noeud succ = l.getSuccesseur();
    		if (succ == this)
    			throw new PropreSuccesseurException() ;
    		else 
    			nbSucc=+ 1 ;
    	}
    	return nbSucc ;
    	
	}
	
	/**
	 * @return tous les noeuds sucesseurs 
	 */
	public ArrayList<Noeud> getSuccesseurs(){
		ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
		for(Liaison l : liaisons){
			noeuds.add(l.getSuccesseur());
		}
		return noeuds ;
	}
	
	/**
	 * dessine le noeud 
	 * @param dessin
	 * @param color
	 */
	public void dessiner(Dessin dessin, Color color){
		dessin.setColor(color);
		dessin.drawPoint (longitude, latitude, RAD);
	}
	
	public void dessiner(Dessin dessin)	{
		this.dessiner(dessin, Color.GREEN);
	}
	
	public boolean inZone(int zone)	{
		return this.zone == zone;
	}
	
	public void setZone(int zone)	{
		this.zone = zone;
	}
	
	public int getZone()	{
		return zone;
	}
	
	@Override
	public boolean equals(Object noeud)	{
		return noeud instanceof Noeud && ((Noeud) noeud).numero == this.numero;
	}
	
	@Override
	public String toString()	{
		return this.zone +":" + this.numero;
	}
}
