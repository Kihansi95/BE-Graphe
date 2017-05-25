package core.graphe;
import java.awt.Color;
import  java.util. * ;

import base.Couleur;
import base.Dessin;
import exceptions.CheminNonOrigineException;
import exceptions.CheminNonRouteException;
import exceptions.SuccesseurNonExistantException;

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
	
	/**
	 * Clone d'un chemin
	 * @param chemin
	 */
	public Chemin(final Chemin chemin)	{
		if(chemin != null)	{
			this.noeudsPasses.addAll(chemin.noeudsPasses);
			this.routesEmpruntes.addAll(chemin.routesEmpruntes);			
		}
	}
	
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

	}
	
	/**
	 * Ajouter la nouvelle liaison vers le prochain noeud dans ce chemin
	 * @param liaison
	 */
	public void addRoute(Liaison liaison)	{
		
		// asset
		Noeud lastNoeud = noeudsPasses.get(noeudsPasses.size() - 1);
		
		if(liaison.isSensUnique())	{
			
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
		if(liaison.isSensUnique())	{
			noeudsPasses.push(liaison.getSuccesseur());
		}	else	{
			Noeud newNoeud = liaison.getPredecesseur().equals(lastNoeud)? liaison.getSuccesseur() : liaison.getPredecesseur();
			noeudsPasses.push(newNoeud);
		}
		routesEmpruntes.push(liaison);
		
	}
	
	public void addSommet(Noeud noeud, Critere critere)	{
		
		// asset
		if(this.noeudsPasses.contains(noeud))
			throw new IllegalArgumentException("Noeud "+noeud+ " existe déjà dans le chemin");
		
		try	{
			
			Liaison optimum = getDestinataire().getLiaisonOptimal(noeud, critere);
			this.routesEmpruntes.add(optimum);
			this.noeudsPasses.add(noeud);
			
		} catch (SuccesseurNonExistantException e)	{
			throw new IllegalArgumentException("Noeud "+noeud+" n'est pas en mesure de la continuite de chemin");
		}
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
		
		return noeudsPasses.pop();
	}
		
	/**
	 * Calcul le cout de la route en fonction du choix
	 * @param choix
	 * @return
	 */
	public float coutRoute(Critere choix)	{
		
		float coutChemin = 0f;
		
		switch(choix)	{
		case TEMPS:
			for(Liaison route : this.routesEmpruntes)
				coutChemin +=  route.getLongueur() *60f / (route.getVitesseMax() * 1000f);
			break;
		case DISTANCE:
			for(Liaison route : this.routesEmpruntes)
				coutChemin += route.getLongueur();
			break;
		default:
			throw new IllegalArgumentException("Choix de cout non implemente: " + choix);
		}
		return coutChemin;
	}
	
	/**
	 * Get le noeud d'origine de chemin
	 * @return Noeud
	 */
	public Noeud getOrigine()	{
		return noeudsPasses.firstElement();
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
		for(Noeud noeud: noeudsPasses)
			noeud.dessiner(dessin, color);
	}
	

	/**
	 * Dessiner le chemin avec la couleur par default
	 * @param dessin
	 * @param zone
	 */
	public void dessiner(Dessin dessin, int zone)	{
		dessiner(dessin, zone, null);
	}

	@Override
	public String toString()	{
		StringBuilder str = new StringBuilder("Chemin de "+ this.getOrigine() +" vers "+ this.getDestinataire() + "\n");
		
		Iterator<Noeud> noeud = noeudsPasses.iterator();
		Iterator<Liaison> liaison = routesEmpruntes.iterator();
		
		str.append(noeud.next()+"\n");
		
		while(noeud.hasNext() && liaison.hasNext())	{
			str.append("--> " + noeud.next()+" par " + liaison.next() +"\n");
		}
		
		str.append("===========================\n");
		str.append("Longueur totale = "+ this.coutRoute(Critere.DISTANCE) +" m\n");
		str.append("Temps total = "+ this.coutRoute(Critere.TEMPS) + " minutes\n");

		return str.toString();
	}
}
