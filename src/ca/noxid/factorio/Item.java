package ca.noxid.factorio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by noxid on 08/08/17.
 */
public class Item {

	private final String id;
	private final String name;
	public final int tier;
	private List<Recipe> recipes;

	public Item(String id, String name, int tier) {

		this.id = id;
		this.name = name;
		this.tier = tier;

		recipes = new LinkedList<>();
	}

	public void addRecipe(Recipe r) {
		recipes.add(r);
	}

	public void getIngredients(HashMap<Item, Float> components, float amount) {
		// figure out trees n shit later. for now lets just assume the first
		// recipe is the one we want.
		if (recipes.size() >= 1) {
			Recipe r = recipes.get(0);
			r.getIngredients(components, amount);
		}
	}

	public void finalizeRecipes(HashMap<String, Item> itemMap) {
		for (Recipe r : recipes) {
			r.populateIngredients(itemMap);
		}
	}

	public String getName() {
		return name;
	}
}
