/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author m-1
 */
public class FileZip {
    private static final Logger log = Logger.getLogger(FileZip.class);
    //Obtiene el ultimo zip
    public File getLastZip(String url) {
        File dir = new File(url);
        String[] zips_list = dir.list();
        if (zips_list != null) {
            String ult_zip = zips_list[zips_list.length - 1];
            File fichero = new File(url + ult_zip);
            System.out.println("File: " + fichero.getName());
            return fichero;
        }
        log.warn("No hay ficheros");
        return null;
    }
    //trae un fichero 
    public File getZip(String url, String fileName) {
        File fichero = new File(url + fileName);
        if (fichero.exists()) {
            System.out.println("File: " + fichero.getName());
            return fichero;
        }
        log.warn("No hay ficheros");
        return null;
    }
}
