/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lavar
 */
public class ClientHandler extends Thread{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    DataInputStream dis = null;
    DataOutputStream dos;
    Socket s;
    
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }
    
    @Override 
    public void run() {
        String received;
        String toreturn;
        while(true) {
            try {
                // Preguntar al usuario que es lo que necesita
                dos.writeUTF("Que deseas [Fecha | Hora]..\n" + "Escribe Salir para terminar la conexión.");
                // recibe una respuesta del cliente
            
                received = dis.readUTF();
                if(received.equals("Salir")){
                    System.out.println("Cliente " + this.s + "envia salida...");
                    System.out.println("Cerrando esta conexión...");
                    this.s.close();
                    System.out.println("Conexión cerrada");
                    break;
                }
                
                // creando el objeto de la fecha
                Date date = new Date();
                
                // escribir en el flujo de salida basado en la respuesta del cliente.
                switch(received) {
                    case "Fecha":
                        toreturn =  fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;
                    case "Hora":
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;
                    default:
                        dos.writeUTF("Entrada invalida");
                        break;
                }
            } catch (IOException ex) {
                System.getLogger(ClientHandler.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }   
        }
        try {
                // Cerrando los flujos de entrada y salida
                this.dis.close();
                this.dos.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
    }
}
