package core.algorithme.dijkstra;

import core.graphe.*;

public class Label implements Comparable<Label> {
	
	final protected static float PRECISION = 100000f;
	private boolean marquage;
	private float cout;
	private Label pere;
	private Noeud sommetCourant;

	// Je sauvegarde ici la liaison optimale trouvé à chaque fois on maj son père
	private Liaison liaisonOptimal;
	
	public Label(Noeud sommetCourant)	{
		if(sommetCourant == null)
			throw new IllegalArgumentException("sommet courant est null\n");
		
		this.sommetCourant = sommetCourant;
		this.marquage = false;
		
		this.cout = Float.MAX_VALUE ;
		this.pere = null ; 
		this.liaisonOptimal = null;
	}
	
	public Label(Label label)	{
		this.marquage = label.marquage;
		this.cout = label.cout;
		this.pere = null;	// TODO on doit cloner aussi ou pas le label pere?
		this.sommetCourant = label.sommetCourant;
	}
	
	public boolean isMarque()	{
		return marquage;
	}
	
	public void setMarquage(boolean marque)	{
		this.marquage = marque;
	}
	
	public void setPere(Label pere){
		this.pere = pere ;
	}
	
	public void setCout(float cout){
		this.cout = cout ;
	}
	
	public Label getPere (){
		return this.pere ;
	}
	
	public float getCout(){
		return this.cout ;
	}
	public Noeud getSommetCourant(){
		return sommetCourant ;
	}

	public int compareTo(Label label) { 
		//TODO : conseil prof : return -1 ou 0 ou 1 plutot! comme ça pas besoin de précision 
		return (int)((this.cout - label.cout)*PRECISION) ;
	}
	
	/**
	 * Maj son père et liaison de père vers sommet courant
	 * @param pere
	 * @param liaison
	 */
	public void update(Label pere, Liaison liaison, Critere critere)	{
		this.pere = pere;
		this.liaisonOptimal = liaison;
		
		switch (critere)	{
		case TEMPS:
			this.cout = pere.cout + liaison.getLongueur()/liaison.getVitesseMax();
			break;
		case DISTANCE:
			this.cout = pere.cout + liaison.getLongueur();
			break;
		case VITESSE:
			this.cout = pere.cout + liaison.getLongueur();
			break;
		default:
			throw new IllegalArgumentException("Critere num " + critere + " non connu");
			
		}			
	}
	
	public Liaison getLiaison()	{
		return liaisonOptimal;
	}
	
	@Override
	public boolean equals(Object label)	{
		return label instanceof Label && ((Label) label).getSommetCourant().equals(this.getSommetCourant());
	}
	
	@Override
	public String toString()	{
		return "Label no "+sommetCourant.getNumero()+" - cout = "+cout;
	}
}
