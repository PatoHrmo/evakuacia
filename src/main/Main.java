package main;

import ga.GenetickyAlg;
import ga.ModelSiete;

public class Main {

	public static void main(String[] args) {
		ModelSiete modelSiete = new ModelSiete();
		modelSiete.inicializujAij(277.5);
		GenetickyAlg alg = new GenetickyAlg(10, 0.1, 200, modelSiete);
		alg.run();
	}

}
