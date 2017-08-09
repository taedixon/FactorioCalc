package ca.noxid.factorio;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by noxid on 09/08/17.
 */
public class BillOfMaterials {

	Map<Item, Product> products;

	BillOfMaterials() {
		products = new HashMap<>();
	}

	public void addProduct(Item item, float amount) {
		Product prod;
		if (products.containsKey(item)) {
			prod = products.get(item);
		} else {
			prod = new Product(item, amount, true);
		}
		products.put(item, prod);
		item.getIngredients(products, amount);
	}

	public void setFactories(Collection<Factory> factories) {
		products.values().forEach(p -> p.setFactories(factories));
	}

	public String computeResults(float time) {
		StringBuilder sb = new StringBuilder();
		Map<Integer, List<Product>> tiers = products.values().stream()
				.filter(p -> !p.primaryProduct)
				.collect(Collectors.groupingBy(prod -> prod.getItem().tier));
		List<Integer> sortedTiers = new ArrayList<>(tiers.keySet());
		Collections.sort(sortedTiers);
		for (Integer tier : sortedTiers) {
			sb.append(String.format("\n===TIER %d===\n", tier));
			for (Product p : tiers.get(tier)) {
				sb.append(p.getResults(time));
			}
		}
		sb.append("\n===FINAL PRODUCTS===\n");
		products.values().stream().filter(p -> p.primaryProduct)
				.forEach(p -> sb.append(p.getResults(time)));
		return sb.toString();
	}
}
