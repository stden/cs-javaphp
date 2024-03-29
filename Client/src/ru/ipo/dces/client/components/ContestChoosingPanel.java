package ru.ipo.dces.client.components;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;
import ru.ipo.dces.client.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 11.04.2009
 * Time: 18:59:58
 */
public class ContestChoosingPanel extends JPanel {

    private ContestDescription contest = null;

    private boolean popup = false;
    private boolean showLabel = true;
    private int beforeLabelGap = 20;

    private final JList selectContestList;
    private final JLabel selectedContestLabel;
    private final JButton selectContestButton;
    private final JButton refreshContestListButton;
    private final JLabel titleLabel;
    private final JTextField searchContestField;

    private HashSet<ActionListener> contestChangedListeners = new HashSet<ActionListener>();

    private ChooseContestDialog chooseContestDialog = new ChooseContestDialog();

    private boolean noContestsAvailable = true;

    public ContestChoosingPanel() {
        selectedContestLabel = new JLabel();
        selectedContestLabel.setFont(selectedContestLabel.getFont().deriveFont(Font.BOLD));
        selectContestButton = new JButton("Выбрать");
        refreshContestListButton = new JButton("Обновить");
        selectContestList = new JList();
        titleLabel = new JLabel("Доступные соревнования");
        searchContestField = new JTextField();

        selectContestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute();
            }
        });

        refreshContestListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshContestList();
            }
        });

        selectContestList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (noContestsAvailable) return;

                ContestsListBean bean = (ContestsListBean) selectContestList.getSelectedValue();
                if (bean == null) return;
                setContest(bean.getDescription());

                //searchContestField.setText(bean.toString());
            }
        });

        searchContestField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                doSearchContest();
            }

            public void removeUpdate(DocumentEvent e) {
                doSearchContest();
            }

            public void changedUpdate(DocumentEvent e) {
                //do nothing
            }
        });

        setInterface();

    }

    public void execute() {
        if (isPopup()) {
            ContestDescription contest = chooseContestDialog.run();
            if (contest != null)
                setContest(contest);
        }
    }

    private void doSearchContest() {
        final String inp = searchContestField.getText();

        final ListModel listModel = selectContestList.getModel();
        final int size = listModel.getSize();
        for (int i = 0; i < size; i++) {
            final String name = listModel.getElementAt(i).toString();
            if (name.startsWith(inp)) {
                //select element in list
                selectContestList.setSelectedIndex(i);
                selectContestList.ensureIndexIsVisible(i);

                break;
            }
        }

        selectContestList.setSelectedIndex(-1);
    }

    public void refreshContestList() {
        ContestsListBean[] contestDescriptions = getContestList();

        noContestsAvailable = (contestDescriptions.length == 0);

        ListModel listModel;
        ListCellRenderer cellRenderer;

        if (noContestsAvailable) {
            listModel = new AbstractListModel() {
                public int getSize() {
                    return 1;
                }

                public Object getElementAt(int index) {
                    return "Нет доступных соревнований";
                }
            };

            DefaultListCellRenderer dcr = new DefaultListCellRenderer();
            Font font = dcr.getFont();
            dcr.setFont(font.deriveFont(Font.ITALIC));
            dcr.setForeground(Color.GRAY);
            cellRenderer = dcr;

            AutoCompleteDecorator.decorate(searchContestField, Arrays.asList(), true);

        } else {
            listModel = new DefaultComboBoxModel(contestDescriptions);
            cellRenderer = new DefaultListCellRenderer();
            AutoCompleteDecorator.decorate(searchContestField, Arrays.asList(contestDescriptions), true);
        }

        selectContestList.setEnabled(!noContestsAvailable);
        selectContestList.setCellRenderer(cellRenderer);
        selectContestList.setModel(listModel);
        selectContestList.setSelectedIndex(-1);
    }

    private void setInterface() {
        removeAll();

        if (popup) {

            if (showLabel) {
                setLayout(new TableLayout(new double[][]{
                        {beforeLabelGap, TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}, {TableLayout.FILL}
                }));

                add(new JLabel("Соревнование "), "1, 0");
                add(selectedContestLabel, "2, 0");
                add(selectContestButton, "3, 0");
            } else {
                setLayout(new GridLayout(1, 1));
                add(selectContestButton);
            }

        } else {
            setLayout(new TableLayout(new double[][]{
                    {TableLayout.FILL, TableLayout.PREFERRED}, {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}
            }));

            add(titleLabel, "0, 0");
            add(refreshContestListButton, "1, 0");
            add(new JScrollPane(selectContestList), "0, 1, 1, 1");
            add(searchContestField, "0, 2, 1, 2");
        }

        validate();
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
        setInterface();
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        setInterface();
    }

    public ContestDescription getContest() {
        return contest;
    }

    //TODO move selection in list if the list is visible

    public void setContest(ContestDescription contest) {
        this.contest = contest;
        String contestDisplayName = contest == null ? "" : contest.name;
        selectedContestLabel.setText(contestDisplayName);

        for (ActionListener contestChangedListener : contestChangedListeners)
            contestChangedListener.actionPerformed(new ActionEvent(this, 0, null));
    }

    public int getBeforeLabelGap() {
        return beforeLabelGap;
    }

    public void setBeforeLabelGap(int beforeLabelGap) {
        this.beforeLabelGap = beforeLabelGap;
        setInterface();
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    //action listeners

    public void addContestChangedActionListener(ActionListener l) {
        contestChangedListeners.add(l);
    }

    public void removeContestChangedActionListener(ActionListener l) {
        contestChangedListeners.remove(l);
    }

    public static ContestsListBean[] getContestList() {
        ContestDescription[] contestDescriptions = Controller.getAvailableContests();
        ContestsListBean[] result = new ContestsListBean[contestDescriptions.length];
        for (int i = 0; i < contestDescriptions.length; i++)
            result[i] = new ContestsListBean(contestDescriptions[i]);

        return result;
    }

}