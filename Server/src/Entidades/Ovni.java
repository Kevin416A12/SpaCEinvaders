package Entidades;

public class Ovni {

    private int x;
    private int y;

    private int direccion;

    private int puntaje;

    public Ovni(int x, int y, int direccion) {

        this.x = x;
        this.y = y;
        this.direccion = direccion;

        puntaje = generarPuntaje();
    }

    private int generarPuntaje() {

        int[] valores = {50,100,150,300};

        return valores[(int)(Math.random()*4)];
    }

    public void mover() {
        x += direccion;
    }

    public boolean fueraPantalla(int ancho){
        return x < 0 || x >= ancho;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPuntaje() {
        return this.puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
