package ca.noxid.factorio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/**
 * Created by noxid on 08/08/17.
 */

public class CalcApp extends JFrame {
	private HashMap<String, Item> items;
	private HashMap<String, Factory> factories;
	private HashMap<Item, Float> desiredQuantities;
	private List<JTextField> qtyInputs;
	private JTextField timeField;
	private JTextArea outputArea;

	public CalcApp() {
		super();
		//init class vars
		loadConfigs();
		desiredQuantities = new HashMap<>();
		qtyInputs = new LinkedList<>();

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
			e.printStackTrace();
		}

		setContentPane(createContentPane());
		setTitle("Factorio Calculator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private Container createContentPane() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//create "bill of materials" view
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		basePanel.add(createBomPane(), gbc);

		//create "modules" view
		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridheight = 1;
		basePanel.add(createModulesPane(), gbc);


		//create "controls" view
		gbc.gridx = 2;
		gbc.weightx = 1;
		basePanel.add(createControlsPane(), gbc);

		//create "output" view
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		basePanel.add(createOutputPane(), gbc);

		return basePanel;
	}

	private Container createBomPane() {

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());

		JScrollPane jsp = new JScrollPane(listPanel);
		jsp.getVerticalScrollBar().setUnitIncrement(10);

		jsp.setBorder(BorderFactory.createTitledBorder("Bill of Materials"));

		ArrayList<Item> itemCopy = new ArrayList<>(items.values());
		itemCopy.sort((a, b) -> a.getName().compareTo(b.getName()));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		for (Item i : itemCopy) {
			gbc.gridx = 0;
			gbc.weightx = 0;
			JLabel desc = new JLabel(i.getName());
			listPanel.add(desc, gbc);
			gbc.gridx++;
			gbc.weightx = 1;
			JTextField countField = new JTextField(8);
			countField.addKeyListener(new BomKeyAdapter(i, countField));
			listPanel.add(countField, gbc);
			qtyInputs.add(countField);
			gbc.gridy++;
		}
		jsp.setPreferredSize(new Dimension(280, 600));
		jsp.setMinimumSize(new Dimension(280, 100));

		return jsp;
	}

	private Container createModulesPane() {
		//currently does nothing
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new GridLayout(0, 2));
		basePanel.setBorder(BorderFactory.createTitledBorder("Modules"));

		String[] modules = {
				"prod3", "speed3", "efficient3",
				"prod2", "speed2", "efficient2",
				"prod1", "speed1", "efficient1",
		};

		for (int i = 0; i < 4; i++) {
			JLabel label = new JLabel(String.format("Module %d", i+1));
			JComboBox<String> cbox = new JComboBox<>(modules);
			cbox.setEnabled(false);
			basePanel.add(label);
			basePanel.add(cbox);
		}

		basePanel.setEnabled(false);
		return basePanel;
	}

	private Container createControlsPane() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		basePanel.setBorder(BorderFactory.createTitledBorder("Production Settings"));

		//add time factor picker
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		JLabel timeDesc = new JLabel("Seconds to manufacture all goods");
		timeField = new JTextField(8);
		timeField.setText("1");
		timePanel.add(timeDesc);
		timePanel.add(timeField);
		basePanel.add(timePanel, BorderLayout.NORTH);


		//add buttons
		JPanel buttonsPanel = new JPanel();
		JButton clearButton = new JButton();
		clearButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onClearResults();
			}
		});
		clearButton.setText("Reset");
		buttonsPanel.add(clearButton);

		JButton submitButton = new JButton();
		submitButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCalculateResults();
			}
		});
		submitButton.setText("Calculate");
		buttonsPanel.add(submitButton);

		basePanel.add(buttonsPanel, BorderLayout.SOUTH);

		//finalize
		return basePanel;
	}

	private Container createOutputPane() {
		outputArea = new JTextArea();
		JScrollPane jsp = new JScrollPane(outputArea);

		return jsp;
	}

	private void onCalculateResults() {
		BillOfMaterials bom = new BillOfMaterials();
		desiredQuantities.forEach((item, amt) -> {
			if (amt > 0) bom.addProduct(item, amt);
		});
		bom.setFactories(factories.values());
		float time = 1;
		try {
			time = Float.parseFloat(timeField.getText());
		} catch (NumberFormatException ignored) {}
		if (time < 0) time = 1;
		outputArea.setText(bom.computeResults(time));
	}

	private void onClearResults() {
		desiredQuantities.clear();
		qtyInputs.forEach(x -> x.setText(""));
		timeField.setText("");
		outputArea.setText("");
	}


	private class BomKeyAdapter extends KeyAdapter {
		private final Item item;
		private final JTextField field;

		BomKeyAdapter(Item item, JTextField field){
			this.item = item;
			this.field = field;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			String content = field.getText() + e.getKeyChar();
			float fval = 0f;
			try {
				fval = Float.parseFloat(content);
			} catch (NumberFormatException ignored) {}

			if (fval > 0) {
				desiredQuantities.put(item, fval);
			}
		}
	}

	private void loadConfigs() {
		try {
			items = CsvLoader.loadItems(new File("data/recipe.csv"));
			factories = CsvLoader.factoryLoader(new File("data/factory.csv"));
		} catch (FileNotFoundException | InputMismatchException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(String.format("%d items loaded.\n%d factories loaded.", items.size(), factories.size()));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(CalcApp::new);
	}
}
