package core.algorithme;

import core.graphe.*;

public class Label implements Comparable<Label> {
	
	final protected static float PRECISION = 1000f;
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
		
		//TODO pour Duc
		this.liaisonOptimal = null;
	}
	
	public boolean isMarque()	{
		return marquage;
	}
	
	public void marquer()	{
		marquage = true;
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
		return (int)((this.cout - label.cout)*1000f) ;
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
			this.cout = pere.cout + liaison.getLongueur()/liaison.getDescripteur().vitesseMax();
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
