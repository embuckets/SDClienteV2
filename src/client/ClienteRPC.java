/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Desktop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilio
 */
public class ClienteRPC {

    private boolean listening = true;

    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://127.0.0.1:2222/xmlrpc"));
//            config.setEnabledForExceptions(false);
            config.setEnabledForExceptions(true);
            config.setEnabledForExtensions(true);
            config.setConnectionTimeout(60 * 1000);
            config.setReplyTimeout(60 * 1000);
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] files = (Object[]) client.execute("FileManager.getFilesNames", new Object[]{});
            System.out.println("Escribe 'q' para salir");
            System.out.println("Escribe 'ls' para listar archivos");
            System.out.println("Elige el archivo que quieres ver:");
            System.out.println(Arrays.toString(files));

            while (listening) {
                System.out.print("> ");
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Saliendo");
                    listening = false;
                    System.exit(0);
                    break;
                } else if (userInput.equalsIgnoreCase("ls")) {
                    files = (Object[]) client.execute("FileManager.getFilesNames", new Object[]{});
                    System.out.println(Arrays.toString(files));
                    //filemanager.getfilesnames
                } else {

                    byte[] fileBytes = (byte[]) client.execute("FileManager.getFile", new Object[]{userInput});
                    if (fileBytes != null) {

                        createTempFile(fileBytes, userInput);
                    } else {
                        System.out.println("No se encontro el archivo");
                    }
                }

            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ClienteRPC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlRpcException ex) {
            Logger.getLogger(ClienteRPC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createTempFile(byte[] bytes, String fileName) {
//        String[] fileTokens = fileName.split("\\.");
        try {
            File file = new File(fileName);
            FileOutputStream fileOutput = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            bufferedOutput.write(bytes, 0, bytes.length);
            bufferedOutput.flush();
            fileOutput.close();
            bufferedOutput.close();
            System.out.println("El archivo " + fileName + " se escribio en " + file.getAbsolutePath());

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
