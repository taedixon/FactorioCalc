package ca.noxid.factorio;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by noxid on 09/08/17.
 */
public class BillOfMaterials {

	Item product;
	float productCount;
	HashMap<Item, Float> components;

	BillOfMaterials(Item targetItem, float amount) {
		components = new HashMap<>();
		product = targetItem;
		productCount = amount;
		targetItem.getIngredients(components, amount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Map<Integer, List<Item>> tiers = components.keySet().stream()
				.collect(Collectors.groupingBy(item -> item.tier));
		List<Integer> sortedTiers = new ArrayList<>(tiers.keySet());
		Collections.sort(sortedTiers);
		for (Integer tier : sortedTiers) {
			sb.append(String.format("\n===TIER %d===\n", tier));
			for (Item i : tiers.get(tier)) {
				sb.append(String.format("%s: %.2f\n", i.getName(), components.get(i)));
			}
		}
		sb.append(String.format("\nResult: %.1f %s", productCount, product.getName()));
		return sb.toString();
	}
}
