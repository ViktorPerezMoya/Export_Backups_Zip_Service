/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package export_databases;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.AppParameters;
import negocio.ConnectionFTP;
import negocio.FileZip;
import negocio.Monitoreo;

/**
 *
 * @author m-1
 */
public class Export_databases {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Monitoreo().monitorear(AppParameters.LOCAL_FOLDER);
    }

}
