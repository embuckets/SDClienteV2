/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import resources.FileManagerInterface;

import java.awt.Desktop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilio
 */
public class ClienteRMI {

    private boolean listening = true;

    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            FileManagerInterface fileManager = (FileManagerInterface) Naming.lookup("rmi://localhost:1099/fileservice");
            System.out.println("Escribe 'q' para salir");
            System.out.println("Escribe 'ls' para ver los archivos");
            System.out.println("Escribe el arhivo que quieres ver:");
            System.out.println(Arrays.toString(fileManager.getFilesNames()));
            while (listening) {
                System.out.print("> ");
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Saliendo");
                    listening = false;
                    System.exit(0);
                    break;
                } else if (userInput.equalsIgnoreCase("ls")) {
                    String[] files = fileManager.getFilesNames();
                    System.out.println(Arrays.toString(files));
                    //filemanager.getfilesnames
                } else {
                    byte[] fileBytes = fileManager.getFile(userInput);
                    if (fileBytes != null) {
                        createTempFile(fileBytes, userInput);

                    } else {
                        System.out.println("No existe ese archivo");
                    }

                }
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ClienteRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createTempFile(byte[] fileBytes, String name) {
        try {
            File file = new File(name);
            FileOutputStream fileOutput = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            bufferedOutput.write(fileBytes, 0, fileBytes.length);
            bufferedOutput.flush();
            fileOutput.close();
            bufferedOutput.close();
            System.out.println("El archivo " + name + " se escribio en " + file.getAbsolutePath());

            System.out.println("Quieres abrir el archivo? [y / n] ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("y")) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(file);

                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ClienteRPC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
