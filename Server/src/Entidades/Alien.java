package Entidades;

public abstract class Alien {
    protected int x;
    protected int y;
    protected int puntaje;
    protected int valorMatriz;
    protected boolean vivo;

    public Alien(int x, int y,
                 int puntaje,
                 int valorMatriz) {

        this.x = x;
        this.y = y;
        this.puntaje = puntaje;
        this.valorMatriz = valorMatriz;
        vivo = true;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getPuntaje() { return puntaje; }

    public int getValorMatriz() { return valorMatriz; }

    public void moverHorizontal(int dx){
        x += dx;
    }

    public void bajar(){
        y++;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void destruir() {
        this.vivo = false;
    }
}
