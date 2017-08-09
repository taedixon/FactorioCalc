package ca.noxid.factorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by noxid on 08/08/17.
 */
public class CsvLoader {

	public static HashMap<String, Item> loadItems(File itemFile) throws FileNotFoundException, InputMismatchException {
		HashMap<String, Item> rv = new HashMap<>();

		Scanner sc = new Scanner(itemFile);
		String[] headers = sc.nextLine().split(",");
		HashMap<String, Integer> headerIDs = new HashMap<>();
		for (int i = 0; i < headers.length; i++) {
			headerIDs.put(headers[i], i);
		}

		int idId, idName, idTime, idTier, idGroup, idProds, idIngredients;
		try {
			idId = headerIDs.get("itemid");
			idName = headerIDs.get("recipe_name");
			idTime = headerIDs.get("time_cost");
			idTier = headerIDs.get("tier");
			idGroup = headerIDs.get("mfg_group");
			idProds = headerIDs.get("num_products");
			idIngredients = headerIDs.get("ingredients");
		} catch (NullPointerException npe) {
			// if we are missing any headers we are expecting in this version of the file
			// do not continue to parse
			throw new InputMismatchException(
					String.format("%s does not match expected format", itemFile.getName()));
		}

		while (sc.hasNextLine()) {
			String[] line = sc.nextLine().split(",", -1);
			String id = line[idId];
			if (id.length() <= 0) {
				//skip empty lines
				continue;
			}
			System.out.println(String.format("Loaded item: %s", id));
			Item curItem = rv.get(id);
			if (curItem == null) {
				// currently the items will simply have the name of the first recipe
				// that produces them... later it would be smarter to separate Items
				// and Recipes more cleanly
				curItem = new Item(id, line[idName], Integer.parseInt(line[idTier]));
				rv.put(id, curItem);
			}
			//establish the list of ingredients for this recipe
			HashMap<String, Float> ingredients = new HashMap<>();
			for (int i = idIngredients; i+1 < line.length; i += 2) {
				String ingredient = line[i];
				if (ingredient.length() > 0) {
					float amt = Float.parseFloat(line[i+1]);
					ingredients.put(ingredient, amt);
				}
			}
			Recipe curRecipe = new Recipe(
					line[idName],
					line[idGroup],
					Float.parseFloat(line[idTime]),
					Float.parseFloat(line[idProds]),
					ingredients);
			curItem.addRecipe(curRecipe);

		}
		for (Item i : rv.values()) {
			i.finalizeRecipes(rv);
		}
		return rv;
	}

	public static HashMap<String, Factory> factoryLoader(File factoryFile) throws FileNotFoundException, InputMismatchException {
		HashMap<String, Factory> rv = new HashMap<>();

		Scanner sc = new Scanner(factoryFile);
		String[] headers = sc.nextLine().split(",");
		HashMap<String, Integer> headerIDs = new HashMap<>();
		for (int i = 0; i < headers.length; i++) {
			headerIDs.put(headers[i], i);
		}

		int idId, idName, idSpeed, idSlots, idGroup, idPower, idDrain, idPollution;
		try {
			idId = headerIDs.get("factoryid");
			idName = headerIDs.get("nicename");
			idSpeed = headerIDs.get("speed");
			idSlots = headerIDs.get("modslots");
			idGroup = headerIDs.get("mfg_group");
			idPower = headerIDs.get("consumption");
			idDrain = headerIDs.get("drain");
			idPollution = headerIDs.get("pollution");
		} catch (NullPointerException npe) {
			// if we are missing any headers we are expecting in this version of the file
			// do not continue to parse
			throw new InputMismatchException(
					String.format("%s does not match expected format", factoryFile.getName()));
		}

		while (sc.hasNextLine()) {
			String[] line = sc.nextLine().split(",", -1);
			String id = line[idId];
			if (id.length() <= 0) {
				continue;
			}
			Factory curFactory = new Factory(
					id,
					line[idName],
					line[idGroup],
					Float.parseFloat(line[idSpeed]),
					Integer.parseInt(line[idSlots]),
					Float.parseFloat(line[idPower]) + Float.parseFloat(line[idDrain]),
					Float.parseFloat(line[idPollution])
			);
			rv.put(id, curFactory);
		}

		return rv;
	}
}
