/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.awt.TrayIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import nicon.notify.core.Notification;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import view.JFConsole;

/**
 *
 * @author m-1
 */
public class ConnectionFTP {
    
    public FTPClient client = null;
    private static final Logger log = Logger.getLogger(ConnectionFTP.class);
    
    public ConnectionFTP() {
        client = new FTPClient();
    }
    
    public void conectFTP() {
        String sFTP = AppParameters.FTP_SERVICE;
        String sUser = AppParameters.FTP_USER;
        String sPassword = AppParameters.FTP_PASS;
        
        try {
            client.connect(sFTP);
            
            showServerReply(client);
            
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.warn("Falló la conexión.");
                return;
            }
            
            boolean login = client.login(sUser, sPassword);
            showServerReply(client);
            if (login) {
                log.info("Conexion FTP exitosa");
            } else {
                log.error("Falló la conexión FTP");
            }
        } catch (IOException ioe) {
            log.fatal(ioe.getStackTrace());
        }
    }
    
    public void disconectFTP() {
        try {
            client.logout();
            client.disconnect();
            log.info("Desconección exitosa!!!");
        } catch (IOException ioe) {
            log.error("No se logro desconectar del FTP");
            log.fatal(ioe.getStackTrace());
        }
    }
    
    public void sendFile(File file, JFConsole frame) {
        try {
            client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            
            log.info("URL: " + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            // Guardando el archivo en el servidor
            if (client.changeWorkingDirectory(AppParameters.FTP_FOLDER)) {
                if (client.storeFile(file.getName(), fis)) {
                    log.info("Se ha enviado el fichero");
                    try{
                    //Notification.desktopMessage("Información", "Se ha enviado un fichero");
                    frame.mensajeTrayIcon("Se ha enviado un fichero", TrayIcon.MessageType.INFO);
                    }catch(NullPointerException e){}
                } else {
                    log.warn("No se ha enviado el fichero");
                }
            }
        } catch (FileNotFoundException e) {
            log.error("No se encontro el archivo");
            log.fatal(e.getStackTrace());
        } catch (IOException e) {
            log.error("Ocurrio un error al enbiar el archivo");
            log.fatal(e.getStackTrace());
        }
        
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                log.info("SERVER: " + aReply);
            }
        }
    }
}
