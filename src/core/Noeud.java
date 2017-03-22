package core;

import java.io.DataInputStream;
import java.io.IOException;

public class Noeud {
	// variables
	private float longitude ;
	private float latitude ;
	private int numero ;
	private static int compteur ;
	private boolean visited ;
	private List<Liaison> liaisons ;
	// constructeur 
	public Noeud ( float latitude, float longitude ){
		this.latitude = latitude ;
		this.longitude = longitude ;
		this.numero = compteur++;
		this.visited = false ;
	    }
	
	// getteurs
	
	public float getLongitude(){
		return this.longitude ;
	}
	
	public float getLatitude() {
		return this.latitude ;
	}
	
	// methodes en lien avec les successeurs
	
	public Noeud getSuccesseur (Liaison L) {
		return L.getSuccesseur() ;
	}
	
	public int getNbSuccesseur() {
		// TODO
	}
	
	public List<Noeud> getSuccesseurs (){
	 // TODO
		
	}
}
