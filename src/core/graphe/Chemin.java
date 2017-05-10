package core.graphe;
import java.awt.Color;
import  java.util. * ;

import base.Couleur;
import base.Dessin;

/**
 *  permet de retrnir le chemin entre 2 sommets (origine et destination) avec les routes empruntées 
 */
public class Chemin {

	// DANS LA LISTE DE SOMMETS (d'apr�s le remplissage avec Dijkstra, 
	//le 1er sommet est le sommet DESTINATION. 
	
	// listes des chemins empruntés dans l'ordre du chemin
	private List<Noeud> liste_sommets_empruntes ;
	private Noeud origine ;
	/**
	 * liaison optimal à chaque fois qu'on passe une noeud à l'autre
	 */
	private List<Liaison> routesEmpruntes;
	
	/**
	 * cout du chemin (en temps et distance)
	 */
	private float temps_total ;
	private float distance_totale ;
	
	/**
	 * constructeur 
	 */
	public Chemin(Noeud origine){
		liste_sommets_empruntes = new ArrayList<Noeud>();
		routesEmpruntes = new ArrayList<Liaison>();
		temps_total = 0 ;
		distance_totale = 0 ;	
		this.origine = origine ;
	}
	
	public Chemin(Noeud origine,ArrayList<Noeud> liste_som, ArrayList<Liaison> routesEmprunt,float tmps_min, float dist){
		if(routesEmpruntes == null ||  liste_som == null || (routesEmpruntes.size()-1) != liste_som.size()){
			throw new IllegalArgumentException("Nombre de routes et sommets ne correspondent pas entre eux...");
		}
		this.liste_sommets_empruntes = new ArrayList<Noeud>();
		
		if (liste_som != null){
			this.liste_sommets_empruntes.addAll(liste_som) ;
		}
		
		this.routesEmpruntes = new ArrayList<Liaison>();
		if (routesEmprunt != null){
			this.routesEmpruntes.addAll(routesEmprunt);
		}
		// TODO : calcul temps distance avec for
		
		this.temps_total = tmps_min ;
		this.distance_totale = dist ;
		this.origine = origine ;
	}
	
	/**
	 * getteur temps_total
	 */
	public float getTempsTotal(){
		return temps_total ;
	}
	/**
	 * setteur temps_total
	 */
	public void setTempsTotal(float nouveauT){
		this.temps_total = nouveauT ;
	}
	
	/**
	 * getteur distance_totale 
	 */
	public float getDistanceTotale (){
		return distance_totale ;
	}
	/**
	 * setteur distance_totale 
	 */
	public void setDistanceTotale(float nouveauD){
		this.distance_totale = nouveauD ;
	}
	
	/**
	 * pour ajouter des sommets 
	 */
	public void addSommet( Noeud sommet){
		liste_sommets_empruntes.add(sommet);
	}
	/**
	 * ajouter une route empruntée à la liste des routes empruntées
	 * => si liste nulle, on ajoute la 1ere liaison
	 * => si liste non nulle, on ajoute  la liaison et on update le temps et la distance
	 */	
	public void addRoute(Liaison route){
		routesEmpruntes.add(route);
		this.distance_totale =+ route.coutRoute(false);
		this.temps_total=+ route.coutRoute(true);
		if (!liste_sommets_empruntes.contains(route.getPredecesseur())){
			liste_sommets_empruntes.add(route.getPredecesseur());
		}
		if(!liste_sommets_empruntes.contains(route.getSuccesseur())){
			liste_sommets_empruntes.add(route.getSuccesseur());
		}
		
	}
	/*public void addRoute (Noeud sommet_next, boolean choix){
		if (liste_sommets_empruntes.size()== 0){
			liste_sommets_empruntes.add(sommet_next);
		}
		else {
			// on prend la route en fin de liste
			Noeud sommet_actuel = liste_sommets_empruntes.get(liste_sommets_empruntes.size()-1);
			// on fait la liste des routes possibles entre les 2 sommets 
			ArrayList<Liaison> routes = sommet_actuel.getLiaisons_1vers2(sommet_next);
			Liaison route_plus_court ;
			float temps ;
			float distance ;
			float temps_aux ;
			float distance_aux ;
			
			// on teste si il existe bien des routes : 
			if (routes.size()==0){
				throw new EmptyStackException();
			}
			else {
				
				route_plus_court = routes.get(0);
				temps = route_plus_court.coutRoute(true) ; 
				distance = route_plus_court.coutRoute(false) ;
				// on cherche le chemin le plus court en temporel :
				if (choix == true){
					for (Liaison rt : routes){
						temps_aux = rt.coutRoute(true);
						distance_aux = rt.coutRoute(false);
						if (temps_aux<temps){
							route_plus_court = rt ;
							distance = distance_aux ;
							temps = temps_aux ;
						}
						if (temps_aux==temps && distance_aux < distance){
							route_plus_court = rt ;
							distance = distance_aux ;
							temps= temps_aux ;
						}
					}
				}
				//choix== false chemin le plus court en distance 
				else {
					for (Liaison rt : routes){
						temps_aux = rt.coutRoute(true);
						distance_aux = rt.coutRoute(false);
						if (distance_aux<distance){
							route_plus_court = rt ;
							distance = distance_aux ;
							temps = temps_aux ;
						}
						if (distance_aux==distance && temps_aux < temps){
							route_plus_court = rt ;
							distance = distance_aux ;
							temps= temps_aux ;
						}
					}
				}
				
			}
			// on ajoute le prochain sommet  et on a la route la plus courte (en distance ou temps) donc on uptdate les couts 
			liste_sommets_empruntes.add(sommet_next);
			this.routesEmpruntes.add(route_plus_court);
			setDistanceTotale(getDistanceTotale()+distance);
			setTempsTotal(getTempsTotal()+temps);			
		}
	}
	*/
	/**
	 * Dessiner l'emsemble de chemin
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone)	{
		System.out.println ("nb sommet emprunt�s"+ liste_sommets_empruntes.size()+ "\n");
		liste_sommets_empruntes.get(0).dessiner(dessin);
		for(Liaison route: routesEmpruntes){
			System.out.println("j'essaie de dessiner une route\n");
			int i=1 ;
			route.dessiner(dessin, zone);
			
			if(dessin == null)
				throw new IllegalArgumentException("dessin null");
			
			dessin.setColor(Color.GREEN);
			
			float current_long = route.getPredecesseur().getLongitude();
			float current_lat = route.getPredecesseur().getLatitude();
			
			for(Segment s: route.getSegments())	{
				s.dessiner(dessin, current_long, current_lat);
				current_long += s.getDeltaLong();
				current_lat += s.getDeltaLat();
			}
			
			if (route.getSuccesseur().inZone(zone)) {
				dessin.drawLine(current_long, current_lat, route.getSuccesseur().getLongitude(), route.getSuccesseur().getLatitude());
			}
			//liste_sommets_empruntes.get(i).dessiner(dessin);
			i++;
		}
	}

	
}
