package konzola;

import java.util.Scanner;
import logika.*;

public class KonzolnaIgra {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Igra igra = new Igra();

        System.out.println("IGRAJ MINESWEEPER");
        System.out.println("Komande:");
        System.out.println(" n R C M  -> nova igra (redovi, kolone, mine)");
        System.out.println(" o r c -> otvori polje");
        System.out.println(" f r c -> oznaci/ukloni zastavicu");
        System.out.println(" c r c -> chord (otvori oko)");
        System.out.println(" q     -> izlaz");
        
        
        while (true) {
            System.out.print("\n> ");
            String linija = sc.nextLine().trim();
            if (linija.isEmpty()) continue;

            String[] parametri= linija.split("\\s+");
            String komanda = parametri[0].toLowerCase();

            try {
                if (komanda.equals("q")) {
                    System.out.println("Kraj igre.");
                    break;
                }

                if (komanda.equals("n")) {
                    if (parametri.length != 4) {
                        System.out.println("Greška! Format: n Redovi Kolone Mine");
                        continue;
                    }
                    igra.resetuj(Integer.parseInt(parametri[1]), Integer.parseInt(parametri[2]),
                    		Integer.parseInt(parametri[3]));
                    ispisi(igra, false);
                    continue;
                }

                if (!provjeriIgru(igra)) continue;

                int r = (parametri.length > 1) ? Integer.parseInt(parametri[1]) : -1;
                int c = (parametri.length > 2) ? Integer.parseInt(parametri[2]) : -1;

                switch (komanda) {
                    case "o":
                        igra.otvori(r, c);
                        break;
                    case "f":
                        igra.oznaci(r, c);
                        break;
                    case "c":
                        igra.chordKlik(r, c);
                        break;
                    default:
                        System.out.println("Nepoznata komanda.");
                        continue;
                }

                ispisi(igra, false);
                ispisiStatus(igra);

            } catch (NumberFormatException ex) {
                System.out.println("Greška: Unesite ispravne brojeve.");
            } catch (IllegalArgumentException ex) {
                System.out.println("Greška: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Neočekivana greška.");
            }
        }
        sc.close();
    }

    private static boolean provjeriIgru(Igra igra) {
        if (igra.dajPlocu() == null) {
            System.out.println("Prvo pokrenite igru komandom 'n'.");
            return false;
        }
        return true;
    }

    private static void ispisi(Igra igra, boolean krajIgre) {
        Ploca p = igra.dajPlocu();
        if (p == null) return;

        System.out.println("\nStatus: " + igra.dajStatus() + " | Mine: " + igra.preostaleMine());
        
        System.out.print("    ");
        for (int c = 0; c < p.dajBrojKolona(); c++) System.out.printf("%2d ", c);
        System.out.println("\n    " + "---".repeat(p.dajBrojKolona()));

        for (int r = 0; r < p.dajBrojRedova(); r++) {
            System.out.printf("%2d |", r); 
            for (int c = 0; c < p.dajBrojKolona(); c++) {
                Polje polje = p.dajPolje(r, c);
                char ch = prikaz(polje, krajIgre || igra.dajStatus() == StatusIgre.PORAZ);
                System.out.printf(" %c ", ch);
            }
            System.out.println();
        }
    }

    private static char prikaz(Polje polje, boolean otkrijSve) {
        StanjePolja stanje = polje.dajStanje();

        if (stanje == StanjePolja.OZNACENO) return 'F';
        
        if (stanje == StanjePolja.ZATVORENO) {
            if (otkrijSve && polje.imaMinu()) return '*';
            return '#';
        }

        if (polje.imaMinu()) return '*';
        
        int n = polje.dajBrojMinaOkolo();
        return (n == 0) ? '.' : (char) ('0' + n);
    }

    private static void ispisiStatus(Igra igra) {
        if (igra.dajStatus() == StatusIgre.POBJEDA) {
            System.out.println("POBJEDA! Otvorena su sva sigurna polja.");
        } else if (igra.dajStatus() == StatusIgre.PORAZ) {
            System.out.println("PORAZ! Otvorili ste minu.");
            ispisi(igra, true); 
        }
    }
}