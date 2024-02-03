/**
 * Trieda BunkaPoziar predstavuje bunku v simulácii lesného požiaru.
 *  
 * @author: Daniel J.
 * @version 1.0
 */
public class BunkaPoziar {
    private char stav;

    /**
     * Konštruktor pre vytvorenie bunky so stavom ' '.
     */
    public BunkaPoziar() {
        this.stav = ' ';
    }

    /**
     * Metóda vráti aktuálny stav bunky.
     * 
     * @return Aktuálny stav bunky.
     */
    public char getStav() {
        return this.stav;
    }

    /**
     * Metóda nastaví nový stav bunky.
     * 
     * @param stav Nový stav bunky.
     */
    public void setStav(char stav) {
        this.stav = stav;
    }
}