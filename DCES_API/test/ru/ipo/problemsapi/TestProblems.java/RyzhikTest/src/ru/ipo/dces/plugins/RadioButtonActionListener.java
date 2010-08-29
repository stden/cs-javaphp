package ru.ipo.dces.plugins;

import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 22.04.2009
 * Time: 12:06:48
 */
public class RadioButtonActionListener implements ActionListener {

    private int problemNumber;
    private JRadioButton button;
    private PluginEnvironment env;

    public RadioButtonActionListener(int problemNumber, JRadioButton button, PluginEnvironment env) {
        this.problemNumber = problemNumber;
        this.button = button;
        this.env = env;
    }

    public void actionPerformed(ActionEvent e) {
        //is called when radio button is pressed

        HashMap<String, String> sol = new HashMap<String, String>();
        sol.put("action", "answer"); //в кнопке для подсчета результат "action" -> "get results"
        sol.put("question", "" + problemNumber);
        sol.put("answer", button.getText());

        try {
            HashMap<String, String> ans = env.submitSolution(sol);
        } catch (GeneralRequestFailureException e1) {
            //do nothing
        }
    }
}
