package Logica;

import Entidades.*;
import Server.MyPublisher;
import Server.Role;

import java.util.List;

public class Logica extends Thread {

    private AlienFactory factory;

    private List<Alien> aliens;
    private List<Escudo> escudos;

    private Ovni ovni;
    private final int SIZE = 256;

    private int idPartida;
    private MyPublisher publisher;

    private int alienDirection = 2;

    private int playerX = 128;
    private int playerY = 240;

    private int score = 0;
    private boolean gameOver = false;


    public Logica(int idPartida, MyPublisher publisher) {
        this.idPartida = idPartida;
        this.publisher = publisher;

        factory = new SpaceInvadersFactory();

        aliens = factory.crearFormacion();

        escudos = factory.crearEscudos();

        ovni = null;
    }

    @Override
    public void run() {
        while (true) {
            if (ovni == null && Math.random() < 0.01) {
                ovni = factory.crearOvni();
            }
            moverOvni();

            moverAliens();

            verificarGameOver();

            verificarVictoria();

            enviarMatriz(false);
            if (!gameOver) {
                moverAliens();
                verificarGameOver();
                enviarMatriz(false);
            } else {
                enviarGameOver();
                break;
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public synchronized void recibirMensaje(String mensaje) {
        if (gameOver) {
            return;
        }

        System.out.println("Partida " + idPartida + " recibió: " + mensaje);

        if (mensaje.equals("Derecha")) {
            moverJugadorDerecha();
        } else if (mensaje.equals("Izquierda")) {
            moverJugadorIzquierda();
        } else if (mensaje.equals("Disparar")) {
            disparar();
            enviarMatriz(true);
            return;
        }

        enviarMatriz(false);
    }

    private void moverAliens() {
        boolean cambiarDireccion = false;

        for (Alien alien : aliens) {

            if (!alien.estaVivo()) {
                continue;
            }

            int siguienteX = alien.getX() + alienDirection;

            if (siguienteX <= 0 || siguienteX >= SIZE - 1) {
                cambiarDireccion = true;
                break;
            }
        }

        if (ovni != null) {

            ovni.mover();

            if (ovni.fueraPantalla(SIZE)) {
                ovni = null;
            }
        }

        if (cambiarDireccion) {

            alienDirection *= -1;

            for (Alien alien : aliens) {

                if (alien.estaVivo()) {
                    alien.bajar();
                }
            }

        } else {

            for (Alien alien : aliens) {

                if (alien.estaVivo()) {
                    alien.moverHorizontal(alienDirection);
                }
            }
        }
    }

    private void verificarGameOver() {
        for (Alien alien : aliens) {

            if (!alien.estaVivo()) {
                continue;
            }

            if (alien.getY() >= playerY - 10) {
                gameOver = true;
                return;
            }
        }
    }

    private void verificarVictoria() {

        for (Alien alien : aliens) {

            if (alien.estaVivo()) {
                return;
            }
        }

        gameOver = true;
    }

    private void moverJugadorDerecha() {
        if (playerX < SIZE - 10) {
            playerX += 5;
        }
    }

    private void moverJugadorIzquierda() {
        if (playerX > 10) {
            playerX -= 5;
        }
    }

    private void moverOvni() {

        if (ovni == null) {
            return;
        }

        ovni.mover();

        if (ovni.fueraPantalla(SIZE)) {
            ovni = null;
        }
    }

    private void disparar() {

        for (int y = playerY; y >= 0; y--) {

            for (Alien alien : aliens) {

                if (!alien.estaVivo()) {
                    continue;
                }

                if (tocaAlien(
                        playerX,
                        y,
                        alien.getX(),
                        alien.getY(),
                        alien.getValorMatriz())) {

                    alien.destruir();

                    score += alien.getPuntaje();

                    return;
                }
            }


        // Colisión con ovni

            if (ovni != null &&
                    tocaAlien(playerX,
                            y,
                            ovni.getX(),
                            ovni.getY(),
                            4)) {

                score += ovni.getPuntaje();

                ovni = null;

                return;
            }
        }
    }

    private boolean tocaAlien(
            int x,
            int y,
            int objetoX,
            int objetoY,
            int tipo) {

        int[][] forma;

        if (tipo == 4) {
            forma = SpriteDrawer.obtenerFormaOvni();
        } else {
            forma = SpriteDrawer.obtenerFormaAlien(tipo);
        }

        int localX = x - objetoX;
        int localY = y - objetoY;

        if (localY < 0 || localY >= forma.length) {
            return false;
        }

        if (localX < 0 || localX >= forma[0].length) {
            return false;
        }

        return forma[localY][localX] == 1;
    }

    private void enviarMatriz(boolean mostrarRayo) {

        int[][] matriz = new int[SIZE][SIZE];


    // Dibujar aliens

        for (Alien alien : aliens) {

            if (!alien.estaVivo()) {
                continue;
            }

            SpriteDrawer.dibujarAlien(
                    matriz,
                    alien.getX(),
                    alien.getY(),
                    alien.getValorMatriz());
        }


    // Dibujar ovni

        if (ovni != null) {

            SpriteDrawer.dibujarOvni(
                    matriz,
                    ovni.getX(),
                    ovni.getY());
        }


    // Dibujar escudos

        for (Escudo escudo : escudos) {

            if (!escudo.destruido()) {

                SpriteDrawer.dibujarEscudo(
                        matriz,
                        escudo.getX(),
                        escudo.getY());
            }
        }


    // Dibujar jugador

        SpriteDrawer.dibujarJugador(
                matriz,
                playerX,
                playerY);


    // Dibujar rayo

        if (mostrarRayo) {

            for (int y = playerY - 1; y >= 0; y--) {

                matriz[y][playerX] = 5;
            }
        }

        enviar(matriz);
    }

    private void enviarGameOver() {
        int[][] matriz = new int[SIZE][SIZE];

        SpriteDrawer.escribirTexto(matriz, "GAME OVER", 55, 90, 5);
        SpriteDrawer.escribirTexto(matriz, "SCORE " + score, 55, 120, 4);

        enviar(matriz);
    }

    private void enviar(int[][] matriz) {
        StringBuilder sb = new StringBuilder();

        sb.append("SCORE ").append(score).append("\n");
        sb.append("MATRIX\n");

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(matriz[i][j]);
            }
            sb.append("\n");
        }

        sb.append("END\n");
        System.out.println("----- MATRIZ ENVIADA -----");
        System.out.println(sb.toString());

        publisher.sendTo(idPartida, Role.DISPLAY, sb.toString());
        publisher.sendTo(idPartida, Role.CONTROL, sb.toString());
    }
}