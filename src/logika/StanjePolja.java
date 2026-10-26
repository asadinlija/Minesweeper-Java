package logika;

/**
 * Enumeracija koja definiše moguća vidljiva stanja jednog polja na ploči.
 * <p>
 * Ova stanja određuju kako se polje prikazuje korisniku (GUI ili konzola) i
 * koje su akcije dozvoljene nad njim (npr. ne može se otvoriti polje koje je
 * već otvoreno).
 * </p>
 */
public enum StanjePolja {

	/**
	 * Polje je skriveno. Ovo je početno stanje svakog polja.
	 * Sadržaj polja (broj ili mina) nije vidljiv igraču.
	 */
	ZATVORENO,

	/**
	 * Polje je otkriveno (igrač je kliknuo na njega).
	 * U ovom stanju sadržaj polja je vidljiv.
	 */
	OTVORENO,

	/**
	 * Polje je označeno zastavicom (flagged).
	 * Igrač postavlja ovo stanje kada sumnja da se na tom mjestu nalazi mina.
	 * Polje u ovom stanju se ne može otvoriti lijevim klikom dok se oznaka ne
	 * skine.
	 */
	OZNACENO
}