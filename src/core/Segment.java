package core;

import java.awt.Color;

import base.Dessin;

public class Segment {

	private float deltaLong;
	private float deltaLat;
	
	public Segment(float deltaLong, float deltaLat)	{
		this.deltaLong = deltaLong;
		this.deltaLat = deltaLat;
	}

	/**
	 * Dessiner un segment
	 * @param dessin
	 * @param from_long 
	 * @param from_lat
	 */
	public void dessiner(Dessin dessin, float from_long, float from_lat) {
		dessin.drawLine(from_long, from_lat, from_long + deltaLong, from_lat + deltaLat);
	}

	public float getDeltaLong() {
		return deltaLong;
	}

	public float getDeltaLat() {
		return deltaLat;
	}

}
