package ca.noxid.factorio;

import javax.swing.*;

/**
 * Created by noxid on 09/08/17.
 */
public class Util {
	private Util() {}
	/**
	 * Shows a string message in a message box dialog.
	 * @param message
	 */
	public static void msgBox(String message)
	{
		System.out.println(message);
		JOptionPane.showMessageDialog(null,message,"Info",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
