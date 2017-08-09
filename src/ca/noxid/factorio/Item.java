package ca.noxid.factorio;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by noxid on 08/08/17.
 */
public class Item {

	public final String id;
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

	public void getIngredients(Map<Item, Product> products, float amount) {
		// figure out trees n shit later. for now lets just assume the first
		// recipe is the one we want.
		if (recipes.size() >= 1) {
			Recipe r = recipes.get(0);
			r.getIngredients(products, amount);
		}
	}

	public void finalizeRecipes(HashMap<String, Item> itemMap) {
		recipes.forEach(r -> r.populateIngredients(itemMap));
	}

	public String getName() {
		return name;
	}

	public Set<String> getFactoryGroups() {
		return recipes.stream().map(r -> r.mfgType).collect(Collectors.toSet());
	}

	public float getTimeToMake() {
		if (recipes.size() > 0) {
			return recipes.get(0).time;
		}
		return 0;
	}
}
