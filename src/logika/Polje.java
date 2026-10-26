package logika;

/**
 * Predstavlja pojedinačno polje (ćeliju) na ploči igre.
 * <p>
 * Svako polje čuva informacije o tome:
 * <ul>
 * <li>Da li sadrži minu.</li>
 * <li>Koliko ima mina u susjedstvu (broj koji se prikazuje igraču).</li>
 * <li>Trenutno stanje vidljivosti (zatvoreno, otvoreno ili označeno
 * zastavicom).</li>
 * </ul>
 * </p>
 */
public class Polje {
	private boolean mina;
	private int brojMinaOkolo;
	private StanjePolja stanje;

	/**
	 * Kreira novo polje sa početnim vrijednostima.
	 * Polje inicijalno nema minu, broj susjednih mina je 0,
	 * i stanje je postavljeno na {@link StanjePolja#ZATVORENO}.
	 */
	public Polje() {
		mina = false;
		brojMinaOkolo = 0;
		stanje = StanjePolja.ZATVORENO;
	}

	/**
	 * Provjerava da li ovo polje krije minu.
	 * 
	 * @return {@code true} ako je na polju mina, inače {@code false}.
	 */
	public boolean imaMinu() {
		return mina;
	}

	/**
	 * Postavlja ili uklanja minu sa ovog polja.
	 * Koristi se prilikom inicijalizacije ploče.
	 * 
	 * @param v {@code true} za postavljanje mine, {@code false} za uklanjanje.
	 */
	public void postaviMinu(boolean v) {
		mina = v;
	}

	/**
	 * Vraća prethodno izračunati broj mina koje se nalaze u 8 susjednih polja.
	 * Ovo je vrijednost koja se prikazuje igraču kada otvori polje (ako nije mina).
	 * 
	 * @return Broj susjednih mina (od 0 do 8).
	 */
	public int dajBrojMinaOkolo() {
		return brojMinaOkolo;
	}

	/**
	 * Postavlja broj mina u susjedstvu.
	 * Ovu metodu obično poziva klasa {@link Ploca} nakon što rasporedi sve mine.
	 * 
	 * @param v Broj mina u okolini.
	 */
	public void postaviBrojMinaOkolo(int v) {
		brojMinaOkolo = v;
	}

	/**
	 * Vraća trenutno stanje polja.
	 * 
	 * @return Vrijednost iz enumeracije {@link StanjePolja} (npr. ZATVORENO,
	 *         OTVORENO, OZNACENO).
	 */
	public StanjePolja dajStanje() {
		return stanje;
	}

	/**
	 * Mijenja stanje polja.
	 * Ovo se koristi kada igrač otvori polje (lijevi klik) ili postavi zastavicu
	 * (desni klik).
	 * 
	 * @param novo Novo stanje koje treba postaviti.
	 */
	public void postaviStanje(StanjePolja novo) {
		stanje = novo;
	}

	/**
	 * Pomoćna metoda za provjeru da li je polje zatvoreno (skriveno).
	 * 
	 * @return {@code true} ako je stanje {@link StanjePolja#ZATVORENO}.
	 */
	public boolean jeZatvoreno() {
		return stanje == StanjePolja.ZATVORENO;
	}

	/**
	 * Pomoćna metoda za provjeru da li je polje već otvoreno (vidljivo).
	 * 
	 * @return {@code true} ako je stanje {@link StanjePolja#OTVORENO}.
	 */
	public boolean jeOtvoreno() {
		return stanje == StanjePolja.OTVORENO;
	}

	/**
	 * Pomoćna metoda za provjeru da li je polje označeno zastavicom (flagged).
	 * 
	 * @return {@code true} ako je stanje {@link StanjePolja#OZNACENO}.
	 */
	public boolean jeOznaceno() {
		return stanje == StanjePolja.OZNACENO;
	}

}