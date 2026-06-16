package Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import Logica.Logica;

public class Server extends Thread {
    private int port;
    private MyPublisher publisher;
    private Map<Integer, Logica> partidas;

    public Server(int port, Map<Integer, Logica> partidas) {
        this.port = port;
        this.partidas = partidas;
        this.publisher = new MyPublisher();
    }

    public MyPublisher getPublisher() {
        return publisher;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en puerto " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                MySubscriber subscriber = new MySubscriber(socket, publisher, partidas);
                subscriber.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}