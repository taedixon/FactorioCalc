package ca.noxid.factorio;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noxid on 08/08/17.
 */
public class Recipe {

	private final String name;
	public final String mfgType;
	public final float time;
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

	public void getIngredients(Map<Item, Product> products, float amount) {
		for (Item i : ingredients.keySet()) {
			Product curProd = products.get(i);
			if (curProd == null) {
				curProd = new Product(i, 0, false);
				products.put(i, curProd);
			}
			float requiredForRecipe = ingredients.get(i) / nProduct * amount;
//			System.out.println(String.format("I need %.1f %s to make %.1f %s", requiredForRecipe, i.getName(), amount, name));
			curProd.addToProduction(requiredForRecipe);
//			System.out.println(String.format("I now have %.1f %s", components.get(i), i.getName()));
			i.getIngredients(products, requiredForRecipe);
		}
	}
}
