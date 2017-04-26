package core.graphe;


import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import base.Dessin;
import exceptions.PropreSuccesseurException;

public class Noeud {
	
	public final static int RAD = 10;
	
	// variables
	private float longitude ;
	private float latitude ;
	private int numero ;
	private static int compteur ;
	private boolean visited ;
	private ArrayList<Liaison> liaisons ;
	private int zone;
	
	// constructeur 
	public Noeud ( float longitude, float latitude, int zone){
		this.latitude = latitude ;
		this.longitude = longitude ;
		this.numero = compteur++;
		this.visited = false ;
		this.liaisons = new ArrayList<Liaison>();
		this.zone = zone;				// zone == -1 : zone not defined
	}
	
	public Noeud(float longitude, float latitude)	{
		this(longitude, latitude, -1);
	}
	
	// getteurs
	
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
	/*
	 * getteur liste des liaisons de 1 vers 2
	 */
	public ArrayList<Liaison> getLiaisons_1vers2 (Noeud dest){
		ArrayList<Liaison> liste_retour = new ArrayList<Liaison>() ;
		Iterator <Liaison> ite = liaisons.iterator() ;
		while (ite.hasNext()) {
			Liaison l = ite.next() ;
			Noeud succes = l.getSuccesseur() ;
			if (succes == dest){
				liste_retour.add(l);
			}
		}
		
		return liste_retour ;
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
		System.out.println("Noeud: "+longitude+", "+latitude);
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
}
