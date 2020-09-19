package com;

import com.listeners.FrameListener;
import com.listeners.TabbedPaneChangeListener;
import com.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);
    

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandlerr.log(e);
        }
    }

    public Controller getController() {
        return controller;
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void init() {
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
    }

    public void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initEditMenu(this, menuBar);
        MenuHelper.initStyleMenu(this, menuBar);
        MenuHelper.initAlignMenu(this, menuBar);
        MenuHelper.initColorMenu(this, menuBar);
        MenuHelper.initFontMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);

        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void initEditor(){
         htmlTextPane.setContentType("text/html");

         JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
         tabbedPane.add("Text", jScrollPane);

         JScrollPane jScrollPane1 = new JScrollPane(plainTextPane);
         tabbedPane.add("HTML", jScrollPane1);

         tabbedPane.setPreferredSize(getPreferredSize());
         tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
         getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

   public boolean canUndo(){
        return undoManager.canUndo();
   }

   public boolean canRedo(){
        return undoManager.canRedo();
   }

   public void undo(){
        try {
            undoManager.undo();
        }catch (Exception e){
            ExceptionHandlerr.log(e);
        }
    }

    public void redo(){
        try{
            undoManager.redo();}
        catch (CannotRedoException e) {
            ExceptionHandlerr.log(e);
        }
    }


    public void resetUndo(){
      undoManager.discardAllEdits();
    }
    
    public boolean isHtmlTabSelected(){
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

   public void update(){
      htmlTextPane.setDocument(controller.getDocument());
   }

   public void showAbout() {
       JOptionPane.showMessageDialog(tabbedPane.getSelectedComponent(), "Версия 1.0\nАвтор: Тигран Оганесян", "О программме", JOptionPane.INFORMATION_MESSAGE);
   }

  public void selectedTabChanged() {
        int n = tabbedPane.getSelectedIndex();

        if(n == 0){
           controller.setPlainText(plainTextPane.getText());
        }

        if(n == 1){
            plainTextPane.setText(controller.getPlainText());
        }

        this.resetUndo();
    }

    public void exit() {
        controller.exit();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       String command = e.getActionCommand();
       switch (command){
           case "Новый":
               controller.createNewDocument();
               break;

           case "Открыть":
               controller.openDocument();
               break;

           case "Сохранить":
               controller.saveDocument();
               break;

           case "Сохранить как...":
               controller.saveDocumentAs();
               break;

           case "Выход":
               controller.exit();
               break;

           case "О программе":
               this.showAbout();
       }
    }
}
