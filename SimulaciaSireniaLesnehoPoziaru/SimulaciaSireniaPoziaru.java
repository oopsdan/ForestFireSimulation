import javafx.application.Platform;

/**
 * Trieda SimulaciaSireniaPoziaru predstavuje simulátor šírenia požiaru v lese.
 *  
 * @author: Daniel J.
 * @version 1.0
 */
public class SimulaciaSireniaPoziaru {
    private SimulaciaPoziaruGrid simulaciaPoziaruGrid;
    private MainAplikacia mainAplikacia; // Referencia na hlavnú aplikáciu

    /**
     * Konštruktor pre SimulaciaSireniaPoziaru inicializuje simuláciu mriežky požiaru a referenciu na hlavnú aplikáciu.
     * 
     * @param simulaciaPoziaruGrid Mriežka pre simuláciu požiaru
     * @param mainAplikacia Hlavná aplikácia simulácie lesného požiaru
     */
    public SimulaciaSireniaPoziaru(SimulaciaPoziaruGrid simulaciaPoziaruGrid, MainAplikacia mainAplikacia) {
        this.simulaciaPoziaruGrid = simulaciaPoziaruGrid;
        this.mainAplikacia = mainAplikacia;
    }

    /**
     * Metóda simulaciaSireniaPoziaru simuluje jednorazové šírenie požiaru.
     */
    public void simulaciaSireniaPoziaru() {
        this.simulaciaPoziaruGrid.simulaciaSireniaPoziaru();
    }

    /**
     * Metóda LogikaSimulacieSireniaPoziaru obsahuje logiku pre nekonečnú simuláciu šírenia požiaru v smyčke.
     * Aktualizuje mriežku a vykreslí ju v hlavnej aplikácii každú sekundu.
     */
    public void logikaSimulacieSireniaPoziaru() {
        while (true) {
            this.simulaciaPoziaruGrid.simulaciaSireniaPoziaru();
            Platform.runLater(() -> {
                this.simulaciaPoziaruGrid.updateSpalenaOblast();
                this.mainAplikacia.vykresliGrid(); // Zavolá metódu vykresliGrid() z hlavnej aplikácie
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}