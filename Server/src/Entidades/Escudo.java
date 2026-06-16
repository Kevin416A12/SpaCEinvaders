package Entidades;

public class Escudo {

    private int x;
    private int y;

    private int vida;

    public Escudo(int x, int y) {

        this.x = x;
        this.y = y;

        vida = 4;
    }

    public void recibirImpacto() {

        if(vida > 0)
            vida--;
    }

    public boolean destruido() {
        return vida == 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getVida() {
        return this.vida;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
}
