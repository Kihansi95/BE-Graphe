package core;


import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import base.Dessin;

import exceptions.PropreSuccesseurException;

public class Noeud {
	// variables
	private float longitude ;
	private float latitude ;
	private int numero ;
	private static int compteur ;
	private boolean visited ;
	private ArrayList<Liaison> liaisons ;
	
	// constructeur 
	public Noeud ( float latitude, float longitude ){
		this.latitude = latitude ;
		this.longitude = longitude ;
		this.numero = compteur++;
		this.visited = false ;
		this.liaisons = new ArrayList<Liaison>();
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
	
	public ArrayList<Noeud> getSuccesseurs(){
		ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
		for(Liaison l : liaisons){
			noeuds.add(l.getSuccesseur());
		}
		return noeuds ;
	}
	
	public void dessiner(Dessin dessin, Color color){
		dessin.setColor(color == null? Color.GREEN : color);
		dessin.drawPoint (longitude, latitude, 5);
		
	}
}
