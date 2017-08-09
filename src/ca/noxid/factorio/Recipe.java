package ca.noxid.factorio;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noxid on 08/08/17.
 */
public class Recipe {

	private final String name;
	private final String mfgType;
	private final float time;
	private final float nProduct;
	private final Map<String, Float> ingredientIDs;
	private Map<Item, Float> ingredients;

	public Recipe(String name, String mfgType, float time, float nProduct, HashMap<String, Float> ingredients) {
		this.name = name;
		this.mfgType = mfgType;
		this.time = time;
		this.nProduct = nProduct;

		this.ingredientIDs = ingredients;

		this.ingredients = new HashMap<>();
	}

	public void populateIngredients(Map<String, Item> itemMap) {
		for (String s : ingredientIDs.keySet()) {
			Item i = itemMap.get(s);
			if (i == null) {
				System.err.println(String.format("Warning! Missing item %s for recipe %s", s, name));
			} else {
				ingredients.put(i, ingredientIDs.get(s));
			}
		}
	}

	public void getIngredients(HashMap<Item, Float> components, float amount) {
		for (Item i : ingredients.keySet()) {
			if (components.get(i) == null) {
				components.put(i, 0f);
			}
			float curAmt = components.get(i);
			float requiredForRecipe = ingredients.get(i) / nProduct * amount;
//			System.out.println(String.format("I need %.1f %s to make %.1f %s", requiredForRecipe, i.getName(), amount, name));
			components.put(i, curAmt + requiredForRecipe);
//			System.out.println(String.format("I now have %.1f %s", components.get(i), i.getName()));
			i.getIngredients(components, requiredForRecipe);
		}
	}
}
