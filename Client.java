/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author lavar
 */
public class Client {

    
    public static void main(String[] args) {
        try {
            Scanner scn = new Scanner(System.in);
                
            // obtener la ip del localhost
            InetAddress ip = InetAddress.getByName("localhost");
            
            // establecer la conexión con el servidor en el puerto 5056
            Socket s = new  Socket(ip,5056);
            
            // obtener flujos de entrada y salida
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos =  new DataOutputStream(s.getOutputStream());
            
            // el siguiente ciclo realiza el intercambio de información entre 
            // el cliente y el handler
            
            while(true) {
                System.out.println(dis.readUTF());
                String toSend = scn.nextLine();
                dos.writeUTF(toSend);
                
                // Si el cliente envía "Salir", se cierra la conexión
                // y luego se rompe el ciclo
                if (toSend.equals("Salir"))
                        {
                            System.out.println("Cerrando esta conexión: " + s);
                            s.close();
                            System.out.println("Conexión terminada");
                            break;
                        }
                // imprimir la fecha o el tiempo como lo pidió el cliente
                String received = dis.readUTF();
                System.out.println(received);
            }
            // Cerrando recursos
            scn.close();
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
}
