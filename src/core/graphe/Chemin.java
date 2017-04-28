package core.graphe;
import  java.util. * ;

import base.Dessin;

/**
 *  permet de retrnir le chemin entre 2 sommets (origine et destination) avec les routes empruntées 
 */
public class Chemin {

	
	// nom du chemin
	private String nom_chemin ;
	
	// listes des chemins empruntés dans l'ordre du chemin
	private List<Noeud> liste_sommets_empruntes ;
	
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
	public Chemin(String nom_chemin){
		liste_sommets_empruntes = new ArrayList<Noeud>();
		temps_total = 0 ;
		distance_totale = 0 ;
		this.nom_chemin = nom_chemin ;		
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
	 * ajouter une route empruntée à la liste des routes empruntées
	 * => si liste nulle, on ajoute la 1ere liaison
	 * => si liste non nulle, on ajoute  la liaison et on update le temps et la distance
	 */
	
	public void addRoute (Noeud sommet_next, boolean choix){
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
	
	/**
	 * Dessiner l'emsemble de chemin
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone)	{
		liste_sommets_empruntes.get(0).dessiner(dessin);
		for(Liaison route: routesEmpruntes)
			route.dessiner(dessin, zone);
		liste_sommets_empruntes.get(liste_sommets_empruntes.size() - 1).dessiner(dessin);
	}
	
	
}