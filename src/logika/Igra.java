package logika;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @brief Glavna logika igre Minesweeper (Minolovac).
 *
 * Klasa upravlja stanjem igre, pločom, postavljanjem mina (first-click safe),
 * otvaranjem polja, označavanjem zastavicom, flood fill otvaranjem (BFS) i chord klikom.
 * Ne zavisi od UI sloja (GUI/konzola).
 */
public class Igra {
	private Ploca ploca;
	private StatusIgre status;
	private boolean minePostavljene;
	private int brojZastavica;
	
	private void inicijalizujStanje() {
		status = StatusIgre.SPREMNA;
		minePostavljene = false;
		brojZastavica = 0;
	}
	
	/**
	 * @brief Kreira novu igru u početnom stanju (bez ploče dok se ne pozove resetuj).
	 */
	public Igra() {
		inicijalizujStanje();
	}
	
	/**
	 * @brief Resetuje igru i kreira novu ploču dimenzija redovi×kolone sa zadatim brojem mina.
	 *
	 * Metoda validira parametre, kreira novu ploču i vraća igru u početno stanje.
	 *
	 * @param redovi Broj redova ploče (R).
	 * @param kolone Broj kolona ploče (C).
	 * @param mine Broj mina na ploči (B/M).
	 *
	 * @throws IllegalArgumentException Ako su parametri izvan dozvoljenih granica.
	 */
	public void resetuj(int redovi, int kolone, int mine) {
		validiraj(redovi, kolone, mine);
		ploca = new Ploca(redovi, kolone, mine);
		inicijalizujStanje();
	}

	private void validiraj(int R, int C, int B) {
		if (R < 5 || R > 50)
			throw new IllegalArgumentException("Broj redova mora biti u rasponu [5, 50]. Uneseno: " + R);
		if (C < 5 || C > 50)
			throw new IllegalArgumentException("Broj kolona mora biti u opsegu [5, 50]. Uneseno: " + C);
		int minMine = Math.min(R, C);
		int maxMine = (int) Math.floor(R*C*0.8);
		if(B < minMine || B > maxMine)
			throw new IllegalArgumentException("Broj mina mora biti u opsegu [" +
			minMine + ", " + maxMine + "].");
	}

	/**
	 * @brief Vraća trenutnu ploču igre.
	 * @return Referenca na ploču ili null ako igra nije resetovana.
	 */
	public Ploca dajPlocu() { return ploca; }

	/**
	 * @brief Vraća trenutni status igre.
	 * @return Status igre (SPREMNA, U_TOKU, POBJEDA ili PORAZ).
	 */
	public StatusIgre dajStatus() { return status; }
	
	/**
	 * @brief Vraća broj preostalih mina prema broju postavljenih zastavica.
	 *
	 * Preostale mine = ukupno mina na ploči - broj zastavica.
	 *
	 * @return Procjena preostalih mina; 0 ako ploča nije inicijalizovana.
	 */
	public int preostaleMine() {
		if (ploca == null) return 0;
		return ploca.dajBrojMina() - brojZastavica;
	}

	/**
	 * @brief Postavlja ili uklanja zastavicu na polju (toggle).
	 *
	 * Zastavica se može postaviti samo na neotvoreno polje. Ova akcija ne utiče direktno na pobjedu,
	 * jer se pobjeda provjerava otvaranjem svih sigurnih polja.
	 *
	 * @param i Indeks reda.
	 * @param j Indeks kolone.
	 */
	public void oznaci(int i, int j) {
		if(!mozeAkcija(i, j)) return;
		Polje p = ploca.dajPolje(i, j);
		
		if(p.jeOtvoreno()) return;
		
		if(p.jeOznaceno()) {
			p.postaviStanje(StanjePolja.ZATVORENO);
			brojZastavica--;
		} else{
			p.postaviStanje(StanjePolja.OZNACENO);
			brojZastavica++;
		}
	}

	/**
	 * @brief Otvara polje na zadatim koordinatama.
	 *
	 * - Prvi klik je siguran: mine se postavljaju tek na prvom otvaranju i izbjegava se početno polje.
	 * - Ako je otvorena mina, igra prelazi u stanje PORAZ.
	 * - Ako je broj susjednih mina 0, izvršava se flood fill (BFS).
	 * - Nakon otvaranja provjerava se pobjeda.
	 *
	 * @param i Indeks reda.
	 * @param j Indeks kolone.
	 */
	public void otvori(int i, int j) {
		if(!mozeAkcija(i, j)) return;
		
		Polje p = ploca.dajPolje(i, j);
		if(!p.jeZatvoreno()) return;
		if(!minePostavljene) {
			ploca.postaviMine(i, j);
			ploca.prebrojMineOkolo();
			minePostavljene = true;
			status = StatusIgre.U_TOKU;
		}
		if(p.imaMinu()) {
			p.postaviStanje(StanjePolja.OTVORENO);
			status = StatusIgre.PORAZ;
			return;
		}
		p.postaviStanje(StanjePolja.OTVORENO);
		if(p.dajBrojMinaOkolo() == 0) {
			floodFillBFS(i, j);
		}
		if(provjeriPobjedu()) {
			status = StatusIgre.POBJEDA;
		}
		
	}

	private boolean provjeriPobjedu() {
		int redovi = ploca.dajBrojRedova();
		int kolone = ploca.dajBrojKolona();
		
		int sigurnaPolja = redovi * kolone - ploca.dajBrojMina();
		int otvorenaSigurna = 0;
		for(int i = 0; i < redovi; i++) {
			for(int j = 0; j < kolone; j++) {
				Polje p = ploca.dajPolje(i, j);
				if(!p.imaMinu() && p.jeOtvoreno())
					otvorenaSigurna++;
			}
		}
		return otvorenaSigurna == sigurnaPolja;
	}

	private void floodFillBFS(int pocetakR, int pocetakC) {
		Queue<int[]> q = new ArrayDeque<>();
		q.add(new int[] {pocetakR, pocetakC});
		
		int[] dx = Ploca.dajDX();
		int[] dy = Ploca.dajDY();
		
		while(!q.isEmpty()) {
			int[] curr = q.poll();
			int i = curr[0];
			int j = curr[1];
			
			for(int k = 0; k < 8; k++) {
				int ni = i + dx[k];
				int nj = j + dy[k];
				
				if(!ploca.uGranicama(ni, nj)) continue;
				
				Polje susjed = ploca.dajPolje(ni, nj);
				if(!susjed.jeZatvoreno()) continue;
				if(susjed.imaMinu()) continue;
				susjed.postaviStanje(StanjePolja.OTVORENO);
				if(susjed.dajBrojMinaOkolo() == 0) 
					q.add(new int[]{ni, nj});	
			}
		}
	}

	private boolean mozeAkcija(int i, int j) {
		if(ploca == null) return false;
		if(status == StatusIgre.POBJEDA || status == StatusIgre.PORAZ) return false;
		if(!ploca.uGranicama(i, j)) return false;
		return true;
	}
	
	/**
	 * @brief Izvršava chord klik na otvorenom polju sa brojem > 0.
	 *
	 * Ako je oko centralnog polja postavljen isti broj zastavica kao broj na polju,
	 * otvaraju se sva susjedna polja koja nisu otvorena i nisu označena.
	 *
	 * @param i Indeks reda centralnog polja.
	 * @param j Indeks kolone centralnog polja.
	 */
	public void chordKlik(int i, int j) {
		if(!mozeAkcija(i, j)) return;
		
		Polje centralnoPolje = ploca.dajPolje(i, j);
		if(!centralnoPolje.jeOtvoreno()) return;
		
		int brojMina = centralnoPolje.dajBrojMinaOkolo();
		if(brojMina == 0) return; 
		
		int[] dx = Ploca.dajDX();
		int[] dy = Ploca.dajDY();
		
		int zastavice = 0;
		for(int k = 0; k < 8; k++) {
			int ni = i + dx[k];
			int nj = j + dy[k];
			
			if(!ploca.uGranicama(ni, nj)) continue;
			
			Polje susjed = ploca.dajPolje(ni, nj);
			if(susjed.jeOznaceno()) {
				zastavice++;
			}
		}
		if(zastavice != brojMina) return;
		
		for(int k = 0; k < 8; k++) {
			int ni = i + dx[k];
			int nj = j + dy[k];
			
			if(!ploca.uGranicama(ni, nj)) continue;
			
			Polje susjed = ploca.dajPolje(ni, nj);
			if(susjed.jeZatvoreno()) {
				otvori(ni, nj);
				if(status == StatusIgre.PORAZ) return;
			}
		}
	}
	
}
