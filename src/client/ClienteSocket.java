package client;

import java.awt.Desktop;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Cliente
 */
public class ClienteSocket {

    private static String fileName;

    public void run() {
        // String host = args[0];
        // Integer port = Integer.parseInt(args[1]);
        String host = "127.0.0.1";
        int port = 1111;
        try (Socket socket = new Socket(host, port);
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                Object fromServer = inputStream.readObject();
                if (fromServer instanceof String) {
                    String fromServerString = (String) fromServer;
                    System.out.println(fromServerString);
                    if (fromServerString.equalsIgnoreCase("Terminando...")) {
                        System.exit(0);
//                        break;
                    }
                } else if (fromServer instanceof byte[]) {
                    writeFile((byte[]) fromServer);
                } else if (fromServer instanceof String[]) {
                    System.out.println(Arrays.toString((String[]) fromServer));
                } else {
                    System.out.println("Ese archivo no existe");

                }

                System.out.print("> ");
                String userInput = scanner.nextLine();
                outputStream.writeObject(userInput);
                fileName = userInput;
//                if (userInput != null) {
//                }
            }
        } catch (UnknownHostException e) {
            System.out.println("No se encontro el host: " + host);
        } catch (IOException e) {
            System.out.println("Servidor termino inesperadamente");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(byte[] fileBytes) {
        File file = new File(fileName);
        try (FileOutputStream fileOutput = new FileOutputStream(file);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);) {
            bufferedOutput.write(fileBytes, 0, fileBytes.length);
            bufferedOutput.flush();
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
