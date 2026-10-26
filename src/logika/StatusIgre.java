package logika;

/**
 * Enumeracija koja definiše trenutno stanje (fazu) u kojoj se igra nalazi.
 * <p>
 * Ovi statusi služe kontrolerima (Konzola ili GUI) da znaju:
 * <ul>
 * <li>Kada pokrenuti tajmer (prelazak iz SPREMNA u U_TOKU).</li>
 * <li>Kada onemogućiti dalje poteze (stanje POBJEDA ili PORAZ).</li>
 * <li>Koju poruku prikazati igraču na kraju.</li>
 * </ul>
 * </p>
 */
public enum StatusIgre {

	/**
	 * Igra je inicijalizovana, ploča je kreirana, ali igrač još nije napravio prvi
	 * potez.
	 * U ovom stanju tajmer obično još ne teče.
	 */
	SPREMNA,

	/**
	 * Igra je aktivna. Prvi potez je napravljen, mine su raspoređene i igrač
	 * aktivno otvara polja. U ovom stanju se prihvataju inputi korisnika.
	 */
	U_TOKU,

	/**
	 * Igra je uspješno završena. Igrač je otvorio sva polja koja nisu mine.
	 * Ovo je završno stanje (Game Over - Win).
	 */
	POBJEDA,

	/**
	 * Igra je završena neuspjehom. Igrač je otvorio polje koje sadrži minu.
	 * Ovo je završno stanje (Game Over - Lose).
	 */
	PORAZ
}