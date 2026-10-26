package logika;

import java.util.Random;

/**
 * Predstavlja ploču igre (Minesweeper) koja sadrži mrežu polja.
 * Ova klasa upravlja inicijalizacijom table, postavljanjem mina i računanjem
 * broja susjednih mina za svako polje.
 * Služi kao centralni model stanja igre.
 */
public class Ploca {

	private int brojRedova;
	private int brojKolona;
	private int brojMina;
	private Polje[][] polja;

	private final Random rand = new Random();

	// Pomaci za 8 susjednih polja (kombinacije za obilazak oko tačke)
	private static final int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
	private static final int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

	/**
	 * Konstruktor koji inicijalizuje ploču sa zadanim dimenzijama i ukupnim brojem
	 * mina.
	 * Kreira matricu objekata tipa {@link Polje} i priprema ploču za igru.
	 * * @param brojRedova Ukupan broj redova na ploči.
	 * 
	 * @param brojKolona Ukupan broj kolona na ploči.
	 * @param brojMina   Ukupan broj mina koje će biti postavljene na ploču.
	 */
	public Ploca(int brojRedova, int brojKolona, int brojMina) {
		this.brojRedova = brojRedova;
		this.brojKolona = brojKolona;
		this.brojMina = brojMina;

		polja = new Polje[brojRedova][brojKolona];
		for (int i = 0; i < brojRedova; i++) {
			for (int j = 0; j < brojKolona; j++) {
				polja[i][j] = new Polje();
			}
		}
	}

	/**
	 * Vraća ukupan broj redova definisanih na ploči.
	 * 
	 * @return Broj redova (visina ploče).
	 */
	public int dajBrojRedova() {
		return brojRedova;
	}

	/**
	 * Vraća ukupan broj kolona definisanih na ploči.
	 * 
	 * @return Broj kolona (širina ploče).
	 */
	public int dajBrojKolona() {
		return brojKolona;
	}

	/**
	 * Vraća ukupan broj mina koji se nalazi na ploči.
	 * 
	 * @return Broj mina.
	 */
	public int dajBrojMina() {
		return brojMina;
	}

	/**
	 * Vraća kopiju niza pomaka po X osi za pronalazak susjednih polja.
	 * Koristi se u kombinaciji sa {@link #dajDY()}.
	 * 
	 * @return Niz cijelih brojeva koji predstavljaju pomake po redovima.
	 */
	public static int[] dajDX() {
		return dx.clone();
	}

	/**
	 * Vraća kopiju niza pomaka po Y osi za pronalazak susjednih polja.
	 * Koristi se u kombinaciji sa {@link #dajDX()}.
	 * 
	 * @return Niz cijelih brojeva koji predstavljaju pomake po kolonama.
	 */
	public static int[] dajDY() {
		return dy.clone();
	}

	/**
	 * Provjerava da li su zadane koordinate unutar granica ploče.
	 * * @param i Indeks reda.
	 * 
	 * @param j Indeks kolone.
	 * @return {@code true} ako su koordinate validne (unutar matrice), inače
	 *         {@code false}.
	 */
	public boolean uGranicama(int i, int j) {
		return i >= 0 && i < brojRedova && j >= 0 && j < brojKolona;
	}

	/**
	 * Vraća objekat Polje na specifičnim koordinatama.
	 * Ova metoda je glavni način pristupa pojedinačnim ćelijama na ploči.
	 * * @param i Indeks reda željenog polja.
	 * 
	 * @param j Indeks kolone željenog polja.
	 * @return Objekat tipa {@link Polje} na poziciji [i][j].
	 * @throws IllegalArgumentException Ako su proslijeđene koordinate izvan granica
	 *                                  ploče.
	 */
	public Polje dajPolje(int i, int j) {
		if (!uGranicama(i, j))
			throw new IllegalArgumentException("Koordinate su van granica: (" + i + ", " + j + ")");
		return polja[i][j];
	}

	/**
	 * Nasumično raspoređuje mine po ploči dok se ne dostigne definisani broj mina.
	 * <p>
	 * Metoda osigurava da prvo polje na koje igrač klikne (definisano sa safeR,
	 * safeK)
	 * nikada ne dobije minu, kako bi se spriječio gubitak igre na prvom potezu.
	 * Također pazi da ne postavi dvije mine na isto mjesto.
	 * </p>
	 * * @param safeR Indeks reda koji mora ostati siguran (bez mine).
	 * 
	 * @param safeK Indeks kolone koji mora ostati siguran (bez mine).
	 */
	public void postaviMine(int safeR, int safeK) {
		int brojPostavljenih = 0;

		while (brojPostavljenih < brojMina) {
			int i = rand.nextInt(brojRedova);
			int j = rand.nextInt(brojKolona);

			if (i == safeR && j == safeK)
				continue;
			if (polja[i][j].imaMinu())
				continue;

			polja[i][j].postaviMinu(true);
			brojPostavljenih++;
		}
	}

	/**
	 * Prolazi kroz cijelu ploču i za svako polje koje nije mina računa
	 * koliko se mina nalazi u njegovih 8 susjednih polja.
	 * <p>
	 * Rezultat se upisuje direktno u svako polje pozivom metode
	 * {@link Polje#postaviBrojMinaOkolo(int)}.
	 * </p>
	 */
	public void prebrojMineOkolo() {

		for (int i = 0; i < brojRedova; i++) {
			for (int j = 0; j < brojKolona; j++) {

				if (polja[i][j].imaMinu()) {
					polja[i][j].postaviBrojMinaOkolo(0);
					continue;
				}

				int brojac = 0;
				for (int k = 0; k < 8; k++) {
					int ni = i + dx[k];
					int nj = j + dy[k];

					if (uGranicama(ni, nj) && polja[ni][nj].imaMinu())
						brojac++;

				}
				polja[i][j].postaviBrojMinaOkolo(brojac);
			}
		}

	}

}