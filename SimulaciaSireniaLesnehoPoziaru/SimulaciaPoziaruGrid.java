import java.util.Random;

/**
 * Trieda SimulaciaPoziaruGrid predstavuje mriežku simulácie lesného požiaru.
 * 
 * Mriežka obsahuje dve časti - les a spálené územie. Veľkosť mriežky je špecifikovaná pri vytváraní.
 * 
 * @author: Daniel J.
 * @version 1.0
 */
public class SimulaciaPoziaruGrid {
    private BunkaPoziar[][] les;
    private BunkaPoziar[][] spalenaOblast;
    private int velkost;

    /**
     * Konštruktor pre vytvorenie mriežky s danou veľkosťou.
     * 
     * @param velkost Veľkosť mriežky (počet riadkov a stĺpcov).
     */
    public SimulaciaPoziaruGrid(int velkost) {
        this.velkost = velkost;
        this.les = new BunkaPoziar[velkost][velkost];
        this.spalenaOblast = new BunkaPoziar[velkost][velkost];
        this.inicializujGrid();
    }
    
    /**
     * Metóda vráti mriežku lesa.
     * 
     * @return Mriežka lesa.
     */
    public BunkaPoziar[][] getLes() {
        return this.les;
    }

    /**
     * Metóda vráti mriežku spáleného územia.
     * 
     * @return Mriežka spáleného územia.
     */
    public BunkaPoziar[][] getSpalenaOblast() {
        return this.spalenaOblast;
    }

    /**
     * Inicializuje mriežku lesa a spáleného územia výchozím stavom.
     */
    private void inicializujGrid() {
        for (int i = 0; i < velkost; i++) {
            for (int j = 0; j < velkost; j++) {
                this.les[i][j] = new BunkaPoziar();
                this.spalenaOblast[i][j] = new BunkaPoziar();
            }
        }
    }

    /**
     * Aplikuje danú hustotu stromov na mriežku.
     * 
     * @param novaHustota Hustota stromov v rozsahu od 0 do 1.
     */
    public void aplikujHustotu(double novaHustota) {
        Random random = new Random();

        this.inicializujGrid();

        for (int i = 0; i < velkost; i++) {
            for (int j = 0; j < velkost; j++) {
                if (random.nextDouble() < novaHustota) {
                    this.les[i][j].setStav('T');
                }
            }
        }

        int poziarStlpec = random.nextInt(this.velkost);
        for (int i = 0; i < velkost; i++) {
            this.les[(poziarStlpec + i) % this.velkost][0].setStav('*');
            this.spalenaOblast[(poziarStlpec + i) % this.velkost][0].setStav('*');
        }
    }

    /**
     * Simuluje šírenie požiaru v mriežke lesa.
     */
    public void simulaciaSireniaPoziaru() {
        BunkaPoziar[][] newles = new BunkaPoziar[this.velkost][this.velkost];

        for (int i = 0; i < velkost; i++) {
            for (int j = 0; j < velkost; j++) {
                if (this.les[i][j].getStav() == '*' ||
                        (this.les[i][j].getStav() == 'T' && i > 0 && this.les[i - 1][j].getStav() == '*') ||
                        (this.les[i][j].getStav() == 'T' && i < this.velkost - 1 && this.les[i + 1][j].getStav() == '*') ||
                        (this.les[i][j].getStav() == 'T' && j > 0 && this.les[i][j - 1].getStav() == '*') ||
                        (this.les[i][j].getStav() == 'T' && j < this.velkost - 1 && this.les[i][j + 1].getStav() == '*')) {
                    newles[i][j] = new BunkaPoziar();
                    newles[i][j].setStav('*');
                } else {
                    newles[i][j] = new BunkaPoziar();
                    newles[i][j].setStav(this.les[i][j].getStav());
                }
            }
        }

        this.les = newles;
    }

    /**
     * Aktualizuje mriežku spáleného územia podľa aktuálneho stavu lesa.
     */
    public void updateSpalenaOblast() {
        for (int i = 0; i < velkost; i++) {
            for (int j = 1; j < velkost; j++) {
                if (this.les[i][j].getStav() == '*') {
                    this.spalenaOblast[i][j].setStav('*');
                }
            }
        }
    }
}