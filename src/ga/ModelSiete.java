package ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.function.Consumer;

public class ModelSiete {
	// pocet vozidiel v zdroji i 
	private int Ni[];
	//kapacita vozidiel v zdroji i
	private int Ki[];
	//pocet obyvatelov v zdroji j
	private int Bj[];
	//cas presunu z komunity j do jej prideleneho utociska v minutach
	private double Sj[];
	//cas presunu zo zdroja i do komunity j
	private double pomTij[][];
	//cas presunu zo zdroja i do komunity j
	private double Tij[][];
	// cas pre ktory urcite neexistuje riesenie
	private final int MinCas = 275;
	// index zdroja i v matici Tij
	private int indexiI[];
	// index komunity j v matici Tij
	private int indexiJ[];
	@SuppressWarnings("resource")
	public ModelSiete() {
		Scanner scPoctyVozidiel;
		Scanner scKapacityVozidiel;
		Scanner scPoctyObyvatelov;
		Scanner scCasyPresunovDoUtocisk;
		Scanner scMaticaTij;
		Scanner scIndexI;
		Scanner scIndexJ;
		try {
			scPoctyVozidiel = new Scanner(new File("./02/1N.txt"));
			scKapacityVozidiel = new Scanner(new File("./02/1K.txt"));
			scPoctyObyvatelov = new Scanner(new File("./02/1B.txt"));
			scCasyPresunovDoUtocisk = new Scanner(new File("./02/1Sj.txt"));
			scMaticaTij = new Scanner(new File("./02/1Tij.txt"));
			scIndexI = new Scanner(new File("./02/1IdxI_.txt"));
			scIndexJ = new Scanner(new File("./02/1IdxJ_.txt"));
		} catch (FileNotFoundException e) {
			scPoctyVozidiel = new Scanner("./02/1N.txt");
			scKapacityVozidiel = new Scanner("./02/1K.txt");
			scPoctyObyvatelov = new Scanner("./02/1B.txt");
			scCasyPresunovDoUtocisk = new Scanner("./02/1Sj.txt");
			scMaticaTij = new Scanner("./02/1Tij.txt");
			scIndexI = new Scanner("./02/1IdxI_.txt");
			scIndexJ = new Scanner("./02/1IdxJ_.txt");
		}
		
		
		//inicializcia poli
		
		indexiI = new int[scIndexI.nextInt()];
		indexiJ = new int[scIndexJ.nextInt()];
		Tij = new double[indexiI.length][indexiJ.length];
		Ni = new int[scPoctyVozidiel.nextInt()];
		Ki = new int[scKapacityVozidiel.nextInt()];
		Bj = new int[scPoctyObyvatelov.nextInt()];
		Sj = new double[scCasyPresunovDoUtocisk.nextInt()];
		pomTij = new double[scMaticaTij.nextInt()][scMaticaTij.nextInt()];
		// naplnovanie dat tykajucich sa vozidiel a zdrojov
		for(int i = 0; i<indexiI.length;i++) {
			indexiI[i] = scIndexI.nextInt();
			Ni[i] = scPoctyVozidiel.nextInt();
			Ki[i] = scKapacityVozidiel.nextInt();
		}
		// naplnovanie dat tykajucich sa komodit a obyvatelov
		// pomocne premenne pre nacitanie desatinnzych cisel
		double pom;
		String s;
		scCasyPresunovDoUtocisk.nextLine();
		for(int j = 0; j<indexiJ.length;j++) {
			indexiJ[j] = scIndexJ.nextInt();
			s = scCasyPresunovDoUtocisk.nextLine();
			pom = Double.parseDouble(s);
			Sj[j] = pom;
			Bj[j] = scPoctyObyvatelov.nextInt();
		}
		// naplnenei pomocnej matice casu
		scMaticaTij.nextLine();
		for(int i = 0; i<pomTij.length;i++) {
			for(int j = 0; j<pomTij[0].length;j++) {
				s = scMaticaTij.nextLine();
				pom = Double.parseDouble(s);
				pomTij[i][j] = pom;
			}
		}
		// naplnenie matice s casom prepravy zo zdrojov do komunit
		for(int i = 0; i<indexiI.length;i++) {
			System.out.println();
			for(int j = 0;j<indexiJ.length;j++) {
				Tij[i][j] = pomTij[indexiI[i]-1][indexiJ[j]-1];
			}
		}
		
		
	}
}
