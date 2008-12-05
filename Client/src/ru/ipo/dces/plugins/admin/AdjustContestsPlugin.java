package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.AdjustContestRequest;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.GetContestDataResponse;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AdjustContestsPlugin extends NotificationPlugin {
    private JPanel drawPanel;
    private JList contestsList;
    private JTextField contestName;
    private JFormattedTextField beginTime;
    private JFormattedTextField beginDate;
    private JFormattedTextField endDate;
    private JFormattedTextField endTime;
    private JTextArea contestDescription;
    private JRadioButton ownRegistrationRB;
    private JRadioButton administratorRegistrationRB;
    private JTextField problemName;
    private JTextField problemStatement;
    private JTextField clientPlugin;
    private JButton changeStatementButton;
    private JTextField serverPlugin;
    private JTextField problemAnswer;
    private JButton changeAnswerButton;
    private JList problemsList;
    private JButton addButton;
    private JButton changeButton;
    private JButton deleteButton;
    private JButton downButton;
    private JButton upButton;
    private JButton previewButton;
    private JButton applyButton;
    private JLabel infoMessageLabel;

    private AdjustContestsPluginBean initialBean = new AdjustContestsPluginBean();
    private AdjustContestsPluginBean updatedBean = new AdjustContestsPluginBean();
    private static final int BUFFER = 4096;

    /**
     * Инициализация plugin'а
     *
     * @param env plugin environment
     */
    public AdjustContestsPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();
        env.setTitle("Настроить контест");
        contestsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //TODO: Refactor
                fillDaFormWithData(42);
            }
        });
    }

    private void createUIComponents() {
        drawPanel = this;
    }

    private void fillDaFormWithData(int contestID) {

        GetContestDataResponse gcdr = Controller.getContestData(contestID);

        ContestDescription cd = gcdr.contest;

        initialBean.setContestDescription(cd.description);
        initialBean.setContestName(cd.name);
        initialBean.setBeginDateTime(cd.start);
        initialBean.setEndDateTime(cd.finish);
        initialBean.setIsByAdmin(cd.registrationType);
        initialBean.setContestID(cd.contestID);

        initialBean.setProblemDescriptions(gcdr.problems);

        //TODO fill updated bean with the same data (as in initial bean)
        //TODO fill form controls with the values
    }

    //private void sendDaChangedContestToServaCauseMyConnectIsGettin...() {
    private void sendDaChangedContestToServa() {
        AdjustContestRequest acr = new AdjustContestRequest();

        ContestDescription cd = new ContestDescription();

        cd.compulsory = null;
        cd.data = null;
        cd.description = initialBean.compareContestDescriptions(updatedBean.getContestDescription());
        cd.start = initialBean.compareBeginDateTime(updatedBean.getBeginDateTime());
        cd.finish = initialBean.compareEndDateTime(updatedBean.getEndDateTime());
        cd.name = initialBean.compareContestName(updatedBean.getContestName());
        cd.registrationType = initialBean.compareIsByAdmin(updatedBean.getIsByAdmin());
        cd.contestID = updatedBean.getContestID();

        acr.contest = cd;
        acr.problems = initialBean.compareProblemDescriptions(updatedBean.getProblemDescriptions());

        boolean succeeded = Controller.adjustContestData(acr);

        //TODO: inform user with problem details and ways to work it around
        if (succeeded)
            fireNotificationMessage(infoMessageLabel, "Изменения прошли успешно", NotificationType.Confirm);
        else
            fireNotificationMessage(infoMessageLabel, "Не удалось произвести изменения", NotificationType.Error);

        fillDaFormWithData(cd.contestID);
    }

    private void AddDocumentListener(Document d)
    {
        d.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                doSmth(e);
            }

            public void removeUpdate(DocumentEvent e) {
                doSmth(e);
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void doSmth(DocumentEvent e)
            {
                if (e.getDocument() == contestDescription.getDocument()) {
                    updatedBean.setContestDescription(contestDescription.getText());
                }
                else if(e.getDocument() == contestName.getDocument()) {
                    updatedBean.setContestName(contestName.getText());
                }
                else if(e.getDocument() == beginDate.getDocument()) {
                    try {
                        GregorianCalendar c1 = new GregorianCalendar();
                        GregorianCalendar c2 = new GregorianCalendar();

                        c1.setTime(updatedBean.getBeginDateTime());
                        c2.setTime(new SimpleDateFormat("dd.MM.yy").parse(beginDate.getText()));

                        c1.set(
                                c2.get(GregorianCalendar.YEAR),
                                c2.get(GregorianCalendar.MONTH),
                                c2.get(GregorianCalendar.DAY_OF_MONTH),
                                c1.get(GregorianCalendar.HOUR_OF_DAY),
                                c1.get(GregorianCalendar.MINUTE),
                                c1.get(GregorianCalendar.SECOND)
                                );
                        updatedBean.setBeginDateTime(c1.getTime());        

                    } catch (ParseException e1) {
                        //TODO: Handle a better validation here
                        fireNotificationMessage(infoMessageLabel, "Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    }
                }
                else if(e.getDocument() == beginTime.getDocument()) {
                    try {
                        GregorianCalendar c1 = new GregorianCalendar();
                        GregorianCalendar c2 = new GregorianCalendar();

                        c1.setTime(updatedBean.getBeginDateTime());
                        c2.setTime(new SimpleDateFormat("HH:mm").parse(beginTime.getText()));

                        c1.set(
                                c1.get(GregorianCalendar.YEAR),
                                c1.get(GregorianCalendar.MONTH),
                                c1.get(GregorianCalendar.DAY_OF_MONTH),
                                c2.get(GregorianCalendar.HOUR_OF_DAY),
                                c2.get(GregorianCalendar.MINUTE),
                                c1.get(GregorianCalendar.SECOND)
                                );
                        updatedBean.setBeginDateTime(c1.getTime());

                    } catch (ParseException e1) {
                        //TODO: Handle a better validation here
                        fireNotificationMessage(infoMessageLabel, "Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    }

                }
                else if(e.getDocument() == endDate.getDocument()) {
                    try {
                        GregorianCalendar c1 = new GregorianCalendar();
                        GregorianCalendar c2 = new GregorianCalendar();

                        c1.setTime(updatedBean.getEndDateTime());
                        c2.setTime(new SimpleDateFormat("dd.MM.yy").parse(endDate.getText()));

                        c1.set(
                                c2.get(GregorianCalendar.YEAR),
                                c2.get(GregorianCalendar.MONTH),
                                c2.get(GregorianCalendar.DAY_OF_MONTH),
                                c1.get(GregorianCalendar.HOUR_OF_DAY),
                                c1.get(GregorianCalendar.MINUTE),
                                c1.get(GregorianCalendar.SECOND)
                                );
                        updatedBean.setEndDateTime(c1.getTime());

                    } catch (ParseException e1) {
                        //TODO: Handle a better validation here
                        fireNotificationMessage(infoMessageLabel, "Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    }
                }
                else if(e.getDocument() == endTime.getDocument()) {
                    try {
                        GregorianCalendar c1 = new GregorianCalendar();
                        GregorianCalendar c2 = new GregorianCalendar();

                        c1.setTime(updatedBean.getEndDateTime());
                        c2.setTime(new SimpleDateFormat("HH:mm").parse(endTime.getText()));

                        c1.set(
                                c1.get(GregorianCalendar.YEAR),
                                c1.get(GregorianCalendar.MONTH),
                                c1.get(GregorianCalendar.DAY_OF_MONTH),
                                c2.get(GregorianCalendar.HOUR_OF_DAY),
                                c2.get(GregorianCalendar.MINUTE),
                                c1.get(GregorianCalendar.SECOND)
                                );
                        updatedBean.setEndDateTime(c1.getTime());

                    } catch (ParseException e1) {
                        //TODO: Handle a better validation here
                        fireNotificationMessage(infoMessageLabel, "Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    }
                }
                else if(e.getDocument() == problemStatement.getDocument() || e.getDocument() == problemAnswer.getDocument()) {
                    File res = new File(e.getDocument() == problemAnswer ? problemAnswer.getText() : problemStatement.getText());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ZipOutputStream zipOS = new ZipOutputStream(baos);

                    try {
                        if (res.isFile()) {
                            doZipFile(zipOS, "", res);
                        }
                        else
                            archiveCatalog(res, zipOS, "");
                    } catch (IOException e1) {
                        fireNotificationMessage(infoMessageLabel, "Не удалось запаковать условие задачи", NotificationType.Error);
                    }

                    updatedBean.setProblemStatement(baos.toByteArray());
                }
                else if(e.getDocument() == problemName.getDocument()) {

                }
                else if(e.getDocument() == clientPlugin.getDocument()) {

                }
                else if(e.getDocument() == serverPlugin.getDocument()) {

                }
            }
        });
    }

    private void archiveCatalog(File dir, ZipOutputStream zipOS, String zipPath) throws IOException {
        String[] files = dir.list();

        for (String fileName : files) {

            File file = new File(dir.getAbsolutePath() + "/" + fileName);

            if (file.isDirectory()) {
                archiveCatalog(file, zipOS, zipPath + fileName + "/");
            }
            else{
                doZipFile(zipOS, zipPath, file);
            }
        }
    }

    private void doZipFile(ZipOutputStream zipOS, String zipPath, File file) throws IOException {
        String fileName = file.getName();
        byte[] data = new byte[BUFFER];

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        zipOS.putNextEntry(new ZipEntry(zipPath + fileName));

        int count;

        while((count = bis.read(data,0,BUFFER)) != -1){
            zipOS.write(data, 0, count);
        }

        zipOS.closeEntry();

        bis.close();
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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:122dlu:noGrow,left:4dlu:noGrow,fill:2dlu:noGrow,left:4dlu:noGrow,fill:92dlu:noGrow,left:4dlu:noGrow,fill:72dlu:noGrow,left:4dlu:noGrow,fill:30dlu:noGrow,left:4dlu:noGrow,left:30dlu:noGrow,fill:max(d;4px):noGrow,fill:62dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:60dlu:noGrow,top:4dlu:noGrow,center:17dlu:noGrow,top:4dlu:noGrow,center:1px:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:28px:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:0dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:5dlu:noGrow,center:16dlu:noGrow,top:5dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow"));
        contestsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        contestsList.setModel(defaultListModel1);
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestsList, cc.xywh(3, 4, 1, 39, CellConstraints.DEFAULT, CellConstraints.FILL));
        contestName = new JTextField();
        drawPanel.add(contestName, cc.xyw(9, 5, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label1 = new JLabel();
        label1.setText("Название контеста");
        drawPanel.add(label1, cc.xy(7, 5));
        final JLabel label2 = new JLabel();
        label2.setText("Дата и время начала");
        drawPanel.add(label2, cc.xy(7, 7));
        beginDate = new JFormattedTextField();
        drawPanel.add(beginDate, cc.xyw(9, 7, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label3 = new JLabel();
        label3.setText("Дата и время окончания");
        drawPanel.add(label3, cc.xy(7, 9));
        endDate = new JFormattedTextField();
        drawPanel.add(endDate, cc.xyw(9, 9, 3, CellConstraints.FILL, CellConstraints.FILL));
        endTime = new JFormattedTextField();
        drawPanel.add(endTime, cc.xyw(13, 9, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label4 = new JLabel();
        label4.setText("Описание контеста");
        drawPanel.add(label4, cc.xy(7, 11));
        contestDescription = new JTextArea();
        contestDescription.setLineWrap(true);
        contestDescription.setText("");
        contestDescription.setWrapStyleWord(true);
        drawPanel.add(contestDescription, cc.xyw(9, 11, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("Тип регистрации");
        drawPanel.add(label5, cc.xy(7, 13));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("Самостоятельно");
        drawPanel.add(ownRegistrationRB, cc.xyw(9, 13, 3));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        drawPanel.add(separator1, cc.xywh(5, 3, 1, 40, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xyw(7, 15, 9, CellConstraints.FILL, CellConstraints.FILL));
        beginTime = new JFormattedTextField();
        drawPanel.add(beginTime, cc.xyw(13, 7, 3, CellConstraints.FILL, CellConstraints.FILL));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("Администратором");
        drawPanel.add(administratorRegistrationRB, cc.xyw(13, 13, 3));
        final JLabel label6 = new JLabel();
        label6.setText("Ответ");
        drawPanel.add(label6, cc.xy(7, 40));
        problemAnswer = new JTextField();
        problemAnswer.setEditable(false);
        problemAnswer.setEnabled(true);
        drawPanel.add(problemAnswer, cc.xyw(9, 40, 5, CellConstraints.FILL, CellConstraints.FILL));
        changeAnswerButton = new JButton();
        changeAnswerButton.setText("...");
        drawPanel.add(changeAnswerButton, cc.xy(15, 40));
        final JLabel label7 = new JLabel();
        label7.setText("Условие");
        drawPanel.add(label7, cc.xy(7, 38));
        problemStatement = new JTextField();
        problemStatement.setEditable(false);
        problemStatement.setEnabled(true);
        problemStatement.setText("Условие");
        drawPanel.add(problemStatement, cc.xyw(9, 38, 5, CellConstraints.FILL, CellConstraints.FILL));
        changeStatementButton = new JButton();
        changeStatementButton.setText("...");
        drawPanel.add(changeStatementButton, cc.xy(15, 38, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        label8.setText("Серверный плагин");
        drawPanel.add(label8, cc.xy(7, 36));
        serverPlugin = new JTextField();
        drawPanel.add(serverPlugin, cc.xyw(9, 36, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label9 = new JLabel();
        label9.setText("Клиентский плагин");
        drawPanel.add(label9, cc.xy(7, 34));
        clientPlugin = new JTextField();
        clientPlugin.setText("клиентский плагин");
        drawPanel.add(clientPlugin, cc.xyw(9, 34, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label10 = new JLabel();
        label10.setText("Имя задачи");
        drawPanel.add(label10, cc.xy(7, 32));
        problemName = new JTextField();
        problemName.setText("Имя задачи");
        drawPanel.add(problemName, cc.xyw(9, 32, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label11 = new JLabel();
        label11.setText("Задачи");
        drawPanel.add(label11, cc.xywh(7, 17, 1, 11));
        problemsList = new JList();
        drawPanel.add(problemsList, cc.xywh(9, 17, 5, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        upButton = new JButton();
        upButton.setText("Вверх");
        upButton.setVerticalAlignment(0);
        drawPanel.add(upButton, cc.xy(15, 17, CellConstraints.DEFAULT, CellConstraints.CENTER));
        downButton = new JButton();
        downButton.setText("Вниз");
        drawPanel.add(downButton, cc.xy(15, 19, CellConstraints.DEFAULT, CellConstraints.CENTER));
        addButton = new JButton();
        addButton.setText("Добавить");
        drawPanel.add(addButton, cc.xy(15, 21));
        changeButton = new JButton();
        changeButton.setText("Изменить");
        drawPanel.add(changeButton, cc.xy(15, 23));
        deleteButton = new JButton();
        deleteButton.setText("Удалить");
        drawPanel.add(deleteButton, cc.xy(15, 25));
        previewButton = new JButton();
        previewButton.setText("Посмотреть");
        drawPanel.add(previewButton, cc.xy(15, 27));
        applyButton = new JButton();
        applyButton.setText("Применить");
        drawPanel.add(applyButton, cc.xyw(7, 42, 9));
        final JLabel label12 = new JLabel();
        label12.setText("Доступные контесты");
        label12.setVerticalAlignment(0);
        drawPanel.add(label12, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        infoMessageLabel = new JLabel();
        infoMessageLabel.setForeground(new Color(-52225));
        infoMessageLabel.setText("Внимание! Внимание!");
        drawPanel.add(infoMessageLabel, cc.xyw(7, 3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(ownRegistrationRB);
        buttonGroup.add(administratorRegistrationRB);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return drawPanel;
    }
}
