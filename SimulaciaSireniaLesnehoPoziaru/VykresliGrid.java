import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Trieda VykresliGrid slúži na vykresľovanie mriežky simulácie požiaru na plátno pomocou JavaFX.
 *
 * @author: Daniel J.
 * @version 1.0
 */
public class VykresliGrid {
    private GraphicsContext gc;
    private SimulaciaPoziaruGrid simulaciaPoziaruGrid;
    private static final int BUNKA_VELKOST = 30;

    /**
     * Konštruktor triedy VykresliGrid inicializuje grafický kontext a mriežku simulácie požiaru.
     * 
     * @param gc = Grafický kontext pre vykresľovanie
     * @param simulaciaPoziaruGrid = Mriežka pre simuláciu požiaru
     */
    public VykresliGrid(GraphicsContext gc, SimulaciaPoziaruGrid simulaciaPoziaruGrid) {
        this.gc = gc;
        this.simulaciaPoziaruGrid = simulaciaPoziaruGrid;
    }

    /**
     * Metóda vykresliPozadie vykreslí pozadie mriežky.
     */
    public void vykresliPozadie() {
        this.gc.setFill(Color.BLACK);
        this.gc.fillRect(0, 0, this.simulaciaPoziaruGrid.getLes().length * BUNKA_VELKOST, this.simulaciaPoziaruGrid.getLes()[0].length * BUNKA_VELKOST);
    }

    /**
     * Metóda vykresliGrid vykreslí aktuálny stav mriežky simulácie požiaru.
     */
    public void vykresliGrid() {
        this.vykresliPozadie();

        int pocetSpalenia = 0;

        for (int i = 0; i < this.simulaciaPoziaruGrid.getLes().length; i++) {
            for (int j = 0; j < this.simulaciaPoziaruGrid.getLes()[0].length; j++) {
                if (this.simulaciaPoziaruGrid.getSpalenaOblast()[i][j].getStav() == '*' && j > 0) {
                    this.gc.setFill(Color.RED);
                    this.gc.fillRect(j * BUNKA_VELKOST, i * BUNKA_VELKOST, BUNKA_VELKOST, BUNKA_VELKOST);
                    pocetSpalenia++;
                } else if (this.simulaciaPoziaruGrid.getLes()[i][j].getStav() == 'T') {
                    this.gc.setFill(Color.GREEN);
                    this.gc.fillRect(j * BUNKA_VELKOST, i * BUNKA_VELKOST, BUNKA_VELKOST, BUNKA_VELKOST);
                }
            }
        }

        this.gc.setFill(Color.RED);
        this.gc.fillRect(0, 0, BUNKA_VELKOST, this.simulaciaPoziaruGrid.getLes().length * BUNKA_VELKOST);

        double percentoSpalenia = (double)pocetSpalenia / (this.simulaciaPoziaruGrid.getLes().length * (this.simulaciaPoziaruGrid.getLes()[0].length - 1)) * 100;
    }
}