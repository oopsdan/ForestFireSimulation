import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Trieda MainAplikacia predstavuje aplikáciu pre simuláciu lesného požiaru pomocou JavaFX.
 * 
 * @author: Daniel J.
 * @version 1.0
 */
public class MainAplikacia extends Application {
    // Konštanty pre šírku a výšku okna, veľkosť mriežky a hustotu lesa
    private static final int VELKOST = 11;
    private static final double HUSTOTALESA = 0.6;
    private static final int BUNKAVELKOST = 30;
    private static final int SIRKA = VELKOST * BUNKAVELKOST;
    private static final int VYSKA = VELKOST * BUNKAVELKOST;

    private Canvas canvas;
    private GraphicsContext gc;

    private Slider hustotaSlider;
    private Button nastavitTlacidlo;
    private Button spustitTlacidlo;
    private Label percentoSpaleniaLabel;

    private boolean simulaciaBezi = false;

    private SimulaciaPoziaruGrid simulaciaPoziaruGrid;
    private SimulaciaSireniaPoziaru simulaciaSireniaPoziaru;
    private VykresliGrid vykresliGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inicializuje používateľské rozhranie
        this.inicializujUI(primaryStage);
        this.nastavGrid();
        this.vykresliGrid();
    }

    /**
     * Inicializuje grafické komponenty a nastavenie okna.
     * Vytvára instancie potrebných tried, inicializuje ovládacie prvky a nastavuje akcie na tlačidlá.
     * Inicializuje mriežku pre simuláciu a vykreslí pozadie a počiatočný stav mriežky.
     *
     * @param primaryStage Hlavné okno aplikácie
     */
    private void inicializujUI(Stage primaryStage) {
        this.canvas = new Canvas(SIRKA, VYSKA);
        this.gc = this.canvas.getGraphicsContext2D();

        this.hustotaSlider = new Slider(0, 1, HUSTOTALESA);
        this.nastavitTlacidlo = new Button("Nastaviť");
        this.spustitTlacidlo = new Button("Spustiť");
        this.percentoSpaleniaLabel = new Label("Percent Spáleného: 0%");

        this.nastavitTlacidlo.setOnAction(event -> this.nastavGrid());
        this.spustitTlacidlo.setOnAction(event -> this.spustiSimulaciuOhna());

        BorderPane root = new BorderPane();
        root.setCenter(this.canvas);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(this.hustotaSlider);
        bottomPane.setCenter(this.nastavitTlacidlo);
        bottomPane.setRight(this.spustitTlacidlo);

        root.setBottom(bottomPane);
        root.setTop(this.percentoSpaleniaLabel);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simulácia Lesného Požiaru");
        primaryStage.setScene(scene);
        primaryStage.show();

        this.simulaciaPoziaruGrid = new SimulaciaPoziaruGrid(this.VELKOST);
        this.simulaciaSireniaPoziaru = new SimulaciaSireniaPoziaru(this.simulaciaPoziaruGrid, this);
        this.vykresliGrid = new VykresliGrid(this.gc, this.simulaciaPoziaruGrid);

        this.vykresliPozadie();
        this.nastavGrid();
        this.vykresliGrid();
    }

    /**
     * Vykreslí pozadie mriežky.
     */
    private void vykresliPozadie() {
        this.vykresliGrid.vykresliPozadie();
    }

    /**
     * Vykreslí aktuálny stav mriežky na plátno.
     * Aktualizuje stav mriežky a zobrazuje ho pomocou farieb na plátno.
     */
    public void vykresliGrid() {
        Platform.runLater(() -> {
            this.vykresliPozadie();

            int pocetSpalenia  = 0;

            for (int i = 0; i < this.simulaciaPoziaruGrid.getLes().length; i++) {
                for (int j = 0; j < this.simulaciaPoziaruGrid.getLes()[0].length; j++) {
                    if (this.simulaciaPoziaruGrid.getSpalenaOblast()[i][j].getStav() == '*' && j > 0) {
                        this.gc.setFill(Color.RED);
                        this.gc.fillRect(j * BUNKAVELKOST, i * BUNKAVELKOST, BUNKAVELKOST, BUNKAVELKOST);
                        pocetSpalenia++;
                    } else if (this.simulaciaPoziaruGrid.getLes()[i][j].getStav() == 'T') {
                        this.gc.setFill(Color.GREEN);
                        this.gc.fillRect(j * BUNKAVELKOST, i * BUNKAVELKOST, BUNKAVELKOST, BUNKAVELKOST);
                    }
                }
            }

            this.gc.setFill(Color.RED);
            this.gc.fillRect(0, 0, BUNKAVELKOST, VYSKA);

            double percentoSpalenia  = (double)pocetSpalenia  / (VELKOST * (VELKOST - 1)) * 100;
            this.percentoSpaleniaLabel.setText("Percent Spálenia: " + String.format("%.2f", percentoSpalenia ) + "%");
        });
    }

    /**
     * Zastaví simuláciu, nastaví novú mriežku a aplikuje hustotu lesa.
     */
    private void nastavGrid() {
        this.simulaciaBezi = false;

        this.simulaciaPoziaruGrid = new SimulaciaPoziaruGrid(this.VELKOST);
        this.simulaciaSireniaPoziaru = new SimulaciaSireniaPoziaru(this.simulaciaPoziaruGrid, this);
        this.vykresliGrid = new VykresliGrid(this.gc, this.simulaciaPoziaruGrid);

        this.simulaciaPoziaruGrid.aplikujHustotu(this.hustotaSlider.getValue());

        this.vykresliGrid();
    }

    /**
     * Spustí alebo zastaví simuláciu požiaru.
     */
    private void spustiSimulaciuOhna() {
        if (!this.simulaciaBezi) {
            this.simulaciaBezi = true;

            new Thread(() -> {
                while (this.simulaciaBezi) {
                    this.simulaciaSireniaPoziaru.logikaSimulacieSireniaPoziaru();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            this.simulaciaBezi = false;
        }
    }
}