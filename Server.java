/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lavar
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // El servidor está escuchando en el puerto 5056
        ServerSocket ss = new ServerSocket(5056);
        System.out.println("Servidor iniciado exitosamente. Esperando clientes en el puerto 5056...");
        
        // ciclo infinito para la solicitud del cliente.
        while(true) {
            Socket s = null;
            
            try {
                // El socket recibe un nuevo cliente
                s = ss.accept();
                System.out.println("Se ha conectado un nuevo cliente: " + s);
                
                // obtener el flujo de entrada y salida
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                
                System.out.println("Asignando nuevo hilo para este cliente");
                
                // crear un nuevo hilo
                Thread t = new ClientHandler(s, dis, dos);
                
                // Llamando al método .start()
                t.start();
            } catch(IOException i) {
                s.close();
                i.printStackTrace();
            }
        }
    }   
}


