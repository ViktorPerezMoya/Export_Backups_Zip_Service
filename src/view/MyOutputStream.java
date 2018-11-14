/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;

/**
 *
 * @author m-1
 */
public class MyOutputStream  extends OutputStream {

    private final JEditorPane textArea;
    private String contenido= "";



    public MyOutputStream(JEditorPane textArea) {
        this.textArea = textArea;
        textArea.setContentType("text/plain");

    }


    @Override

    public void write(int b) throws IOException {
        
        this.contenido = this.textArea.getText();
        
        // redirects data to the text area
        
        textArea.setText(contenido+String.valueOf((char)b));

        // scrolls the text area to the end of data

        textArea.setCaretPosition(textArea.getDocument().getLength());

        // keeps the textArea up to date

        textArea.update(textArea.getGraphics());

    }
    
}
