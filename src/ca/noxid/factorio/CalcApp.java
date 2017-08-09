package ca.noxid.factorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;

/**
 * Created by noxid on 08/08/17.
 */
public class CalcApp {

	public static void main(String[] args) {
		HashMap<String, Item> items;
		HashMap<String, Factory> factories;
		try {
			items = CsvLoader.loadItems(new File("data/recipe.csv"));
			factories = CsvLoader.factoryLoader(new File("data/factory.csv"));
		} catch (FileNotFoundException | InputMismatchException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}

		System.out.println(String.format("%d items loaded.\n%d factories loaded.", items.size(), factories.size()));

		BillOfMaterials bom = new BillOfMaterials(items.get("blue_potion"), 1);
		System.out.println(bom.toString());
	}
}
