package core.graphe;

import base.Dessin;

public class Segment {

	private float nextLong;
	private float nextLat;
	
	public Segment(float nextLong, float nextLat)	{
		this.nextLong = nextLong;
		this.nextLat = nextLat;
	}
	
	/**
	 * Cloner un segment
	 * @param segment
	 */
	public Segment(Segment segment)	{
		this.nextLat = segment.nextLat;
		this.nextLong = segment.nextLong;
	}

	/**
	 * Dessiner un segment
	 * @param dessin
	 * @param from_long 
	 * @param from_lat
	 */
	public void dessiner(Dessin dessin, float from_long, float from_lat) {
		dessin.drawLine(from_long, from_lat, nextLong, nextLat);
	}
	
	public float getNextLong()	{
		return nextLong;
	}
	
	public float getNextLat()	{
		return nextLat;
	}
	
	public String toString()	{
		return "["+nextLong+","+nextLat+"]";
	}

}
