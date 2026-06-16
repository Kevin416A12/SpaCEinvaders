package Entidades;

import java.util.List;

public interface AlienFactory {

    List<Alien> crearFormacion();

    Ovni crearOvni();

    List<Escudo> crearEscudos();
}