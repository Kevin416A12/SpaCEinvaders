package Logica;
//0 = vacío
//1 = alien tipo 1
//2 = alien tipo 2
//3 = alien tipo 3
//4 = jugador
//5 = rayo
//6 = bunker
//7 = OVNI
public class SpriteDrawer {

    public static void dibujarAlien(int[][] matriz, int x, int y, int tipo) {
        int[][] forma = obtenerFormaAlien(tipo);
        dibujarForma(matriz, forma, x, y, tipo);
    }

    public static void dibujarJugador(int[][] matriz, int x, int y) {
        int[][] jugador = {
                {0,0,1,0,0},
                {0,1,1,1,0},
                {1,1,1,1,1},
                {1,0,1,0,1}
        };

        dibujarForma(matriz, jugador, x - 2, y, 4);
    }

    public static void dibujarOvni(int[][] matriz, int x, int y) {

        int[][] forma = obtenerFormaOvni();

        for (int fila = 0; fila < forma.length; fila++) {

            for (int col = 0; col < forma[fila].length; col++) {

                if (forma[fila][col] == 1) {

                    int drawY = y + fila;
                    int drawX = x + col;

                    if (drawY >= 0 &&
                            drawY < matriz.length &&
                            drawX >= 0 &&
                            drawX < matriz[0].length) {

                        matriz[drawY][drawX] = 7; // 👈 FIX IMPORTANTE
                    }
                }
            }
        }
    }

    public static int[][] obtenerFormaEscudo() {

        return new int[][]{
                {0,1,1,1,0},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,0,0,0,1}
        };
    }

    public static void dibujarEscudo(
            int[][] matriz,
            int x,
            int y) {

        int[][] forma = obtenerFormaEscudo();

        for (int fila = 0; fila < forma.length; fila++) {

            for (int col = 0; col < forma[fila].length; col++) {

                if (forma[fila][col] == 1) {

                    int drawY = y + fila;
                    int drawX = x + col;

                    if (drawY >= 0 &&
                            drawY < matriz.length &&
                            drawX >= 0 &&
                            drawX < matriz[0].length) {

                        matriz[drawY][drawX] = 6;
                    }
                }
            }
        }
    }

    public static int[][] obtenerFormaOvni() {
        return new int[][]{
                {0,0,1,1,1,1,1,1,0,0},
                {0,1,1,1,1,1,1,1,1,0},
                {1,1,0,1,1,1,1,0,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {0,1,1,0,1,1,0,1,1,0}
        };
    }

    public static void escribirTexto(int[][] matriz, String texto, int x, int y, int valor) {
        int offsetX = 0;

        for (char c : texto.toCharArray()) {
            dibujarLetra(matriz, c, x + offsetX, y, valor);
            offsetX += 18;
        }
    }

    public static int[][] obtenerFormaAlien(int tipo) {
        if (tipo == 1) {
            return new int[][]{
                    {0,1,0,0,0,1,0},
                    {0,0,1,0,1,0,0},
                    {0,1,1,1,1,1,0},
                    {1,1,0,1,0,1,1},
                    {1,1,1,1,1,1,1},
                    {0,1,0,1,0,1,0},
                    {1,0,0,0,0,0,1}
            };
        } else if (tipo == 2) {
            return new int[][]{
                    {0,0,1,1,1,0,0},
                    {0,1,1,1,1,1,0},
                    {1,1,0,1,0,1,1},
                    {1,1,1,1,1,1,1},
                    {0,1,0,1,0,1,0},
                    {1,0,1,0,1,0,1},
                    {0,1,0,0,0,1,0}
            };
        } else {
            return new int[][]{
                    {0,1,0,1,0,1,0},
                    {1,1,1,1,1,1,1},
                    {1,0,1,1,1,0,1},
                    {1,1,1,1,1,1,1},
                    {0,1,1,0,1,1,0},
                    {1,0,0,0,0,0,1},
                    {0,1,0,0,0,1,0}
            };
        }
    }

    private static void dibujarForma(int[][] matriz, int[][] forma, int x, int y, int valor) {
        int size = matriz.length;

        for (int i = 0; i < forma.length; i++) {
            for (int j = 0; j < forma[i].length; j++) {
                if (forma[i][j] == 1) {
                    int globalY = y + i;
                    int globalX = x + j;

                    if (globalY >= 0 && globalY < size &&
                            globalX >= 0 && globalX < size) {
                        matriz[globalY][globalX] = valor;
                    }
                }
            }
        }
    }

    private static void dibujarLetra(int[][] matriz, char c, int x, int y, int valor) {
        String[] forma = obtenerLetra(c);
        if (forma == null) return;

        int escala = 3;
        int size = matriz.length;

        for (int i = 0; i < forma.length; i++) {
            for (int j = 0; j < forma[i].length(); j++) {
                if (forma[i].charAt(j) == '1') {
                    for (int dy = 0; dy < escala; dy++) {
                        for (int dx = 0; dx < escala; dx++) {
                            int globalY = y + i * escala + dy;
                            int globalX = x + j * escala + dx;

                            if (globalY >= 0 && globalY < size &&
                                    globalX >= 0 && globalX < size) {
                                matriz[globalY][globalX] = valor;
                            }
                        }
                    }
                }
            }
        }
    }

    private static String[] obtenerLetra(char c) {
        switch (c) {
            case 'G':
                return new String[]{"11111","10000","10111","10001","11111"};
            case 'A':
                return new String[]{"01110","10001","11111","10001","10001"};
            case 'M':
                return new String[]{"10001","11011","10101","10001","10001"};
            case 'E':
                return new String[]{"11111","10000","11110","10000","11111"};
            case 'O':
                return new String[]{"11111","10001","10001","10001","11111"};
            case 'V':
                return new String[]{"10001","10001","10001","01010","00100"};
            case 'R':
                return new String[]{"11110","10001","11110","10100","10010"};
            case 'S':
                return new String[]{"11111","10000","11111","00001","11111"};
            case 'C':
                return new String[]{"11111","10000","10000","10000","11111"};
            case '0':
                return new String[]{"11111","10001","10001","10001","11111"};
            case '1':
                return new String[]{"00100","01100","00100","00100","01110"};
            case '2':
                return new String[]{"11111","00001","11111","10000","11111"};
            case '3':
                return new String[]{"11111","00001","11111","00001","11111"};
            case '4':
                return new String[]{"10001","10001","11111","00001","00001"};
            case '5':
                return new String[]{"11111","10000","11111","00001","11111"};
            case '6':
                return new String[]{"11111","10000","11111","10001","11111"};
            case '7':
                return new String[]{"11111","00001","00010","00100","00100"};
            case '8':
                return new String[]{"11111","10001","11111","10001","11111"};
            case '9':
                return new String[]{"11111","10001","11111","00001","11111"};
            case ' ':
                return new String[]{"00000","00000","00000","00000","00000"};
            default:
                return null;
        }
    }
}