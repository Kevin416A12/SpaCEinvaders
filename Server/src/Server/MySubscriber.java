package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import Logica.Logica;

public class MySubscriber extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private MyPublisher publisher;
    private Map<Integer, Logica> partidas;

    private int partida;
    private Role role;

    public MySubscriber(Socket socket, MyPublisher publisher, Map<Integer, Logica> partidas) throws IOException {
        this.socket = socket;
        this.publisher = publisher;
        this.partidas = partidas;

        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            String registro = in.readLine();

            if (registro == null) {
                return;
            }

            procesarRegistro(registro);

            publisher.addSubscriber(partida, role, this);

            System.out.println("Cliente conectado a partida " + partida + " como " + role);

            String mensaje;

            while ((mensaje = in.readLine()) != null) {
                if (role == Role.CONTROL) {
                    Logica logica = partidas.get(partida);

                    if (logica != null) {
                        logica.recibirMensaje(mensaje);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado");
        }
    }

    private void procesarRegistro(String registro) {
        String[] partes = registro.split(" ");

        partida = Integer.parseInt(partes[1]);
        role = Role.valueOf(partes[2]);
    }
}