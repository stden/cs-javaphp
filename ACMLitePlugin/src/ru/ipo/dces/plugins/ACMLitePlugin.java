package ru.ipo.dces.plugins;


import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.io.*;

@DCESPluginLoadable

public class ACMLitePlugin extends JPanel implements Plugin {

    private JTextPane statementTextPane;
    private JPanel pluginPanel;
    private JButton ButtonVh;
    private JButton ButtonSubmit;

    private JTextArea textArea1;
    private JTextArea textArea2;


    private JTextField answerTextField;
    private JFileChooser jFileChooser = null;

    private final PluginEnvironment env;

    public ACMLitePlugin(PluginEnvironment pluginEnvironment) {
        env = pluginEnvironment;
        $$$setupUI$$$();
        env.setTitle(pluginEnvironment.getProblemName());


        showStatement();
        showIn();
        textArea1.setEnabled(false);
        textArea1.setText("Решение ещё не отправлено");


        ButtonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });


        ButtonVh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        textArea2.addComponentListener(new ComponentAdapter() {
        });
    }


    private void showStatement() {

        /*TODO
        * 1. get directory env.getProblemFolder()
        * 2. if there is statement.html -> show it
        * 3. else if there is a set of html files -> show any

        */
        File folder = env.getProblemFolder();

        final String title = "Заголовок";
        String resName = "";
        String resExt = "";
        int curPriority = 1;
        String filename = "";

        try {
            statementTextPane.setContentType("text/html");
            resName = "file:///" + folder.getCanonicalPath() + "/text.html";
            statementTextPane.setPage(resName);
        }
        catch (IOException e) {

            statementTextPane.setText("Не удается отобразить условие, свяжитесь с организаторами");

        }


    }


    private void showIn() {

        /*TODO
        * 1. get directory env.getProblemFolder()
        * 2. if there is statement.html -> show it
        * 3. else if there is a set of html files -> show any

        */
        File folder = env.getProblemFolder();

        final String title = "Заголовок";

        String resExt = "";
        int curPriority = 1;
        String resName = "";


        try {


            resName = folder.getCanonicalPath() + "/01";
            BufferedReader br = new BufferedReader(new FileReader(resName));
            textArea2.read(br, null);
            br.close();
            //setTitle(title);

            // getJTextPane().setText(s);
            //statementTextPane.setText(s);


        }
        catch (IOException e) {

            textArea2.setText("Не удается отобразить входной файл, свяжитесь с организаторами");

        }

    }


    private void createUIComponents() {
        pluginPanel = this;
    }

    public JPanel getPanel() {
        return this;
    }

    public void activate() {
        //do nothing
    }

    public void deactivate() {
        //do nothing
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */


    private JFileChooser getJFileChooser() {
        if (jFileChooser == null) {

            jFileChooser = new JFileChooser();

            jFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().endsWith(".txt") ||
                            file.isDirectory();
                }

                public String getDescription() {
                    return "Только .txt";
                }
            });


            //jFileChooser.setDialogTitle("Выбор файла с решением");


        }
        return jFileChooser;
    }

    private void loadFile() {
        int state = getJFileChooser().showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File f = getJFileChooser().getSelectedFile();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));

                textArea1.read(br, null);
                textArea1.setEnabled(true);
                br.close();
                //setTitle(title);

                // getJTextPane().setText(s);
                //statementTextPane.setText(s);


                Listener();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void saveFile() {
        String resName = "";
        File chto;
        File folder = env.getProblemFolder();

        int state = getJFileChooser().showSaveDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File f = getJFileChooser().getSelectedFile();
            try {
                textArea2.setVisible(true);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f + ".txt"));
                textArea2.write(bw);
                bw.close();


            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public void Listener() {
        String s = "";
        s = textArea1.getText();
        //s="42";

        HashMap<String, String> res = new HashMap<String, String>();
        res.put("answer", s);


        try {

            final HashMap<String, String> ans = env.submitSolution(res);


            if (ans.get("result").equals("yes"))
                JOptionPane.showMessageDialog(null, "Вы дали правильный ответ!");
            else if (ans.get("result").equals("no"))
                JOptionPane.showMessageDialog(null, "Вы дали неправильный ответ!!");
            else
                JOptionPane.showMessageDialog(null, "Ответ сервера: " + ans.get("result"));
        }
        catch (GeneralRequestFailureException e1) {
            JOptionPane.showMessageDialog(null, "Не удалось связаться с сервером. Ошибка: " + e1.getMessage());
        }

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        pluginPanel.setLayout(new GridBagLayout());
        ButtonVh = new JButton();
        ButtonVh.setHorizontalAlignment(0);
        ButtonVh.setLabel("Сохранить входные данные");
        ButtonVh.setText("Сохранить входные данные");
        ButtonVh.setVerticalAlignment(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 5);
        pluginPanel.add(ButtonVh, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 9;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 3, 0, 2);
        pluginPanel.add(scrollPane1, gbc);
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-10053121)), "Текст задания", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        statementTextPane = new JTextPane();
        statementTextPane.setContentType("text/html");
        statementTextPane.setEditable(false);
        scrollPane1.setViewportView(statementTextPane);
        ButtonSubmit = new JButton();
        ButtonSubmit.setText("Отправить решение");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 0);
        pluginPanel.add(ButtonSubmit, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setMaximumSize(new Dimension(150, 32767));
        scrollPane2.setMinimumSize(new Dimension(150, 150));
        scrollPane2.setPreferredSize(new Dimension(100, 150));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        pluginPanel.add(scrollPane2, gbc);
        scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-10053121)), "Входные данные"));
        textArea2 = new JTextArea();
        textArea2.setEditable(false);
        scrollPane2.setViewportView(textArea2);
        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setEnabled(false);
        scrollPane3.setHorizontalScrollBarPolicy(30);
        scrollPane3.setMaximumSize(new Dimension(32767, 200));
        scrollPane3.setMinimumSize(new Dimension(200, 150));
        scrollPane3.setPreferredSize(new Dimension(200, 150));
        scrollPane3.setVerifyInputWhenFocusTarget(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        pluginPanel.add(scrollPane3, gbc);
        scrollPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-13395457)), "Выходные данные"));
        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        textArea1.setEnabled(false);
        textArea1.setText("");
        textArea1.setWrapStyleWord(false);
        scrollPane3.setViewportView(textArea1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pluginPanel;
    }
}