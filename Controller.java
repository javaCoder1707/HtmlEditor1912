package com;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view){
        this.view = view;
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void init() {
     createNewDocument();
    }

    public void resetDocument(){
        if(document != null) document.removeUndoableEditListener(view.getUndoListener());

        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

   public void setPlainText(String text){
       resetDocument();
       try {
           new HTMLEditorKit().read(new StringReader(text), document, 0);
       } catch (Exception e) {
           ExceptionHandlerr.log(e);
       }
    }

    public String getPlainText(){
        StringWriter writer = new StringWriter();
        try {
            new HTMLEditorKit().write(writer, document, 0 , document.getLength());
        }catch (Exception e) {
            ExceptionHandlerr.log(e);
        }
        return writer.toString();
    }

    public void createNewDocument(){
        view.selectHtmlTab();

        resetDocument();

        view.setTitle("HTML редактор");
        view.resetUndo();

        currentFile = null;
    }

    public void openDocument(){
        view.selectHtmlTab();

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        jFileChooser.setDialogTitle("Open File");

        int index = jFileChooser.showOpenDialog(view);

        if(index == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());

            resetDocument();
            try {
                FileReader fileReader = new FileReader(currentFile);

                new HTMLEditorKit().read(fileReader, document, 0);
                fileReader.close();

                view.resetUndo();
            } catch (IOException | BadLocationException e) {
                ExceptionHandlerr.log(e);
            }
        }
    }

    public void saveDocument(){
        if(currentFile == null){
            saveDocumentAs();
            return;
        }

        view.selectHtmlTab();
        view.setTitle(currentFile.getName());
            try {
                FileWriter fileWriter = new FileWriter(currentFile);

                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                fileWriter.close();

            } catch (IOException | BadLocationException e) {
                ExceptionHandlerr.log(e);
            }

    }

    public void saveDocumentAs(){
        view.selectHtmlTab();

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        jFileChooser.setDialogTitle("Save File");

        int index = jFileChooser.showSaveDialog(view);

        if(index == JFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter fileWriter = new FileWriter(currentFile);

                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                fileWriter.close();

            } catch (IOException | BadLocationException e) {
                ExceptionHandlerr.log(e);
            }
        }

    }

    public void exit(){
        System.exit(0);
    }

    public static void main(String[] args) {
     View view = new View();
     Controller controller = new Controller(view);

     view.setController(controller);
     view.init();
     controller.init();
    }
}
