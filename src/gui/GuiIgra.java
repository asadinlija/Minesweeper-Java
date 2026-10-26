package gui;

public class GuiIgra {

	private ProzorIgre prozor;
	
	public GuiIgra() {
		prozor = new ProzorIgre();
	}
	public void prikaziProzor() {
		prozor.prikaziProzor();
	}
	public static void main(String[] args) {
		GuiIgra gui = new GuiIgra();
		gui.prikaziProzor();
	}

}
