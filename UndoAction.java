package com.actions;

import com.View;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoAction extends AbstractAction {
    private View view;

    public UndoAction(View view){
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
          view.undo();
    }
}
