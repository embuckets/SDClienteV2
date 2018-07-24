/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import client.ClienteRMI;
import client.ClienteRPC;
import client.ClienteSocket;

import java.util.Scanner;

/**
 *
 * @author emilio
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Escribe 'q' para salir");
        System.out.println("A que servidor te quieres conectar?\n"
                + "[socket, rmi, rpc]");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String userInput = null;
        while (true) {
            userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("rpc")) {
                new ClienteRPC().run();
            } else if (userInput.equalsIgnoreCase("rmi")) {
                new ClienteRMI().run();
            } else if (userInput.equalsIgnoreCase("socket")) {
                new ClienteSocket().run();
            } else if (userInput.equalsIgnoreCase("q")) {
                System.out.println("Saliendo");
                break;
            } else {
                System.out.println("Opcion invalida. Intentea de nuevo");
                System.out.println("A que servidor te quieres conectar?\n"
                        + "[socket, rmi, rpc]");
                System.out.print("> ");
            }

        }
    }

}
