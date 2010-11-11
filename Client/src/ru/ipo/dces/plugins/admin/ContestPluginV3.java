package ru.ipo.dces.plugins.admin;

import com.l2fprod.common.swing.JButtonBar;
import info.clearthought.layout.TableLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.client.resources.Resources;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.serverbeans.AdjustContestRequestBean;
import ru.ipo.dces.serverbeans.ContestDescriptionBean;
import ru.ipo.dces.serverbeans.ProblemDescriptionBean;
import ru.ipo.structurededitor.StructuredEditor;
import ru.ipo.structurededitor.model.Cell;
import ru.ipo.structurededitor.model.ConstantCell;
import ru.ipo.structurededitor.model.DSLBean;
import ru.ipo.structurededitor.view.EditorRenderer;
import ru.ipo.structurededitor.view.StructuredEditorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 31.07.2009
 * Time: 11:45:45
 */
public class ContestPluginV3 implements Plugin, ActionListener {

    private JPanel mainPanel;
    private ContestChoosingPanel contestChoosingPanel;
    private JButton addContestButton;
    private JButton removeContestButton;
    private JButton undoButton;
    private JButton applyButton;
    private StructuredEditor contestEditor;
    //TODO make problem viewer smarter. It should show problems, debug and download them
    private JButton problemViewer;
    private AdjustContestRequestBean bean;
    private int contestType; //-1 no contest, 0 - just created, 1 - adjusting contest
    private final DSLBean emptyBean;

    public ContestPluginV3(PluginEnvironment environment) {
        emptyBean = new DSLBean() {
            public Cell getLayout() {
                //TODO show contest selection when clicked
                return new ConstantCell("Не выбрано соревнование");
            }
        };
        bean = new AdjustContestRequestBean();
        initInterface();
        environment.setTitle(Localization.getAdminPluginName(this.getClass()));
    }

    private void setContestType(int type) {
        contestType = type;
        setContestToolBarView();
        
        if (contestEditor == null)
            return;

        if (type == -1)
            contestEditor.setEnabled(false);
        else
            contestEditor.setEnabled(true);
    }

    private void setContestToolBarView() {
        addContestButton.setEnabled(Controller.isSuperAdmin());
        removeContestButton.setEnabled(contestType == 1 && Controller.isSuperAdmin());
        applyButton.setEnabled(true/*bean.isModified()*/);
        undoButton.setEnabled(true/*bean.isModified()*/);
    }

    private JButton addButtonToContestToolBar(JButtonBar toolBar, String text, Icon icon) {
        JButton button = new JButton(text, icon);
        toolBar.add(button);

        return button;
    }

    public void initInterface() {

        //contest choosing panel
        contestChoosingPanel = new ContestChoosingPanel();
        contestChoosingPanel.setBeforeLabelGap(0);
        contestChoosingPanel.setPopup(true);
        contestChoosingPanel.addContestChangedActionListener(this);

        //problems viewer
        problemViewer = new JButton("pv");

        //apply and undo buttons
        final Resources res = Resources.getInstance();
        JButtonBar contestToolBar = new JButtonBar(JButtonBar.HORIZONTAL);
        addContestButton = addButtonToContestToolBar(contestToolBar, "Добавить", new ImageIcon(res.getResourceAsByteArray("images/add.gif")));
        removeContestButton = addButtonToContestToolBar(contestToolBar, "Удалить", new ImageIcon(res.getResourceAsByteArray("images/remove.gif")));
        applyButton = addButtonToContestToolBar(contestToolBar, "Применить", new ImageIcon(res.getResourceAsByteArray("images/bigApply.gif")));
        undoButton = addButtonToContestToolBar(contestToolBar, "Отменить", new ImageIcon(res.getResourceAsByteArray("images/cancel.gif")));

        applyButton.addActionListener(this);
        removeContestButton.addActionListener(this);
        undoButton.addActionListener(this);
        addContestButton.addActionListener(this);

        setContestType(-1);

        StructuredEditorModel model = new StructuredEditorModel();
        model.setRootElement(new EditorRenderer(model, bean).getRenderResult());
        contestEditor = new StructuredEditor(model, bean);
        contestEditor.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (contestType == -1)
                    contestChoosingPanel.execute();                
            }
        });
        refreshContestEditor();

        //add all controls to main panel
        mainPanel = new JPanel(new TableLayout(new double[][]{
                {TableLayout.FILL, TableLayout.PREFERRED}, {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL}
        }));
        mainPanel.add(contestChoosingPanel, "0, 0, 1, 0");
        mainPanel.add(contestToolBar, "1, 1");
        mainPanel.add(new JScrollPane(contestEditor), "0, 1, 0, 2");
        mainPanel.add(problemViewer, "1, 2");
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public void activate() {
        final boolean contestUnknown = Controller.isContestUnknownMode();
        contestChoosingPanel.setVisible(contestUnknown);

        if (!contestUnknown)
            contestChoosingPanel.setContest(Controller.getContestConnection().getContest());

        if (contestEditor != null)
            contestEditor.requestFocusInWindow();
    }

    public void deactivate() {
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addContestButton) {
            if (!applyChangesConfirmation()) return;
            setContestType(0);
            bean = new AdjustContestRequestBean();
            //TODO get rid of this hack (contest finish = now + hour)
            bean.getContest().setFinish(new Date(new Date().getTime() + 1000*60*60));
            refreshContestEditor();
        } else if (e.getSource() == removeContestButton) {
            if (JOptionPane.showConfirmDialog(
                    null,
                    "Вы уверены, что хотите удалить это соревнование?",
                    "Удаление соревования",
                    JOptionPane.YES_NO_CANCEL_OPTION
            ) != JOptionPane.YES_OPTION)
                return;

            removeContest();

            setContestType(-1);
            bean = new AdjustContestRequestBean();
            refreshContestEditor();
        } else if (e.getSource() == applyButton) {
            applyChanges();
        } else if (e.getSource() == undoButton) {
            contestChanged();
        } else if (e.getSource() == contestChoosingPanel) {
            contestChanged();
        }
    }

    private void removeContest() {
        RemoveContestRequest req = new RemoveContestRequest();
        req.sessionID = Controller.getContestConnection().getSessionID();
        req.contestID = contestChoosingPanel.getContest().contestID;
        try {
            Controller.getServer().doRequest(req);
        } catch (ServerReturnedError e) {
            Controller.log(e);
        } catch (GeneralRequestFailureException e) {
            //do nothing
        }
    }

    /**
     * @return Возвращает, верно ли что пользователь согласился продолжать работу, т.е. не нажал Cancel
     */
    private boolean applyChangesConfirmation() {
        //if (bean.isModified()) {
        /*switch (JOptionPane.showConfirmDialog(
                null,
                "У вас остались несохраненные изменения, сохранить?",
                "Несохраненные изменения",
                JOptionPane.YES_NO_CANCEL_OPTION)
                ) {
            case JOptionPane.YES_OPTION:
                applyChanges();
                break;
            case JOptionPane.NO_OPTION:
                //do nothing
                break;
            case JOptionPane.CANCEL_OPTION:
                return false;
        }*/
        //}

        return true;
    }

    private void contestChanged() {
        //request contest data

        GetContestDataResponse contestData;

        if (contestChoosingPanel.getContest() == null)
            contestData = null; //or make here empty data
        else {
            GetContestDataRequest req = new GetContestDataRequest();
            req.contestID = contestChoosingPanel.getContest().contestID;
            req.extendedData = null;
            req.infoType = GetContestDataRequest.InformationType.NoInfo;
            req.sessionID = Controller.getContestConnection().getSessionID();
            try {
                contestData = Controller.getServer().doRequest(req);
            } catch (ServerReturnedError serverReturnedError) {
                Controller.log(serverReturnedError);
                contestData = null;
            } catch (GeneralRequestFailureException e) {
                contestData = null;
            }
        }

        if (contestData == null) {
            setContestType(-1);
            bean.setContest(null);
            bean.setProblems(null); //or new ProblemDescriptionBean[]
            refreshContestEditor();
        } else {
            bean.setContest(new ContestDescriptionBean(contestData.contest));
            //copy problems ids
            bean.setProblems(new ProblemDescriptionBean[contestData.problems.length]);
            for (int i = 0; i < contestData.problems.length; i++)
                bean.getProblems()[i].setId(contestData.problems[i].id);

            setContestType(1);
            refreshContestEditor();
        }
    }

    //TODO move this to structured editor

    private void refreshContestEditor() {
        StructuredEditorModel model = new StructuredEditorModel();
        DSLBean usedBean;
        if (contestType == -1)
            usedBean = emptyBean;
        else
            usedBean = bean;
        model.setRootElement(new EditorRenderer(model, usedBean).getRenderResult());
        contestEditor.setModel(model);
    }

    private void applyChanges() {
        if (contestType == -1)
            return;

        AdjustContestRequest adjustContestRequest = null;
        CreateContestRequest createContestRequest = null;
        if (contestType == 1) {
            adjustContestRequest = bean.getBin();
            adjustContestRequest.sessionID = Controller.getContestConnection().getSessionID();
        } else {
            createContestRequest = new CreateContestRequest();
            createContestRequest.contest = bean.getContest().getBin();
            createContestRequest.sessionID = Controller.getContestConnection().getSessionID();
        }

        try {
            if (adjustContestRequest != null) {
                Controller.getServer().doRequest(adjustContestRequest);
            } else /*if (createContestRequest != null)*/ {
                CreateContestResponse resp = Controller.getServer().doRequest(createContestRequest);
                createContestRequest.contest.contestID = resp.createdContestID;
                contestChoosingPanel.setContest(createContestRequest.contest);
                setContestType(1);
            }
            contestChanged();
        } catch (ServerReturnedError e) {
            Controller.log(e);
        } catch (GeneralRequestFailureException e) {
            //do nothing
        }
    }

}
