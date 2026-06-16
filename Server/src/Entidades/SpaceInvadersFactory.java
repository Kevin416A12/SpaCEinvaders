package Entidades;


import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersFactory implements AlienFactory {
    private static final int SEPARACION_X = 10;
    private static final int SEPARACION_Y = 10;
    private static final int OFFset_Y = 10;
    @Override
    public List<Alien> crearFormacion() {

        List<Alien> aliens = new ArrayList<>();

        for (int j = 0; j < 11; j++)
            aliens.add(new Calamar(j * SEPARACION_X, 0 * SEPARACION_Y));

        for (int j = 0; j < 11; j++) {
            aliens.add(new Cangrejo(j * SEPARACION_X, 1 * SEPARACION_Y));
            aliens.add(new Cangrejo(j * SEPARACION_X, 2 * SEPARACION_Y));
        }

        for (int j = 0; j < 11; j++) {
            aliens.add(new Pulpo(j * SEPARACION_X, 3 * SEPARACION_Y));
            aliens.add(new Pulpo(j * SEPARACION_X, 4 * SEPARACION_Y));
        }

        return aliens;
    }

    @Override
    public Ovni crearOvni() {

        int direccion =
                Math.random() < 0.5 ? 1 : -1;

        int x =
                direccion == 1 ? 0 : 54;

        return new Ovni(x,0,direccion);
    }

    @Override
    public List<Escudo> crearEscudos() {

        List<Escudo> escudos = new ArrayList<>();

        final int GRID_SIZE = 256;

        final int CANTIDAD_ESCUDOS = 4;
        final int SEPARACION_X = 50;
        final int POS_Y = 220;

        // centrado horizontal
        int anchoTotal = (CANTIDAD_ESCUDOS - 1) * SEPARACION_X;
        int inicioX = (GRID_SIZE - anchoTotal) / 2;

        for (int i = 0; i < CANTIDAD_ESCUDOS; i++) {
            int x = inicioX + i * SEPARACION_X;
            escudos.add(new Escudo(x, POS_Y));
        }

        return escudos;
    }
}
