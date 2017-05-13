package core.algo_duc;
import java.awt.Color;
import  java.util. * ;

import base.Couleur;
import base.Dessin;
import core.graphe.Liaison;
import core.graphe.Noeud;
import exceptions.CheminNonOrigineException;
import exceptions.CheminNonRouteException;

/**
 *  permet de retrnir le chemin entre 2 sommets (origine et destination) avec les routes empruntées 
 */
public class Chemin {
	
	// listes des chemins empruntés dans l'ordre du chemin
	private Stack<Noeud> noeudsPasses ;
	
	/**
	 * liaison optimal à chaque fois qu'on passe une noeud à l'autre
	 */
	private Stack<Liaison> routesEmpruntes;
	
	private float longueur;
	
	public Chemin(Noeud origine){
		this(origine, null, null);
	}
	
	public Chemin(Noeud origine, List<Liaison> routes, List<Noeud> noeudsIntermediaires)	{
		
		// asset
		if(routes != null && noeudsIntermediaires != null && routes.size() != noeudsIntermediaires.size())
			throw new IllegalArgumentException("Nombre de routes et nombre de noeuds intermediaires non correspondant");
		
		// add noeuds
		noeudsPasses = new Stack<Noeud>();
		noeudsPasses.push(origine);
		if(noeudsIntermediaires != null)
			noeudsPasses.addAll(noeudsIntermediaires);
		
		// init routes
		this.routesEmpruntes = new Stack<Liaison>();
		if(routes != null)
			this.routesEmpruntes.addAll(routes);
		
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
				throw new IllegalArgumentException("Liaison ["+liaison+"] ne reprend pas le dernier noeud: ["+lastNoeud+"]");
			
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
			noeudsPasses.push(liaison.getSuccesseur());
		}	else	{
			Noeud newNoeud = liaison.getPredecesseur() == lastNoeud? liaison.getSuccesseur() : liaison.getPredecesseur();
			noeudsPasses.push(newNoeud);
		}
		routesEmpruntes.push(liaison);
		
		// update longueur
		longueur += liaison.getLongueur();
	}
	
	/**
	 * Supprimer le dernier route et dernier noeud ajouté
	 * @return Noeud: noeud retiré
	 * @throws CheminNonOrigineException
	 * @throws CheminNonRouteException
	 */
	public Noeud removeLastNoeud() throws CheminNonOrigineException, CheminNonRouteException	{
		if(noeudsPasses == null || noeudsPasses.isEmpty())
			throw new CheminNonOrigineException("Le chemin n'a pas d'origine, erreur dans construction!");
		
		if(noeudsPasses.size() == 1)
			throw new CheminNonRouteException();
		
		this.longueur -= routesEmpruntes.pop().getLongueur();	// update longueur, TODO check si ça marche correctement
		return noeudsPasses.pop();
	}
	
	/**
	 * Get longueur total de chemin
	 * @return float
	 */
	public float getLongueur()	{
		return longueur;
	}
	
	/**
	 * Get le noeud d'origine de chemin
	 * @return Noeud
	 */
	public Noeud getOrigine()	{
		return noeudsPasses.get(0);
	}
	
	/**
	 * Get le dernier noeud ajouté
	 * @return Noeud: noeud destinataire
	 */
	public Noeud getDestinataire()	{
		return noeudsPasses.peek();
	}
	
	/**
	 * Dessiner l'emsemble de chemin
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone, Color color)	{
		for(Liaison route: routesEmpruntes)
			route.dessiner(dessin, zone, color);
	}
	

	/**
	 * Dessiner le chemin avec la couleur par default
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone)	{
		dessiner(dessin, zone, null);
	}
	
	/**
	 * get le route le plus court entre 2 noeuds
	 * @param depart
	 * @param dest
	 * @return
	 */
	public static Liaison getLiaisonOptimal(Noeud depart, Noeud dest)	{
		// TODO à vérifier l'endroit plus propre pour mettre ce bout de code
		List<Liaison> routes = depart.getLiaisons(dest);
		Collections.sort(routes);
		return routes.get(0);
	}

	@Override
	public String toString()	{
		StringBuilder str = new StringBuilder();
		for(Noeud n : noeudsPasses)
			str.append(n.toString() + "\n");
		int length = str.length();
		str.delete(length-3, length-1);
		return "Chemin longueur "+longueur+" :\n[" + str.toString()+"]";
	}
}
