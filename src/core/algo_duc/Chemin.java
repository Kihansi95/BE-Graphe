package core.algo_duc;
import  java.util. * ;

import base.Dessin;
import core.graphe.Liaison;
import core.graphe.Noeud;

/**
 *  permet de retrnir le chemin entre 2 sommets (origine et destination) avec les routes empruntées 
 */
public class Chemin {
	
	// listes des chemins empruntés dans l'ordre du chemin
	private List<Noeud> noeudsPasses ;
	
	/**
	 * liaison optimal à chaque fois qu'on passe une noeud à l'autre
	 */
	private List<Liaison> routesEmpruntes;
	
	private float longueur;
	
	public Chemin(Noeud origine){
		this(origine, null, null);
	}
	
	private Chemin(Noeud origine, List<Liaison> routes, List<Noeud> noeudsIntermediaires)	{
		
		// asset
		if(routes != null && noeudsIntermediaires != null && routes.size() != noeudsIntermediaires.size())
			throw new IllegalArgumentException("Nombre de routes et nombre de noeuds intermediaires non correspondant");
		
		// add noeuds
		noeudsPasses = new ArrayList<Noeud>();
		noeudsPasses.add(origine);
		noeudsPasses.addAll(noeudsIntermediaires);
		
		// init list or routes and noeuds
		this.routesEmpruntes = routes == null? new LinkedList<Liaison>() : routes;
		this.noeudsPasses = noeudsIntermediaires == null? new LinkedList<Noeud>() : noeudsIntermediaires;
		
		// calcul longueur
		longueur = 0;
		for(Liaison liaison : routesEmpruntes)
			longueur += liaison.getLongueur();

	}
	
	/**
	 * Ajouter la nouvelle liaison vers le prochain noeud dans ce chemin
	 * @param liaison
	 */
	public void addRoute(Liaison liaison)	{
		
		// asset
		Noeud lastNoeud = noeudsPasses.get(noeudsPasses.size() - 1);
		if(liaison.getDescripteur().isSensUnique())	{
			
			// vérif si prédécesseur est bien la fin de chemin
			if(!liaison.getPredecesseur().equals(lastNoeud))
				throw new IllegalArgumentException("Liaison ["+liaison+"] ne reprend pas du dernier noeud: ["+lastNoeud+"]");
			
			// vérif si successeur présent dans chemin (cycle)
			if(noeudsPasses.contains(liaison.getSuccesseur()))
				throw new IllegalArgumentException("Liaison ["+liaison+"] revient sur le noeud dans chemin: ["+lastNoeud+"]");
		} else	{
			
			// vérif si les 2 extremités présents dans chemin (cycle)
			if(noeudsPasses.contains(liaison.getPredecesseur()) && noeudsPasses.contains(liaison.getSuccesseur()))
				throw new IllegalArgumentException("Cycle detecté: Liaison ["+liaison+"] reprend 2 noeuds dans chemin"+this);
		}
		
		// add new noeud and new liaison
		if(liaison.getDescripteur().isSensUnique())	{
			noeudsPasses.add(liaison.getSuccesseur());
		}	else	{
			Noeud newNoeud = liaison.getPredecesseur() == lastNoeud? liaison.getSuccesseur() : liaison.getPredecesseur();
			noeudsPasses.add(newNoeud);
		}
		routesEmpruntes.add(liaison);
		
		// update longueur
		longueur += liaison.getLongueur();
	}
	
	public float getLongueur()	{
		return longueur;
	}
	
	/**
	 * Dessiner l'emsemble de chemin
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone)	{
		for(Liaison route: routesEmpruntes)
			route.dessiner(dessin, zone);
		routesEmpruntes.get(routesEmpruntes.size() - 1).dessiner(dessin, zone);
	}
	
	/**
	 * get le route le plus court entre 2 noeuds
	 * @param depart
	 * @param dest
	 * @return
	 */
	public static Liaison getLiaisonOptimal(Noeud depart, Noeud dest)	{
		// TODO à vérifier l'endroit plus propre pour mettre ce bout de code
		List<Liaison> routes = depart.getLiaisons_1vers2(dest);
		Collections.sort(routes);
		return routes.get(0);
	}

	@Override
	public String toString()	{
		StringBuilder str = new StringBuilder();
		for(Noeud n : noeudsPasses)
			str.append(n.toString() + " - ");
		str.delete(-2, -1);
		return "Chemin longueur "+longueur+" :" + str.toString();
	}
}
