package application;

import java.io.DataInputStream;
import java.util.*;

import base.*;
import core.*;
import core.algorithme.dijkstra.Label;
import core.graphe.Noeud;
import exceptions.SommetNonExisteException;

public class Test {

	public static void main(String[] args) {

	}
	
	public static void intersectTest()	{
		try {
			
			DataInputStream mapdata = Openfile.open ("carre") ;
			Graphe graphe = new Graphe("carre", mapdata, new DessinInvisible()) ;
			
			List<Label> set1 = new ArrayList<Label>();
			List<Label> set2 = new ArrayList<Label>();
			
			Noeud sameNoeud = graphe.getNoeud(9);
			Noeud noeud1 = graphe.getNoeud(8);
			Noeud noeud2 = graphe.getNoeud(10);
				
			// label dans set 1
			Label label11 = new Label(sameNoeud);
			label11.setCout(1);
			Label label12 = new Label(noeud1);
			set1.add(label11);	set1.add(label12);
			
			
			// label dans set 2
			Label label21 = new Label(sameNoeud);
			label21.setCout(2);
			Label label22 = new Label(noeud2);
			set2.add(label21); 	set2.add(label22);
			
			System.out.println("before intersect: ");
			System.out.println(set1);
			System.out.println(set2);
				
			// intersect test
			set1.retainAll(set2);
			
			System.out.println("\n after intersect: ");
			System.out.println(set1);	// cout dans set 1 (1)
			System.out.println(set2);
			
			} catch (SommetNonExisteException e) {
				e.printStackTrace();
			}
	}

	public static void intersectMapTest()	{
		try {
			
			DataInputStream mapdata = Openfile.open ("carre") ;
			Graphe graphe = new Graphe("carre", mapdata, new DessinInvisible()) ;
			
			Map<Noeud, Label> set1 = new HashMap<Noeud, Label>();
			Map<Noeud, Label> set2 = new HashMap<Noeud, Label>();
			
			Noeud sameNoeud = graphe.getNoeud(9);
			Noeud noeud1 = graphe.getNoeud(8);
			Noeud noeud2 = graphe.getNoeud(10);
				
			// label dans set 1
			Label label11 = new Label(sameNoeud);
			label11.setCout(1);
			Label label12 = new Label(noeud1);
			set1.put(sameNoeud, label11);	set1.put(noeud1, label12);
			
			
			// label dans set 2
			Label label21 = new Label(sameNoeud);
			label21.setCout(2);
			Label label22 = new Label(noeud2);
			set2.put(sameNoeud, label21); 	set2.put(noeud2, label22);
			
			System.out.println("before intersect: ");
			System.out.println(set1);
			System.out.println(set2);
				
			// intersect test
			set1.keySet().retainAll(set2.keySet());
			
			System.out.println("\n after intersect: ");
			System.out.println(set1);	// cout dans set 1 (1)
			System.out.println(set2);
			
		} catch (SommetNonExisteException e) {
			e.printStackTrace();
		}
	}

}
