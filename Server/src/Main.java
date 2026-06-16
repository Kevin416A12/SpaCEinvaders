import java.util.HashMap;
import java.util.Map;

import Logica.Logica;
import Server.MyPublisher;
import Server.Server;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Logica> partidas = new HashMap<>();

        Server server = new Server(5000, partidas);
        MyPublisher publisher = server.getPublisher();

        Logica partida1 = new Logica(1, publisher);
        Logica partida2 = new Logica(2, publisher);

        partidas.put(1, partida1);
        partidas.put(2, partida2);

        server.start();

        partida1.start();
        partida2.start();
    }
}