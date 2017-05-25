package application;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.poi.xssf.usermodel.*;

import base.Dessin;
import base.DessinInvisible;
import base.Openfile;
import base.Readarg;
import core.Graphe;
import core.algorithme.dijkstra.Pcc;
import core.algorithme.test.PccStarTest;
import core.algorithme.test.PccTest;
import core.graphe.Critere;
import exceptions.SommetNonExisteException;

/**
 * Cette classe est dedie a genere les tests automatiquement
 */
public class EvaluationPerformance {
	
	public static void main(String[] args) {
		
		EvaluationPerformance eval = new EvaluationPerformance(new Readarg(args));
		try {
			eval.run();
		} catch (SommetNonExisteException | IOException e) {
			e.printStackTrace();
		}

	}
	
	private XSSFWorkbook workbook;
	private String nomFichier;
	private int nbLoop;			//TODO ingorer pour l'instant
	private String[] maps;
	private Random rd;
	
	public EvaluationPerformance(Readarg inputUser)	{

		// init from user
		System.out.println("Test generator");
		String nomConcat = inputUser.lireString("Nom des cartes separe par espace (\" \"): ");
		this.maps = nomConcat.split(" ");
		this.nbLoop = inputUser.lireInt("Le nombre de test par carte: ");
		this.nomFichier = inputUser.lireString("Nom de fichier Excel (.xlsx): ");
		
		// default params
		this.rd = new Random();		
		workbook = new XSSFWorkbook();		
	}

	private void run() throws SommetNonExisteException, FileNotFoundException, IOException {
		
		// faire pour chaque carte:
		for(int idMap = 0; idMap < maps.length; idMap++)	{
			
			// init graphe
			DataInputStream mapdata = Openfile.open (this.maps[idMap]) ;
			Dessin dessin = new DessinInvisible();
			Graphe graphe = new Graphe(maps[idMap], mapdata, dessin);

			//init excel
			XSSFSheet sheet = this.workbook.createSheet(maps[idMap]);
			writeMapInfo(sheet, graphe);
			
			int offsetLigne = 5;	// ligne debut de remplire des donnees
			int offsetColumne = 10;  // debut de PccStar
			
			writeTitle(sheet,offsetColumne);
			
			for(int i = 0; i < this.nbLoop; i++)	{
				int numOrigine = rd.nextInt(graphe.getNombreNoeud());		// origine et dest aleatoire
				int numDestination = numOrigine;
				while(numDestination == numOrigine)	{
					numDestination = rd.nextInt(graphe.getNombreNoeud());
				} 
				
				// test Pcc en temps
				PccTest pcc = new PccTest(graphe, graphe.getNoeud(numOrigine), graphe.getNoeud(numDestination), Critere.TEMPS);
				pcc.run();	
				writeData(sheet.createRow(offsetLigne + i), 0, pcc);
				
				// test PccStar en temps
				PccStarTest pccStar = new PccStarTest(graphe, graphe.getNoeud(numOrigine), graphe.getNoeud(numDestination), Critere.TEMPS);
				pccStar.run();
				writeData(sheet.getRow(offsetLigne + i), offsetColumne, pccStar);
			}
			
		}
		
		workbook.write(new FileOutputStream(this.nomFichier + ".xlsx"));
		workbook.close();
		System.out.println("Test finished");
	}
	
	private void writeData(XSSFRow ligne, int offsetColumn, Pcc algo)	{
		ligne.createCell(0 + offsetColumn).setCellValue(algo.getOrigine().getNumero());
		ligne.createCell(1 + offsetColumn).setCellValue(algo.getDestination().getNumero());
		ligne.createCell(2 + offsetColumn).setCellValue(algo.hasSolution());
		ligne.createCell(3 + offsetColumn).setCellValue(algo.getTempsExcec());
		ligne.createCell(4 + offsetColumn).setCellValue(algo.getMaxTas());
		ligne.createCell(5 + offsetColumn).setCellValue(algo.getNbVisites());
		ligne.createCell(6 + offsetColumn).setCellValue(algo.getnbMarque());
	}
	
	private void writeMapInfo(XSSFSheet sheet, Graphe map)	{
		XSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Nom de carte: ");
		row.createCell(1).setCellValue(map.toString());
		
		row = sheet.createRow(1);
		row.createCell(0).setCellValue("Sommets: ");
		row.createCell(1).setCellValue(map.getNombreNoeud());
		
		row = sheet.createRow(2);
		row.createCell(0).setCellValue("Aretes: ");
		row.createCell(1).setCellValue(map.getNombreLiaison());
	}
	
	private void writeTitle(XSSFSheet sheet, int offsetColumn)	{	
		XSSFRow row = sheet.createRow(4);
		
		// titre pour pcc
		row.createCell(0).setCellValue("Depart");
		row.createCell(1).setCellValue("Destination");
		row.createCell(2).setCellValue("Existe Solution");
		row.createCell(3).setCellValue("CPU (ms)");
		row.createCell(4).setCellValue("Nombre noeud max dans le Tas");
		row.createCell(5).setCellValue("Nombre de noeud visite");
		row.createCell(6).setCellValue("Nombre de noeud marque");
		
		// titre pour pcc star
		row.createCell(0 + offsetColumn).setCellValue("Depart");
		row.createCell(1 + offsetColumn).setCellValue("Destination");
		row.createCell(2 + offsetColumn).setCellValue("Existe Solution");
		row.createCell(3 + offsetColumn).setCellValue("CPU (ms)");
		row.createCell(4 + offsetColumn).setCellValue("Nombre noeud max dans le Tas");
		row.createCell(5 + offsetColumn).setCellValue("Nombre de noeud visite");
		row.createCell(6 + offsetColumn).setCellValue("Nombre de noeud marque");
	}

}
