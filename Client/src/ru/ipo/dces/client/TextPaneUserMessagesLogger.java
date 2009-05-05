package ru.ipo.dces.client;

import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.log.UserMessagesLogger;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.Date;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.03.2009
 * Time: 0:45:12
 */

public class TextPaneUserMessagesLogger implements UserMessagesLogger {

  private final JTextPane pane;
  private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
  private final DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);

  public TextPaneUserMessagesLogger(JTextPane pane) {
    this.pane = pane;
  }

  public void log(String message, LogMessageType type, String sender) {

    try {

      switch (type) {
        case Error:
          StyleConstants.setForeground(attributeSet, Color.RED);
          break;
        case OK:
          StyleConstants.setForeground(attributeSet, Color.GREEN);
          break;
        case Warning:
          StyleConstants.setForeground(attributeSet, Color.ORANGE);
          break;
      }

      pane.getDocument().insertString(0, "\n", attributeSet);
      StyleConstants.setBold(attributeSet, true);
      pane.getDocument().insertString(0, message, attributeSet);

      StyleConstants.setForeground(attributeSet, Color.WHITE);

      StyleConstants.setBold(attributeSet, false);
      pane.getDocument().insertString(0, ": ", attributeSet);
      pane.getDocument().insertString(0, sender, attributeSet);
      pane.getDocument().insertString(0, " ", attributeSet);
      pane.getDocument().insertString(0, dateFormat.format(new Date()), attributeSet);
    } catch (BadLocationException e) {
      //do nothing
    }

    //pane.scrollRectToVisible(new Rectangle(1, 1));
    pane.setCaretPosition(0);
  }

  /*
  private int lastLocation() {
    return pane.getDocument().getLength();
  }

  public void log(String message, LogMessageType type, String sender) {

    try {

      StyleConstants.setForeground(attributeSet, Color.WHITE);

      StyleConstants.setBold(attributeSet, false);

      pane.getDocument().insertString(lastLocation(), dateFormat.format(new Date()), attributeSet);
      pane.getDocument().insertString(lastLocation(), " ", attributeSet);
      pane.getDocument().insertString(lastLocation(), sender, attributeSet);
      pane.getDocument().insertString(lastLocation(), ": ", attributeSet);

      switch (type) {
        case Error:
          StyleConstants.setForeground(attributeSet, Color.RED);
          break;
        case OK:
          StyleConstants.setForeground(attributeSet, Color.GREEN);
          break;
        case Warning:
          StyleConstants.setForeground(attributeSet, Color.ORANGE);
          break;
      }

      StyleConstants.setBold(attributeSet, true);
      pane.getDocument().insertString(lastLocation(), message, attributeSet);
      pane.getDocument().insertString(lastLocation(), "\n", attributeSet);

    } catch (BadLocationException e) {
      //do nothing
      System.out.println("asdf");
    }
  }*/

}
