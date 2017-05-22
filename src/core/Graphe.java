package core ;



/**
 *   Classe representant un graphe.
 *   A vous de completer selon vos choix de conception.
 */

import java.io.* ;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.* ;
import core.graphe.Chemin;
import core.graphe.Critere;
import core.graphe.Liaison;
import core.graphe.Noeud;
import core.graphe.Segment;
import exceptions.SommetNonExisteException;

public class Graphe {

    // Nom de la carte utilisee pour construire ce graphe
    private final String nomCarte ;

    // Fenetre graphique
    private final Dessin dessin ;

    // Version du format MAP utilise'.
    private static final int version_map = 4 ;
    private static final int magic_number_map = 0xbacaff ;

    // Version du format PATH.
    private static final int version_path = 1 ;
    private static final int magic_number_path = 0xdecafe ;

    // Identifiant de la carte
    private int idcarte ;

    // Numero de zone de la carte
    private int numzone ;
    
    /**
     * Vitesse maximum existant sur la carte
     */
    private int vitesseMax;

    /*
     * Ces attributs constituent une structure ad-hoc pour stocker les informations du graphe.
     * Vous devez modifier et ameliorer ce choix de conception simpliste.
     */
    private float[] longitudes ;
    private float[] latitudes ;
    private Descripteur[] descripteurs ;
    
    private ArrayList<Noeud> noeuds;
    private List<Liaison> routes;
    
    // Deux malheureux getters.
    public Dessin getDessin() { return dessin ; }
    public int getZone() { return numzone ; }

    
    public Graphe (String nomCarte, DataInputStream dis, Dessin dessin) {

    	this.nomCarte = nomCarte ;
    	this.dessin = dessin ;
    	this.noeuds = new ArrayList<Noeud>();
    	this.routes = new ArrayList<Liaison>();
    	Utils.calibrer(nomCarte, dessin) ;
    	
    	// Lecture du fichier MAP. 
    	// Voir le fichier "FORMAT" pour le detail du format binaire.
    	try {

    	    // Nombre d'aretes
    	    int edges = 0 ;

    	    // Verification du magic number et de la version du format du fichier .map
    	    int magic = dis.readInt () ;
    	    int version = dis.readInt () ;
    	    Utils.checkVersion(magic, magic_number_map, version, version_map, nomCarte, ".map") ;

    	    // Lecture de l'identifiant de carte et du numero de zone, 
    	    this.idcarte = dis.readInt () ;
    	    this.numzone = dis.readInt () ;

    	    // Lecture du nombre de descripteurs, nombre de noeuds.
    	    int nb_descripteurs = dis.readInt () ;
    	    int nb_nodes = dis.readInt () ;

    	    // Nombre de successeurs enregistrés dans le fichier.
    	    int[] nsuccesseurs_a_lire = new int[nb_nodes] ;
    	    
    	    // En fonction de vos choix de conception, vous devrez certainement adapter la suite.
    	    this.longitudes = new float[nb_nodes] ;
    	    this.latitudes = new float[nb_nodes] ;
    	    this.descripteurs = new Descripteur[nb_descripteurs] ;
    	    

    	    // Lecture des noeuds
    	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
    	    	
	    		// Lecture du noeud numero num_node
	    		longitudes[num_node] = ((float)dis.readInt ()) / 1E6f ;
	    		latitudes[num_node] = ((float)dis.readInt ()) / 1E6f ;
	    		
	    		noeuds.add(new Noeud(num_node, longitudes[num_node], latitudes[num_node]));
	    		nsuccesseurs_a_lire[num_node] = dis.readUnsignedByte() ;
    	    }
    	    
    	    Utils.checkByte(255, dis) ;
    	    
    	    this.vitesseMax = 0;
    	    // Lecture des descripteurs
    	    for (int num_descr = 0 ; num_descr < nb_descripteurs ; num_descr++) {
	    		// Lecture du descripteur numero num_descr
	    		descripteurs[num_descr] = new Descripteur(dis) ;
	
	    		// On affiche quelques descripteurs parmi tous.
	    		//if (0 == num_descr % (1 + nb_descripteurs / 400))
	    		    //System.out.println("Descripteur " + num_descr + " = " + descripteurs[num_descr]) ;
	    		
	    		if(descripteurs[num_descr].vitesseMax() > this.vitesseMax)
	    			this.vitesseMax = descripteurs[num_descr].vitesseMax();
    	    }
    	    
    	    Utils.checkByte(254, dis) ;
    	    
    	    // Lecture des successeurs
    	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
    	    	Noeud predecesseur = noeuds.get(num_node);
    	    	
    	    	// Lecture de tous les successeurs du noeud num_node
	    		for (int num_succ = 0 ; num_succ < nsuccesseurs_a_lire[num_node] ; num_succ++) {
	    		    
	    		    int succ_zone = dis.readUnsignedByte() ;		// zone du successeur	
	    		    
	    		    int dest_node = Utils.read24bits(dis) ;			// numero de noeud du successeur	
	    		    
	    		    int descr_num = Utils.read24bits(dis) ;			// descripteur de l'arete
	    		  
	    		    int longueur  = dis.readUnsignedShort() ;		// longueur de l'arete en metres
	
	    		    int nb_segm   = dis.readUnsignedShort() ;		// Nombre de segments constituant l'arete
	
	    		    edges++ ;
	    		    	    		    
	    		    Noeud successeur = noeuds.get(dest_node);
	    		    successeur.setZone(succ_zone);
	    		    Descripteur descripteur = descripteurs[descr_num];
	    		    Liaison route = new Liaison(predecesseur, successeur, longueur, descripteur);
	    		    Liaison routeInverse = descripteur.isSensUnique()? null : new Liaison(successeur, predecesseur, longueur, descripteur);
	    		    
	    		    // Chaque segment est dessine'
	    		    for (int i = 0 ; i < nb_segm ; i++) {
		    			float delta_lon = (dis.readShort()) / 2.0E5f ;
		    			float delta_lat = (dis.readShort()) / 2.0E5f ;
		    			Segment segment = new Segment(delta_lon, delta_lat);
		    			route.addSegment(segment);
		    			if(routeInverse != null)
		    				routeInverse.addSegment(segment);
	    		    }
	    		    
	    		    routes.add(route);
	    		    if(routeInverse != null)
	    		    	routes.add(routeInverse);
	    		}
    	    }
    	    
    	    this.dessiner();
    	    Utils.checkByte(253, dis) ;

    	    System.out.println("Fichier lu : " + nb_nodes + " sommets, " + edges + " aretes, " 
    			       + nb_descripteurs + " descripteurs.") ;

    	} catch (IOException e) {
    	    e.printStackTrace() ;
    	    System.exit(1) ;
    	}

    }
    
    /**
     * get la vitesse Maximal existante sur la carte
     * @return
     */
    public int getVitesseMax()	{
    	return this.vitesseMax;
    }
    
    /**
     * Get tous les noeuds de graphe.<br/>
     * <b>ceci n'est qu'une copie de la liste, toutes modification ne sera ignoré</b>
     * @return List Noeud
     */
    public ArrayList<Noeud> getNoeuds()	{
    	return noeuds;
    }
    
    /**
     * get le noeud correspond à numéro dans l'agrument
     * @param numNoeud
     * @return le Noeud correspond à numNoeud
     * @throws SommetNonExisteException 
     */
    public Noeud getNoeud(int numNoeud) throws SommetNonExisteException	{
    	try	{
    		return getNoeuds().get(numNoeud);    		
    	} catch(IndexOutOfBoundsException e)	{
    		throw new SommetNonExisteException("Noeud n° "+numNoeud+" n'existe pas dans la carte "+this);
    	}
    }
    
    /**
     * Get les routes de graphe.<br/>
     * <b>ceci n'est qu'une copie de la liste, toutes modification ne sera ignoré</b>
     * @return List Liaison
     */
    public List<Liaison> getRoutes()	{
    	return new LinkedList<Liaison>(routes);
    }
    
    @Override
    public String toString()	{
    	return this.nomCarte;
    }
    
    /*
     * Dessiner tous les routes et les noeud dans un graphe à couleur par défaut
     */
    public void dessiner()	{
    	for(Liaison route: this.routes)	
    		route.dessiner(dessin, numzone);
    	
    	for(Noeud noeud: this.noeuds)	
    		noeud.dessiner(dessin, null);
    }
    
    /**
     * Get numero de zone d'un sommet
     * @param numSommet
     * @return
     * @throws SommetNonExisteException
     */
    public int getZone(int numSommet) throws SommetNonExisteException	{
    	if(numSommet < 0 || numSommet >= noeuds.size())
    		throw new SommetNonExisteException();
    	return this.noeuds.get(numSommet).getZone();
    }
    
    // Rayon de la terre en metres
    private static final double rayon_terre = 6378137.0 ;

    /**
     *  Calcule de la distance orthodromique - plus court chemin entre deux points à la surface d'une sphère
     *  @param long1 longitude du premier point.
     *  @param lat1 latitude du premier point.
     *  @param long2 longitude du second point.
     *  @param lat2 latitude du second point.
     *  @return la distance entre les deux points en metres.
     *  Methode Ã©crite par Thomas Thiebaud, mai 2013
     */
    public static double distance(double long1, double lat1, double long2, double lat2) {
        double sinLat = Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2));
        double cosLat = Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));
        double cosLong = Math.cos(Math.toRadians(long2-long1));
        return rayon_terre*Math.acos(sinLat+cosLat*cosLong);
    }

    public double distance_sommets (int sommet1, int sommet2){
    	double latA = this.noeuds.get(sommet1).getLatitude();
    	double latB = this.noeuds.get(sommet2).getLatitude();
    	double longA = this.noeuds.get(sommet1).getLongitude();
    	double longB = this.noeuds.get(sommet2).getLongitude();
    	return distance(longA,latA,longB,latB) ;
    }
    /**
     *  Attend un clic sur la carte et affiche le numero de sommet le plus proche du clic.
     *  A n'utiliser que pour faire du debug ou des tests ponctuels.
     *  Ne pas utiliser automatiquement a chaque invocation des algorithmes.
     */
    public void situerClick() {

	System.out.println("Allez-y, cliquez donc.") ;
	
	if (dessin.waitClick()) {
	    float lon = dessin.getClickLon() ;
	    float lat = dessin.getClickLat() ;
	    
	    System.out.println("Clic aux coordonnees lon = " + lon + "  lat = " + lat) ;

	    // On cherche le noeud le plus proche. O(n)
	    float minDist = Float.MAX_VALUE ;
	    int   noeud   = 0 ;
	    
	    for (int num_node = 0 ; num_node < longitudes.length ; num_node++) {
		float londiff = (longitudes[num_node] - lon) ;
		float latdiff = (latitudes[num_node] - lat) ;
		float dist = londiff*londiff + latdiff*latdiff ;
		if (dist < minDist) {
		    noeud = num_node ;
		    minDist = dist ;
		}
	    }

	    System.out.println("Noeud le plus proche : " + noeud) ;
	    System.out.println() ;
	    dessin.setColor(java.awt.Color.red) ;
	    dessin.drawPoint(longitudes[noeud], latitudes[noeud], 5) ;
	}
    }

    /**
     *  Charge un chemin depuis un fichier .path (voir le fichier FORMAT_PATH qui decrit le format)
     *  Verifie que le chemin est empruntable et calcule le temps de trajet.
     */
    public void verifierChemin(DataInputStream dis, String nom_chemin) {
	
	try {
	    
	    // Verification du magic number et de la version du format du fichier .path
	    int magic = dis.readInt () ;
	    int version = dis.readInt () ;
	    Utils.checkVersion(magic, magic_number_path, version, version_path, nom_chemin, ".path") ;

	    // Lecture de l'identifiant de carte
	    int path_carte = dis.readInt () ;

	    if (path_carte != this.idcarte) {
		System.out.println("Le chemin du fichier " + nom_chemin + " n'appartient pas a la carte actuellement chargee." ) ;
		System.exit(1) ;
	    }

	    int nb_noeuds = dis.readInt () ;

	    // Origine du chemin
	    int first_zone = dis.readUnsignedByte() ;
	    int first_node = Utils.read24bits(dis) ;

	    // Destination du chemin
	    int last_zone  = dis.readUnsignedByte() ;
	    int last_node = Utils.read24bits(dis) ;

	    System.out.println("Chemin de " + first_zone + ":" + first_node + " vers " + last_zone + ":" + last_node) ;

	    int current_zone = 0 ;
	    int current_node = 0 ;

	    Chemin chemin = null;
	    
	    // Tous les noeuds du chemin
	    for (int i = 0 ; i < nb_noeuds ; i++) {
			current_zone = dis.readUnsignedByte() ;
			current_node = Utils.read24bits(dis) ;
			
			if(i == 0)
				chemin = new Chemin(this.getNoeuds().get(current_node));
			else
				chemin.addSommet(this.getNoeuds().get(current_node), Critere.VITESSE);
	    }

	    if ((current_zone != last_zone) || (current_node != last_node)) {
		    System.out.println("Le chemin " + nom_chemin + " ne termine pas sur le bon noeud.") ;
		    System.exit(1) ;
		}

	} catch (IOException e) {
	    e.printStackTrace() ;
	    System.exit(1) ;
	}

    }

}
