package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jedinec {
	private int zoznamZdrojov[];
	private int zoznamKomunit[];
	private static final Random gen = new Random();
	private static final Random genMutacie = new Random();
	private int fittness;
	public Jedinec(int pocetZdrojov, int pocetKomunit) {
		zoznamZdrojov = new int[pocetZdrojov];
		zoznamKomunit = new int[pocetKomunit];
		List<Integer> poctyZdrojov = new ArrayList<>(pocetZdrojov);
		List<Integer> poctyKomunit = new ArrayList<>(pocetKomunit);
		// nahodna inicialiyacia poradia spracovania pre greedy heuristiku
		for(int i = 0; i<pocetZdrojov;i++) {
			poctyZdrojov.add(i);
		}
		for(int i = 0; i<pocetZdrojov;i++) {
			zoznamZdrojov[i] = poctyZdrojov.remove(gen.nextInt(poctyZdrojov.size()));
		}
		for(int j = 0; j<pocetKomunit;j++) {
			poctyKomunit.add(j);
		}
		for(int j = 0; j<pocetKomunit;j++) {
			zoznamKomunit[j]= poctyKomunit.remove(gen.nextInt(poctyKomunit.size()));
		}
	}
	public Jedinec(Jedinec jedinec) {
		zoznamZdrojov = new int[jedinec.zoznamZdrojov.length];
		zoznamKomunit = new int[jedinec.zoznamKomunit.length];
		fittness = jedinec.fittness;
		for(int i = 0; i< zoznamZdrojov.length;i++) {
			zoznamZdrojov[i] = jedinec.zoznamZdrojov[i];
		}
		for(int j = 0; j<zoznamKomunit.length;j++) {
			zoznamKomunit[j] = jedinec.zoznamKomunit[j];
		}
	}
	public Jedinec(int[] zoznamZdrojov2, int[] zoznamKomunit2) {
		zoznamZdrojov = zoznamZdrojov2;
		zoznamKomunit = zoznamKomunit2;
	}
	public int[] getZoznamZdrojov() {
		return zoznamZdrojov;
	}
	public void setZoznamZdrojov(int[] zoznamZdrojov) {
		this.zoznamZdrojov = zoznamZdrojov;
	}
	public int[] getZoznamKomunit() {
		return zoznamKomunit;
	}
	public void setZoznamKomunit(int[] zoznamKomunit) {
		this.zoznamKomunit = zoznamKomunit;
	}
	public int getPocetNeevakuovanych() {
		return fittness;
	}
	public void setPocetNeevakuovanych(int fittness) {
		this.fittness = fittness;
	}
	public void zmutuj(double percentoMutacie) {
		int pom;
		for(int i = 0; i<zoznamKomunit.length-1;i++) {
			if(genMutacie.nextDouble()<=percentoMutacie) {
				pom = zoznamKomunit[i];
				zoznamKomunit[i] = zoznamKomunit[i+1];
				zoznamKomunit[i+1] = pom;
			}
		}
		for(int i = 0; i<zoznamZdrojov.length-1;i++) {
			if(genMutacie.nextDouble()<=percentoMutacie) {
				pom = zoznamZdrojov[i];
				zoznamZdrojov[i] = zoznamZdrojov[i+1];
				zoznamZdrojov[i+1] = pom;
			}
		}
	}
	public String toString() {
		String string = "";
		string+="Poradie zdrojov:"+System.lineSeparator();
		for(int i = 0; i<zoznamZdrojov.length;i++) {
			string+=(zoznamZdrojov[i]+1)+" ";
		}
		string+=System.lineSeparator();
		string+="Poradie komunit:"+System.lineSeparator();
		for(int i = 0; i<zoznamKomunit.length;i++) {
			string+=(zoznamKomunit[i]+1)+" ";
		}
		string+=System.lineSeparator();
		string+="pocet neevakuovanych: "+fittness;
		return string;
	}
}
