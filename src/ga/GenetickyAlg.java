package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenetickyAlg {
	private Jedinec[] staraPop;
	private Jedinec[] novaPop;
	private double fitStarej;
	private double fitNovej;
	private Jedinec najJedinec;
	private int velkostPopulacie;
	private double percentoMutacie;
	private int pocetGeneraciiBezZmeny;
	private ModelSiete modelSiete;
	private Random genPociatkuSelekcie;
	private Random genCrossover;
	public GenetickyAlg(int velkostPopulacie, double percentoMutacie, int pocetGeneracii, ModelSiete modelSiete) {
		this.velkostPopulacie = velkostPopulacie;
		this.percentoMutacie = percentoMutacie;
		this.pocetGeneraciiBezZmeny = pocetGeneracii;
		this.modelSiete = modelSiete;
		staraPop = new Jedinec[velkostPopulacie];
		novaPop = new Jedinec[velkostPopulacie];
		genPociatkuSelekcie = new Random();
		genCrossover = new Random();
		najJedinec = modelSiete.generujJedinca();
	}
	/**
	 * metoda spustajuca hlavny algoritmus
	 */
	public void run() {
		if(inicializujPopulaciu()) {
			System.out.println("riesenie sa naslo uz pri inicializacii populacie");
			System.out.println(najJedinec.toString());
			return;
		}
		for(int i = 0; i<pocetGeneraciiBezZmeny;i++) {
			// nasiel sa lepsi jedinec
			if(vytvorNovuPopulaciu()) {
				System.out.println("reset vuuu"+i);
				i = 0;
				
			}
			zmutujNovuPopulaciu();
			if(skopirujNovuPopulaciuDoStarej()) {
				System.out.println("riesenie sa naslo v generaci "+i);
				System.out.println(najJedinec.toString());
				return;
			};
		}
		System.out.println("riesenie sa nenaslo. Najlepsi jedinec: ");
		System.out.println(najJedinec.toString());
		
	}
	/**
	 * vytvori novu populaciu novaPop zo starej populacie staraPop
	 * @return true ak sa v novej populacii vyskytol lepsi jedinec ako doteraz najdeny inak false
	 */
	private boolean vytvorNovuPopulaciu() {
		boolean vylepsenie = false;
		for(int i = 0; i<velkostPopulacie;i++) {
			Jedinec novyJedinec = vytvorJedincaZoStarejPopulacie();
			modelSiete.vypocitajPocetNeevakuovanych(novyJedinec);
			novaPop[i] = novyJedinec;
			if(novyJedinec.getPocetNeevakuovanych()<najJedinec.getPocetNeevakuovanych()) {
				najJedinec = novyJedinec;
				vylepsenie = true;
			}
		}
		return vylepsenie;
	}
    /**
     * vytvori jednica zo starej populacie pomocou selekcie SUS a krizenia UX
     * @return novy jedinec
     */
	private Jedinec vytvorJedincaZoStarejPopulacie() {
		// vyberam dvoch rodicov
		double vzdialenostMedziRodicmy = fitStarej/2;
		// startovny bod SUS
		double startovnyBod = genPociatkuSelekcie.nextDouble()*vzdialenostMedziRodicmy;
		double bodyVybratiaRodica[] = new double[2];
		// definovanie bodov velkosti fittnes, pri ktorych prekroceni vyberiem rodica
		bodyVybratiaRodica[0] = startovnyBod;
		bodyVybratiaRodica[1] = startovnyBod+vzdialenostMedziRodicmy;
		return crossover(vyberRodicov(bodyVybratiaRodica));
	}
	/**
	 * vyberie zo starej populacie dvoch rodicov, na zaklade zlomovych bodov sumy fittness
	 * @param bodyVybratiaRodica pole zlomov
	 * @return pole rodicov
	 */
	private Jedinec[] vyberRodicov(double[] bodyVybratiaRodica) {
		Jedinec rodicia[] = new Jedinec[2];
		int vypocetRodica = 0;
		double sumaFitt = 0;
		for(double bodRodica : bodyVybratiaRodica) {
			int i = 0;
			while(sumaFitt<bodRodica) {
				i++;
				sumaFitt+=1d/staraPop[i].getPocetNeevakuovanych();
			}
			rodicia[vypocetRodica] = staraPop[i];
			vypocetRodica++;
		}
		return rodicia;
	}
	/**
	 * vytvori z rodicov noveho potomka
	 * @param rodicia pole rodicov
	 * @return novy jedinec vyniknuty krizenim
	 */ 
	private Jedinec crossover(Jedinec[] rodicia) {
		
		//vytvorenie listov pre UX crossover
		List<Integer> zoznamyKomunit1 = new ArrayList<>(rodicia[0].getZoznamKomunit().length);
		List<Integer> zoznamyKomunit2 = new ArrayList<>(rodicia[0].getZoznamKomunit().length);
		List<Integer> zoznamyZdrojov1 = new ArrayList<>(rodicia[0].getZoznamZdrojov().length);
		List<Integer> zoznamyZdrojov2 = new ArrayList<>(rodicia[0].getZoznamZdrojov().length);
		for(int i = 0; i<rodicia[0].getZoznamKomunit().length;i++) {
			zoznamyKomunit1.add(rodicia[0].getZoznamKomunit()[i]);
			zoznamyKomunit2.add(rodicia[1].getZoznamKomunit()[i]);
		}
		for(int i = 0; i<rodicia[0].getZoznamZdrojov().length;i++) {
			zoznamyZdrojov1.add(rodicia[0].getZoznamZdrojov()[i]);
			zoznamyZdrojov2.add(rodicia[1].getZoznamZdrojov()[i]);
		}
		int zoznamKomunit[] = new int[rodicia[0].getZoznamKomunit().length];
		int zoznamZdrojov[] = new int[rodicia[0].getZoznamZdrojov().length];
		// krizenie zoznamu komunit
		for(int i = 0; i< zoznamKomunit.length;i++) {
			if(genCrossover.nextBoolean()) {
				zoznamKomunit[i] = zoznamyKomunit1.remove(0);
				zoznamyKomunit2.remove(Integer.valueOf(zoznamKomunit[i]));
			} else {
				zoznamKomunit[i] = zoznamyKomunit2.remove(0);
				zoznamyKomunit1.remove(Integer.valueOf(zoznamKomunit[i]));
			}
		}
		// krizenie zoznamu zdrojov
		for(int i = 0; i<zoznamZdrojov.length;i++) {
			if(genCrossover.nextBoolean()) {
				zoznamZdrojov[i] = zoznamyZdrojov1.remove(0);
				zoznamyZdrojov2.remove(Integer.valueOf(zoznamZdrojov[i]));
			} else {
				zoznamZdrojov[i] = zoznamyZdrojov2.remove(0);
				zoznamyZdrojov1.remove(Integer.valueOf(zoznamZdrojov[i]));
			}
		}
		Jedinec jedinec = new Jedinec(zoznamZdrojov, zoznamKomunit);
		return jedinec;
	}
    /*
     * pokusi sa ymutovat kazdeho clena novej populacie na zaklade percenta mutacie
     */
	private void zmutujNovuPopulaciu() {
		for(int i = 0; i<velkostPopulacie;i++) {
			novaPop[i].zmutuj(percentoMutacie);
		}
	}
	/**
	 * skopiruje novu populaciu do starej a skontroluje ci neexistuje jedinec so vsetkymi obyvatelmi evakuovanimi
	 * @return true ak sa nasiel jedinec so vsetkymi evakuovanymi obyvatelmi inak false
	 */
	private boolean skopirujNovuPopulaciuDoStarej() {
		fitStarej = 0;
		for(int i = 0; i<velkostPopulacie;i++) {
			staraPop[i] = new Jedinec(novaPop[i]);
			if(staraPop[i].getPocetNeevakuovanych()==0) {
				najJedinec = staraPop[i];
				return true;
			}
			fitStarej+= 1d/staraPop[i].getPocetNeevakuovanych();
		}
		return false;
		
	}
    /**
     * inicializuje staru populaciu
     * @return true ak sa naslo riesenie pri ktorom boli evakuovany vsetci inak false
     */
	private boolean inicializujPopulaciu() {
		fitStarej = 0;
		for(int i = 0; i< velkostPopulacie;i++) {
			staraPop[i] = modelSiete.generujJedinca();
			if(staraPop[i].getPocetNeevakuovanych()==0) {
				najJedinec = staraPop[i];
				return true;
			}
			fitStarej+=1d/staraPop[i].getPocetNeevakuovanych();
		}
		return false;
		
	}
	
}
