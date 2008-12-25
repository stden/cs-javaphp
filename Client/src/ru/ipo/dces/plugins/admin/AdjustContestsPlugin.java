package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.*;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;
import ru.ipo.dces.plugins.admin.beans.ProblemsBean;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private JButton deleteButton;
    private JButton downButton;
    private JButton upButton;
    private JButton previewButton;
    private JButton applyButton;
    private JLabel infoMessageLabel;
    private JSeparator contestsSeparator;
    private JButton refreshContestsButton;

    private AdjustContestsPluginBean initialBean = new AdjustContestsPluginBean();
    private AdjustContestsPluginBean updatedBean = new AdjustContestsPluginBean();
    private static final int BUFFER = 4096;

    private DefaultListModel problemsListModel = new DefaultListModel();
    private DefaultListModel contestsListModel = new DefaultListModel();
    private JFileChooser chooseFileDialog = new JFileChooser();

    /**
     * ������������� plugin'�
     *
     * @param env plugin environment
     */
    public AdjustContestsPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();
        env.setTitle("��������� �������");
        contestsList.setModel(contestsListModel);

        contestsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ContestsListBean selected = (ContestsListBean) contestsList.getSelectedValue();

                if (selected == null)
                    return;

                fillDaFormWithData(selected.getDescription().contestID);
            }
        });

        ownRegistrationRB.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updatedBean.setIsByAdmin(ownRegistrationRB.isSelected() ? ContestDescription.RegistrationType.Self : ContestDescription.RegistrationType.ByAdmins);
            }
        });

        addDocumentListener(contestName.getDocument());
        addDocumentListener(contestDescription.getDocument());
        addDocumentListener(beginDate.getDocument());
        addDocumentListener(beginTime.getDocument());
        addDocumentListener(endDate.getDocument());
        addDocumentListener(endTime.getDocument());
        addDocumentListener(clientPlugin.getDocument());
        addDocumentListener(serverPlugin.getDocument());
        addDocumentListener(problemAnswer.getDocument());
        addDocumentListener(problemStatement.getDocument());
        addDocumentListener(problemName.getDocument());

        problemsList.setModel(problemsListModel);
        problemsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ProblemsBean pb = (ProblemsBean) problemsList.getSelectedValue();

                updatedBean.setUpdateAllowed(false);
                if (pb == null) {
                    clientPlugin.setText("");
                    serverPlugin.setText("");
                    problemAnswer.setText("");
                    problemStatement.setText("");
                    problemName.setText("");
                } else {
                    clientPlugin.setText(pb.getDescription().clientPluginAlias);
                    serverPlugin.setText(pb.getDescription().serverPluginAlias);
                    problemName.setText(pb.getDescription().name);
                    //TODO: save humane name or get files from server if name was not set
                    problemAnswer.setText("��������� ������");
                    problemStatement.setText("��������� ������");
                }
                updatedBean.setUpdateAllowed(true);
            }
        });
        upButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = problemsList.getSelectedIndex();

                if (i == 0 || i == -1)
                    return;

                ProblemsBean pb1 = (ProblemsBean) problemsListModel.getElementAt(i - 1);
                ProblemsBean pb2 = (ProblemsBean) problemsListModel.getElementAt(i);

                problemsListModel.setElementAt(pb1, i);
                problemsListModel.setElementAt(pb2, i - 1);

                ProblemDescription[] problemDescriptions = updatedBean.getProblemDescriptions();
                ProblemDescription temp = problemDescriptions[i];
                problemDescriptions[i] = problemDescriptions[i - 1];
                problemDescriptions[i - 1] = temp;
            }
        });

        downButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = problemsList.getSelectedIndex();

                if (i >= problemsListModel.size() - 1 || i == -1)
                    return;

                ProblemsBean pb1 = (ProblemsBean) problemsListModel.getElementAt(i);
                ProblemsBean pb2 = (ProblemsBean) problemsListModel.getElementAt(i + 1);

                problemsListModel.setElementAt(pb1, i + 1);
                problemsListModel.setElementAt(pb2, i);

                ProblemDescription[] problemDescriptions = updatedBean.getProblemDescriptions();
                ProblemDescription temp = problemDescriptions[i];
                problemDescriptions[i] = problemDescriptions[i + 1];
                problemDescriptions[i + 1] = temp;
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProblemDescription pb = new ProblemDescription();

                pb.id = -1;
                pb.name = "����� ������";
                pb.serverPluginAlias = "";
                pb.clientPluginAlias = "";
                pb.statementData = null;
                pb.answerData = null;


                ProblemDescription[] pds = updatedBean.getProblemDescriptions();

                ArrayList<ProblemDescription> l = new ArrayList<ProblemDescription>();
                l.addAll(Arrays.asList(pds));
                l.add(pb);
                updatedBean.setProblemDescriptions(l.toArray(new ProblemDescription[l.size()]));

                problemsListModel.addElement(new ProblemsBean(pb));
                problemsList.setSelectedIndex(problemsListModel.size() - 1);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int i = problemsList.getSelectedIndex();

                if (i == -1)
                    return;

                ProblemDescription[] pds = updatedBean.getProblemDescriptions();

                ArrayList<ProblemDescription> l = new ArrayList<ProblemDescription>();
                l.addAll(Arrays.asList(pds));
                l.remove(i);

                updatedBean.setProblemDescriptions(l.toArray(new ProblemDescription[l.size()]));

                problemsListModel.remove(i);
                problemsList.setSelectedIndex(-1);
            }
        });
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendDaChangedContestToServa();
            }
        });
        changeStatementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File selFile = chooseFileOrFolder();
                if (selFile == null) return;

                problemStatement.setText(selFile.getAbsolutePath());
            }
        });
        changeAnswerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File selFile = chooseFileOrFolder();
                if (selFile == null) return;

                problemAnswer.setText(selFile.getAbsolutePath());
            }
        });
        previewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO process locally stored problem instead of server one
                int i = problemsList.getSelectedIndex();
                if (i == -1) return;

                //get problem id
                int problemID = updatedBean.getProblemDescriptions()[i].id;

                //get contest id
                int contestID;
                //if super admin
                if (Controller.getUserType() == UserDescription.UserType.SuperAdmin) {
                    final ContestsListBean bean = (ContestsListBean) contestsList.getSelectedValue();
                    if (bean == null) return;
                    contestID = bean.getDescription().contestID;
                } else //if contest admin
                    contestID = Controller.getContestID();

                Controller.debugProblem(problemID, contestID);
            }
        });
        refreshContestsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contestsListModel.clear();

                final ContestDescription[] contestDescriptions = Controller.getAvailableContests();

                for (ContestDescription cd : contestDescriptions)
                    contestsListModel.addElement(new ContestsListBean(cd));

                contestsList.setSelectedIndex(-1);
            }
        });
    }


    private File chooseFileOrFolder() {
        if (chooseFileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return chooseFileDialog.getSelectedFile();
        else
            return null;
    }

    private void refreshContestsList() {
        contestsListModel.clear();
        ContestDescription[] contestDescriptions = Controller.getAvailableContests();

        for (ContestDescription description : contestDescriptions)
            contestsListModel.addElement(new ContestsListBean(description));
    }

    private void createUIComponents() {
        drawPanel = this;
    }

    private void fillDaFormWithData(int contestID) {

        GetContestDataResponse gcdr = Controller.getContestData(contestID);

        //TODO handle as an error
        if (gcdr == null) return;

        ContestDescription cd = gcdr.contest;

        initialBean.setContestDescription(cd.description);
        initialBean.setContestName(cd.name);
        initialBean.setBeginDateTime(cd.start);
        initialBean.setEndDateTime(cd.finish);
        initialBean.setIsByAdmin(cd.registrationType);
        initialBean.setContestID(cd.contestID);

        ProblemDescription[] initialPDCopy = new ProblemDescription[gcdr.problems.length];
        for (int i = 0; i < initialPDCopy.length; i++)
            initialPDCopy[i] = cloneProblemDescription(gcdr.problems[i]);
        initialBean.setProblemDescriptions(initialPDCopy);

        updatedBean.setContestDescription(cd.description);
        updatedBean.setContestName(cd.name);
        updatedBean.setBeginDateTime(cd.start);
        updatedBean.setEndDateTime(cd.finish);
        updatedBean.setIsByAdmin(cd.registrationType);
        updatedBean.setContestID(cd.contestID);

        ProblemDescription[] updatedPDCopy = new ProblemDescription[gcdr.problems.length];
        for (int i = 0; i < updatedPDCopy.length; i++)
            updatedPDCopy[i] = cloneProblemDescription(gcdr.problems[i]);
        updatedBean.setProblemDescriptions(updatedPDCopy);

        updatedBean.setUpdateAllowed(false);
        contestName.setText(cd.name);
        contestDescription.setText(cd.description);
        beginDate.setText(new SimpleDateFormat("dd.MM.yy").format(cd.start));
        beginTime.setText(new SimpleDateFormat("HH:mm").format(cd.start));
        endDate.setText(new SimpleDateFormat("dd.MM.yy").format(cd.finish));
        endTime.setText(new SimpleDateFormat("HH:mm").format(cd.finish));

        problemsListModel.clear();

        for (ProblemDescription problem : updatedPDCopy) {
            problemsListModel.addElement(new ProblemsBean(problem));
        }

        problemsList.setSelectedIndex(-1);

        clientPlugin.setText("");
        serverPlugin.setText("");
        problemAnswer.setText("");
        problemStatement.setText("");
        problemName.setText("");

        updatedBean.setUpdateAllowed(true);
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
            JOptionPane.showMessageDialog(null, "��������� ������ �������, ������� ������", "�������� ��������", JOptionPane.INFORMATION_MESSAGE);
            //fireNotificationMessage(infoMessageLabel, "��������� ������ �������", NotificationType.Confirm);
        else
            JOptionPane.showMessageDialog(null, "�� ������� ���������� ���������", "�������� ��������", JOptionPane.ERROR_MESSAGE);
        //fireNotificationMessage(infoMessageLabel, "�� ������� ���������� ���������", NotificationType.Error);

        fillDaFormWithData(cd.contestID);
    }

    private void addDocumentListener(Document d) {
        d.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                doSmth(e);
            }

            public void removeUpdate(DocumentEvent e) {
                doSmth(e);
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void doSmth(DocumentEvent e) {
                if (!updatedBean.isUpdateAllowed())
                    return;

                if (e.getDocument() == contestDescription.getDocument()) {
                    updatedBean.setContestDescription(contestDescription.getText());
                } else if (e.getDocument() == contestName.getDocument()) {
                    updatedBean.setContestName(contestName.getText());
                } else if (e.getDocument() == beginDate.getDocument()) {
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
                        fireNotificationMessage(infoMessageLabel, "������� ���������� ���� (��.��.��) � ����� (��:��)", NotificationType.Error);
                    }
                } else if (e.getDocument() == beginTime.getDocument()) {
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
                        fireNotificationMessage(infoMessageLabel, "������� ���������� ���� (��.��.��) � ����� (��:��)", NotificationType.Error);
                    }

                } else if (e.getDocument() == endDate.getDocument()) {
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
                        fireNotificationMessage(infoMessageLabel, "������� ���������� ���� (��.��.��) � ����� (��:��)", NotificationType.Error);
                    }
                } else if (e.getDocument() == endTime.getDocument()) {
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
                        fireNotificationMessage(infoMessageLabel, "������� ���������� ���� (��.��.��) � ����� (��:��)", NotificationType.Error);
                    }
                } else if (e.getDocument() == problemStatement.getDocument() || e.getDocument() == problemAnswer.getDocument()) {

                    ProblemsBean pb = (ProblemsBean) problemsList.getSelectedValue();

                    if (pb == null)
                        return;

                    File res = new File(e.getDocument() == problemAnswer.getDocument() ? problemAnswer.getText() : problemStatement.getText());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ZipOutputStream zipOS = new ZipOutputStream(baos);

                    try {
                        if (res.isFile()) {
                            doZipFile(zipOS, "", res);
                        } else
                            archiveCatalog(res, zipOS, "");
                    } catch (IOException e1) {
                        fireNotificationMessage(infoMessageLabel, "�� ������� ���������� ������� ������", NotificationType.Error);
                    }

                    try {
                        zipOS.close();
                    } catch (IOException e1) {
                        if (e.getDocument() == problemAnswer.getDocument())
                            pb.getDescription().answerData = null;
                        else
                            pb.getDescription().statementData = null;
                    }

                    if (e.getDocument() == problemAnswer.getDocument())
                        pb.getDescription().answerData = baos.toByteArray();
                    else
                        pb.getDescription().statementData = baos.toByteArray();
                } else if (e.getDocument() == problemName.getDocument()) {
                    int i = problemsList.getSelectedIndex();
                    if (i == -1) return;
                    //ProblemsBean pb = (ProblemsBean) problemsList.getSselectedValue();
                    ProblemsBean pb = (ProblemsBean) problemsListModel.getElementAt(i);

                    if (pb == null)
                        return;

                    pb.getDescription().name = problemName.getText();
                    //refresh list by hack
                    problemsListModel.setElementAt(pb, i);
                } else if (e.getDocument() == clientPlugin.getDocument()) {
                    ProblemsBean pb = (ProblemsBean) problemsList.getSelectedValue();

                    if (pb != null)
                        pb.getDescription().clientPluginAlias = clientPlugin.getText();
                } else if (e.getDocument() == serverPlugin.getDocument()) {
                    ProblemsBean pb = (ProblemsBean) problemsList.getSelectedValue();

                    if (pb != null)
                        pb.getDescription().serverPluginAlias = serverPlugin.getText();
                }
            }
        });
    }

    private void setColumn(FormLayout form, int colNo, int xSize, double grow) {
        form.setColumnSpec(colNo, new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(xSize), grow));
    }

    public void activate() {

        FormLayout cur = (FormLayout) drawPanel.getLayout();

        if (Controller.getUserType() == UserDescription.UserType.SuperAdmin) {

            refreshContestsList();
            setColumn(cur, 3, 122, 0);
            setColumn(cur, 4, 4, 1);
            setColumn(cur, 5, 2, 0);
            setColumn(cur, 6, 4, 1);

        } else if (Controller.getUserType() == UserDescription.UserType.ContestAdmin) {

            setColumn(cur, 3, 0, 0);
            setColumn(cur, 4, 0, 0);
            setColumn(cur, 5, 0, 0);
            setColumn(cur, 6, 0, 0);

            fillDaFormWithData(Controller.getContestID());

        } else
            throw new IllegalArgumentException();

    }

    private void archiveCatalog(File dir, ZipOutputStream zipOS, String zipPath) throws IOException {
        String[] files = dir.list();

        if (files == null) return;

        for (String fileName : files) {

            File file = new File(dir.getAbsolutePath() + "/" + fileName);

            if (file.isDirectory()) {
                archiveCatalog(file, zipOS, zipPath + fileName + "/");
            } else {
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

        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zipOS.write(data, 0, count);
        }

        zipOS.closeEntry();

        bis.close();
    }

    private ProblemDescription cloneProblemDescription(ProblemDescription pd) {
        ProblemDescription res = new ProblemDescription();

        res.answerData = null;
        res.clientPluginAlias = pd.clientPluginAlias;
        res.id = pd.id;
        res.name = pd.name;
        res.serverPluginAlias = pd.serverPluginAlias;
        res.statement = null;
        res.statementData = null;

        return res;
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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:m:grow,left:4dlu:noGrow,fill:1dlu:noGrow,left:4dlu:noGrow,fill:92dlu:grow,left:4dlu:noGrow,fill:72dlu:grow,left:4dlu:noGrow,fill:m:noGrow,left:4dlu:noGrow,left:30dlu:grow,fill:max(d;4px):noGrow,fill:62dlu:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:60dlu:grow,top:4dlu:noGrow,center:17dlu:noGrow,top:4dlu:noGrow,center:1px:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:28px:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:0dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:5dlu:noGrow,center:16dlu:noGrow,top:5dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow"));
        contestsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        contestsList.setModel(defaultListModel1);
        contestsList.setSelectionMode(0);
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestsList, cc.xywh(3, 7, 1, 36, CellConstraints.DEFAULT, CellConstraints.FILL));
        contestName = new JTextField();
        drawPanel.add(contestName, cc.xyw(9, 5, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label1 = new JLabel();
        label1.setText("�������� ��������");
        drawPanel.add(label1, cc.xy(7, 5));
        final JLabel label2 = new JLabel();
        label2.setText("���� � ����� ������");
        drawPanel.add(label2, cc.xy(7, 7));
        beginDate = new JFormattedTextField();
        drawPanel.add(beginDate, cc.xyw(9, 7, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label3 = new JLabel();
        label3.setText("���� � ����� ���������");
        drawPanel.add(label3, cc.xy(7, 9));
        endDate = new JFormattedTextField();
        drawPanel.add(endDate, cc.xyw(9, 9, 3, CellConstraints.FILL, CellConstraints.FILL));
        endTime = new JFormattedTextField();
        drawPanel.add(endTime, cc.xyw(13, 9, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label4 = new JLabel();
        label4.setText("�������� ��������");
        drawPanel.add(label4, cc.xy(7, 11));
        contestDescription = new JTextArea();
        contestDescription.setLineWrap(true);
        contestDescription.setText("");
        contestDescription.setWrapStyleWord(true);
        drawPanel.add(contestDescription, cc.xyw(9, 11, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("��� �����������");
        drawPanel.add(label5, cc.xy(7, 13));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("��������������");
        drawPanel.add(ownRegistrationRB, cc.xyw(9, 13, 3));
        contestsSeparator = new JSeparator();
        contestsSeparator.setOrientation(1);
        drawPanel.add(contestsSeparator, cc.xywh(5, 3, 1, 40, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator1 = new JSeparator();
        drawPanel.add(separator1, cc.xyw(7, 15, 9, CellConstraints.FILL, CellConstraints.FILL));
        beginTime = new JFormattedTextField();
        drawPanel.add(beginTime, cc.xyw(13, 7, 3, CellConstraints.FILL, CellConstraints.FILL));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("���������������");
        drawPanel.add(administratorRegistrationRB, cc.xyw(13, 13, 3));
        final JLabel label6 = new JLabel();
        label6.setText("�����");
        drawPanel.add(label6, cc.xy(7, 40));
        problemAnswer = new JTextField();
        problemAnswer.setEditable(false);
        problemAnswer.setEnabled(true);
        drawPanel.add(problemAnswer, cc.xyw(9, 40, 5, CellConstraints.FILL, CellConstraints.FILL));
        changeAnswerButton = new JButton();
        changeAnswerButton.setText("...");
        drawPanel.add(changeAnswerButton, cc.xy(15, 40));
        final JLabel label7 = new JLabel();
        label7.setText("�������");
        drawPanel.add(label7, cc.xy(7, 38));
        problemStatement = new JTextField();
        problemStatement.setEditable(false);
        problemStatement.setEnabled(true);
        problemStatement.setText("");
        drawPanel.add(problemStatement, cc.xyw(9, 38, 5, CellConstraints.FILL, CellConstraints.FILL));
        changeStatementButton = new JButton();
        changeStatementButton.setText("...");
        drawPanel.add(changeStatementButton, cc.xy(15, 38, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        label8.setText("��������� ������");
        drawPanel.add(label8, cc.xy(7, 36));
        serverPlugin = new JTextField();
        drawPanel.add(serverPlugin, cc.xyw(9, 36, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label9 = new JLabel();
        label9.setText("���������� ������");
        drawPanel.add(label9, cc.xy(7, 34));
        clientPlugin = new JTextField();
        clientPlugin.setText("���������� ������");
        drawPanel.add(clientPlugin, cc.xyw(9, 34, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label10 = new JLabel();
        label10.setText("��� ������");
        drawPanel.add(label10, cc.xy(7, 32));
        problemName = new JTextField();
        problemName.setText("��� ������");
        drawPanel.add(problemName, cc.xyw(9, 32, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label11 = new JLabel();
        label11.setText("������");
        drawPanel.add(label11, cc.xywh(7, 17, 1, 11));
        problemsList = new JList();
        problemsList.setSelectionMode(0);
        drawPanel.add(problemsList, cc.xywh(9, 17, 5, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        upButton = new JButton();
        upButton.setText("�����");
        upButton.setVerticalAlignment(0);
        drawPanel.add(upButton, cc.xy(15, 17, CellConstraints.DEFAULT, CellConstraints.CENTER));
        downButton = new JButton();
        downButton.setText("����");
        drawPanel.add(downButton, cc.xy(15, 19, CellConstraints.DEFAULT, CellConstraints.CENTER));
        addButton = new JButton();
        addButton.setText("��������");
        drawPanel.add(addButton, cc.xy(15, 21));
        applyButton = new JButton();
        applyButton.setText("���������");
        drawPanel.add(applyButton, cc.xyw(7, 42, 9));
        final JLabel label12 = new JLabel();
        label12.setText("��������� ��������");
        label12.setVerticalAlignment(0);
        drawPanel.add(label12, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        infoMessageLabel = new JLabel();
        infoMessageLabel.setForeground(new Color(-16777216));
        infoMessageLabel.setText("");
        drawPanel.add(infoMessageLabel, cc.xyw(7, 3, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        deleteButton = new JButton();
        deleteButton.setText("�������");
        drawPanel.add(deleteButton, cc.xy(15, 23));
        previewButton = new JButton();
        previewButton.setText("����������");
        drawPanel.add(previewButton, cc.xy(15, 27));
        refreshContestsButton = new JButton();
        refreshContestsButton.setText("�������� ������");
        drawPanel.add(refreshContestsButton, cc.xy(3, 5));
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