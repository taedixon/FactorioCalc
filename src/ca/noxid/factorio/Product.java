package ca.noxid.factorio;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by noxid on 09/08/17.
 */
public class Product {
	private Item producedItem;
	private float producedAmount;
	private Set<Factory> factoryOptions;
	public final boolean primaryProduct;

	public Product(Item itemProduced, float amount, boolean primary) {
		producedItem = itemProduced;
		producedAmount = amount;
		primaryProduct = primary;
		factoryOptions = new HashSet<>();
	}

	public void setFactories(Collection<Factory> factories) {
		factoryOptions = factories.stream().filter(
				f -> producedItem.getFactoryGroups()
						.contains(f.group)).collect(Collectors.toSet());
	}

	public String getResults(float timeScale) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s :%.2f\n", producedItem.getName(), producedAmount));
		for (Factory f : factoryOptions) {
			float factoryRequired = producedAmount * producedItem.getTimeToMake() / f.speed / timeScale;
			sb.append(String.format("\tRequires %.2f %s\n", factoryRequired, f.name));
		}
		return sb.toString();
	}

	public Item getItem() {
		return producedItem;
	}

	public float getAmount() {
		return producedAmount;
	}

	public void addToProduction(float newProducts) {
		if (newProducts > 0) {
			producedAmount += newProducts;
		}
	}
}
