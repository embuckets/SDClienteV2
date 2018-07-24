/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author emilio
 */
public interface FileManagerInterface extends Remote {

    String[] getFilesNames() throws RemoteException;

    byte[] getFile(String name) throws RemoteException;

}
