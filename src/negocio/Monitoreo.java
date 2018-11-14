/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import nicon.notify.core.Notification;
import org.apache.log4j.Logger;

/**
 *
 * @author m-1
 */
public class Monitoreo {

    private static final Logger log = Logger.getLogger(Monitoreo.class);

    public Monitoreo() {
        initConfig();
    }

    public void monitorear(String directory) {

        log.info("WatchService in " + directory);

        try {
            // Obtenemos el directorio
            Path folder = Paths.get(directory);

            // Solicitamos el servicio WatchService
            WatchService observador = folder.getFileSystem().newWatchService();

            // Registramos los eventos que queremos monitorear
            folder.register(observador, new WatchEvent.Kind[]{ENTRY_CREATE});//other options  ENTRY_DELETE, ENTRY_MODIFY

            log.info("Inicio de observacion en el directorio " + directory);
            try{
            Notification.desktopMessage("Información", "Inicio de observacion en el directorio " + directory);
            }catch(NullPointerException e){
                
            }
            // Esperamos que algo suceda con el directorio
            WatchKey key = observador.take();

            // Algo ocurrio en el directorio para los eventos registrados
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    String fileName = event.context().toString();
                    //Traesmos el ultimo Zip
                    FileZip fz = new FileZip();
                    File zp = fz.getZip(directory, fileName);
                    //Conectamos al FTP
                    ConnectionFTP cnFTP = new ConnectionFTP();
                    cnFTP.conectFTP();
                    //tansferencia de el ultimo zip al ftp
                    cnFTP.sendFile(zp);
                    cnFTP.disconectFTP();
                }

                // Volvemos a escuchar. Lo mantenemos en un loop para escuchar indefinidamente.
                key.reset();
                key = observador.take();
            }

        } catch (InterruptedException e) {
            log.fatal(e.getStackTrace());
        } catch (IOException ex) {
            log.fatal(ex.getStackTrace());
        }
    }

    private void initConfig() {

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            String path = System.getProperty("user.dir");
            log.info("Path: " + path);
            String fileName = "config.conf";
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(path + "\\" + fileName);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] key_value = linea.split("=");
                switch (key_value[0]) {
                    case "ftp_service":
                        AppParameters.FTP_SERVICE = key_value[1];
                        break;
                    case "ftp_user":
                        AppParameters.FTP_USER = key_value[1];
                        break;
                    case "ftp_pass":
                        AppParameters.FTP_PASS = key_value[1];
                        break;
                    case "ftp_folder":
                        AppParameters.FTP_FOLDER = key_value[1];
                        break;
                    case "local_folder":
                        AppParameters.LOCAL_FOLDER = key_value[1];
                        break;
                }
            }
        } catch (Exception e) {
            log.fatal(e.getStackTrace());
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                log.fatal(e2.getStackTrace());
                e2.printStackTrace();
            }
        }
    }
}
