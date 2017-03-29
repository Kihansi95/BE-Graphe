package core;

import LogLine;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;

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
		this.liaisons = new ArrayList<liaison>();
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
	public Noeud getSuccesseur (Liaison L) {
		return L.getSuccesseur() ;
	}
	
	public int getNbSuccesseur() {
		Iterator <Liaison> ite = liaisons.iterator();
    	int nbSucc = 0 ;
    	while (ite.hasNext()){
    		Liaison I = ite.next();
    		Noeud succ = I.getSuccesseur();
    	}
    		
	}
	
	public List<Noeud> getSuccesseurs (){
	 // TODO
		
	}
}
