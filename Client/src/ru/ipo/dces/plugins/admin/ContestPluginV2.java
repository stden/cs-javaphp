package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.client.Localization;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.plugins.admin.beans.*;
import ru.ipo.dces.plugins.admin.components.DCESEditorFactory;
import ru.ipo.dces.plugins.admin.components.DCESRendererFactory;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.File;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.beans.editor.DirectoryPropertyEditor;
import com.l2fprod.common.beans.editor.FilePropertyEditor;
import info.clearthought.layout.TableLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 31.07.2009
 * Time: 11:45:45
 */
public class ContestPluginV2 implements Plugin, ActionListener {

  private JPanel mainPanel;
  private ContestChoosingPanel contestChoosingPanel;

  private JButton addContestButton;
  private JButton removeContestButton;
  private JButton applyContestChangesButton;
  private JButton undoContestChangesButton;

  private JButton addProblemButton;
  private JButton removeProblemButton;
  private JButton upProblemButton;
  private JButton downProblemButton;
  private JButton debugProblemButton;
  private JButton downloadProblemButton;

  private JButton addUserField;
  private JButton removeUserField;
  private JButton upUserField;
  private JButton downUserField;

  private PropertySheetPanel contestProperties;
  private PropertySheetPanel problemProperties;
  private JList problemsList;
  private UserFieldsTable userFieldsTable;

  private PluginEnvironment environment;

  private ContestAdjustmentBean bean = new ContestAdjustmentBean();

//  private static final Color ENABLED_PROPERTY_FOREGROUND = Color.BLACK;
//  private static final Color DISABLED_PROPERTY_FOREGROUND = Color.LIGHT_GRAY;

  public ContestPluginV2(PluginEnvironment environment) {
    this.environment = environment;
    //environment.setTitle(Localization.getAdminPluginName(this.getClass()));
    initInterface();
    environment.setTitle(Localization.getAdminPluginName(this.getClass()));
  }

  private JButton addButtonToContestToolBar(JButtonBar toolBar, String text, Icon icon) {
    JButton button = new JButton(text, icon);
    button.addActionListener(this);
    toolBar.add(button);

    return button;
  }

  public void initInterface() {
    mainPanel = new JPanel(new TableLayout(new double[][]{
            {TableLayout.FILL}, {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL}
    }));

    contestChoosingPanel = new ContestChoosingPanel();
    contestChoosingPanel.setBeforeLabelGap(0);
    contestChoosingPanel.setPopup(true);
    contestChoosingPanel.addContestChangedActionListener(this);
    mainPanel.add(contestChoosingPanel, "0, 0");

    JButtonBar contestToolBar = new JButtonBar(JButtonBar.HORIZONTAL);
    //TODO get data from resources
    addContestButton = addButtonToContestToolBar(contestToolBar, "Добавить", new ImageIcon("images/add.gif"));
    removeContestButton = addButtonToContestToolBar(contestToolBar, "Удалить", new ImageIcon("images/remove.gif"));
    applyContestChangesButton = addButtonToContestToolBar(contestToolBar, "Применить", new ImageIcon("images/bigApply.gif"));
    undoContestChangesButton = addButtonToContestToolBar(contestToolBar, "Отменить", new ImageIcon("images/cancel.gif"));
    mainPanel.add(contestToolBar, "0, 1");

    //start center
    JSplitPane contestSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    contestSplitPane.setDividerLocation(250);
    mainPanel.add(contestSplitPane, "0, 2");

    contestProperties = new PropertySheetPanel();
    contestProperties.setRendererFactory(DCESRendererFactory.getInstance());
    contestProperties.setEditorFactory(DCESEditorFactory.getInstance());
    contestProperties.setDescriptionVisible(true);
    initContestPropertiesEditor();
    contestSplitPane.setLeftComponent(contestProperties);

    JPanel rightPanel = new JPanel(new TableLayout(new double[][]{
            {TableLayout.FILL}, {TableLayout.PREFERRED, 150, TableLayout.PREFERRED, TableLayout.FILL}
    }));
    contestSplitPane.setRightComponent(rightPanel);

    JButtonBar userFieldsToolBar = new JButtonBar(JButtonBar.HORIZONTAL);
    addUserField = addButtonToContestToolBar(userFieldsToolBar, "Добавить", new ImageIcon("images/add.gif"));
    removeUserField = addButtonToContestToolBar(userFieldsToolBar, "Удалить", new ImageIcon("images/remove.gif"));
    upUserField = addButtonToContestToolBar(userFieldsToolBar, "Вверх", new ImageIcon("images/up.gif"));
    downUserField = addButtonToContestToolBar(userFieldsToolBar, "Вниз", new ImageIcon("images/down.gif"));
    rightPanel.add(userFieldsToolBar, "0, 0");

    userFieldsTable = new UserFieldsTable();
    rightPanel.add(userFieldsTable, "0, 1");

    JButtonBar problemToolBar = new JButtonBar(JButtonBar.HORIZONTAL);
    addProblemButton = addButtonToContestToolBar(problemToolBar, "Добавить", new ImageIcon("images/add.gif"));
    removeProblemButton = addButtonToContestToolBar(problemToolBar, "Удалить", new ImageIcon("images/remove.gif"));
    upProblemButton = addButtonToContestToolBar(problemToolBar, "Вверх", new ImageIcon("images/up.gif"));
    downProblemButton = addButtonToContestToolBar(problemToolBar, "Вниз", new ImageIcon("images/down.gif"));
    debugProblemButton = addButtonToContestToolBar(problemToolBar, "Отладить", new ImageIcon("images/debug.gif"));
    downloadProblemButton = addButtonToContestToolBar(problemToolBar, "Скачать", new ImageIcon("images/download.gif"));
    rightPanel.add(problemToolBar, "0, 2");

    JSplitPane problemSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    problemSplitPane.setDividerLocation(250);
    rightPanel.add(problemSplitPane, "0, 3");

    problemsList = new JList();
    problemsList.setModel(bean.getProblemsListModel());
    problemsList.setSelectionModel(bean.getProblemsListSelectionModel());
    problemSplitPane.setLeftComponent(new JScrollPane(problemsList));

    problemProperties = new PropertySheetPanel();
    problemProperties.setEditorFactory(DCESEditorFactory.getInstance());
    problemProperties.setRendererFactory(DCESRendererFactory.getInstance());
    problemProperties.setDescriptionVisible(true);
    initProblemPropertiesEditor();
    problemSplitPane.setRightComponent(problemProperties);

    //set all views and register view changins to listeners
    addListeners();

    //setPropertiesEnabled(contestProperties, false);
    //setPropertiesEnabled(problemProperties, false);
    clearPropertiesValues(contestProperties);
    clearPropertiesValues(problemProperties);
    setProblemsToolBarView();
    setUserFieldsToolBarView();
    setContestToolBarView();

  }

  /*private void setPropertiesEnabled(PropertySheetPanel sheet, boolean enabled) {
    sheet.getTable().setPropertyForeground(enabled ? ENABLED_PROPERTY_FOREGROUND : DISABLED_PROPERTY_FOREGROUND);
    //TODO remove values from table
    sheet.setEnabled(enabled);
    sheet.getTable().setEnabled(enabled);
  }*/

  private void addListeners() {
    bean.addPropertyChangeListener("contestType", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        if (bean.getContestType() != -1) {
          //setPropertiesEnabled(contestProperties, true);
          initContestPropertiesEditor();
        } else {
          //setPropertiesEnabled(contestProperties, false);
          clearPropertiesValues(contestProperties);
        }
        setContestToolBarView();
        setProblemsToolBarView();
        setUserFieldsToolBarView();
      }
    });

    bean.addPropertyChangeListener("modified", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        setContestToolBarView();
      }
    });

    contestProperties.addPropertySheetChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        Property prop = (Property) evt.getSource();
        prop.writeToObject(bean.getContestDescription());
      }
    });

    problemProperties.addPropertySheetChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        Property prop = (Property) evt.getSource();
        int ind = bean.getProblemsListSelectionModel().getMinSelectionIndex();
        if (ind == -1) return;
        prop.writeToObject(bean.getProblemsListModel().get(ind));
        problemsList.repaint();
      }
    });

    bean.getProblemsListSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        setProblemsToolBarView();

        int ind = bean.getProblemsListSelectionModel().getMinSelectionIndex();
        if (ind == -1) {
          //setPropertiesEnabled(problemProperties, false);          
          clearPropertiesValues(problemProperties);
        } else {
          //setPropertiesEnabled(problemProperties, true);
          initProblemPropertiesEditor();
          problemProperties.readFromObject(bean.getProblemsListModel().get(ind));
        }
      }
    });
  }

  private void clearPropertiesValues(PropertySheetPanel sheetPanel) {
    for (Property p : sheetPanel.getProperties()) {
      sheetPanel.removeProperty(p);
    }
  }

  private void setUserFieldsToolBarView() {
    //do nothing by now
  }

  private void setContestToolBarView() {
    addContestButton.setEnabled(Controller.isSuperAdmin());
    removeContestButton.setEnabled(bean.getContestType() == 1 && Controller.isSuperAdmin());
    applyContestChangesButton.setEnabled(bean.isModified());
    undoContestChangesButton.setEnabled(bean.isModified());
  }

  private void setProblemsToolBarView() {
    boolean problemSelected = problemsList.getSelectedIndex() != -1;
    boolean contestCreated = bean.getContestType() == 1;

    addProblemButton.setEnabled(contestCreated && bean.getContestType() != -1);
    removeProblemButton.setEnabled(contestCreated && problemSelected);
    upProblemButton.setEnabled(contestCreated && mayMoveProblem(-1));
    downProblemButton.setEnabled(contestCreated && mayMoveProblem(1));
    debugProblemButton.setEnabled(contestCreated && problemSelected);
    downloadProblemButton.setEnabled(contestCreated && problemSelected);   
  }

  private void initContestPropertiesEditor() {
    DefaultProperty nameProperty = newProperty(
            "name",
            String.class,
            "Название",
            "Название соревнования, которое видят участники и по которому они его выбирают"
    );
    DefaultProperty descriptionProperty = newProperty(
            "description",
            String.class,
            "Описание",
            "Описание соревнования. Возможна html разметка"
    );
    DefaultProperty startProperty = newProperty(
            "start",
            DateBean.class,
            "Начало",
            "Время начала соревнования. До этого времени участники не могут подключиться к соревнованию"
    );
    DefaultProperty finishProperty = newProperty(
            "finish",
            DateBean.class,
            "Окончание",
            "Время конца соревнования. После этого времени участники не могут отсылать решения задач"
    );
    DefaultProperty registrationTypeProperty = newProperty(
            "registrationType",
            ContestDescription.RegistrationType.class,
            "Тип регистрации",
            "Способ регистрации участников. Могут ли они регистрироваться самостоятельно либо их регистрирует только администратор"
    );
    DefaultProperty resultsAccessPolicyProperty = newProperty(
            "resultsAccessPolicy",
            ResultsAccessPolicyBean.class,
            "Доступ к результатам",
            "Описывает, какие пользователи и когда имеют доступ к результатам"
    );
    DefaultProperty contestTimingProperty = newProperty(
            "contestTiming",
            ContestTimingBean.class,
            "Время проведения",
            "Тонкая настройка времени проведения соревнования"
    );

    //start properties

    DefaultProperty startDayProperty = newProperty(
            "day",
            Date.class,
            "День",
            "День начала соревнования"
    );
    DefaultProperty startHourProperty = newProperty(
            "hour",
            int.class,
            "Час",
            "Час начала соревнования"
    );
    DefaultProperty startMinuteProperty = newProperty(
            "minute",
            int.class,
            "Минуты",
            "Минута начала соревнования"
    );

    Property[] startProperties = {startDayProperty, startHourProperty, startMinuteProperty};
    startProperty.addSubProperties(startProperties);

    //finish properties

    DefaultProperty finishDayProperty = newProperty(
            "day",
            Date.class,
            "День",
            "День начала соревнования"
    );
    DefaultProperty finishHourProperty = newProperty(
            "hour",
            int.class,
            "Час",
            "Час начала соревнования"
    );
    DefaultProperty finishMinuteProperty = newProperty(
            "minute",
            int.class,
            "Минуты",
            "Минута начала соревнования"
    );

    Property[] finishProperties = {finishDayProperty, finishHourProperty, finishMinuteProperty};
    finishProperty.addSubProperties(finishProperties);

    //results access policy properties
    DefaultProperty contestPermissionProperty = newProperty(
            "contestPermission",
            ResultsAccessPolicy.AccessPermission.class,
            "Соревнование",
            "Права доступа во время соревнования"
    );
    DefaultProperty contestEndingPermissionProperty = newProperty(
            "contestEndingPermission",
            ResultsAccessPolicy.AccessPermission.class,
            "Окончание",
            "Права доступа во время окончания соревнования"
    );
    DefaultProperty afterContestPermissionProperty = newProperty(
            "afterContestPermission",
            ResultsAccessPolicy.AccessPermission.class,
            "После",
            "Права доступа после окончания соревнования"
    );

    Property[] resultsAccessPolicyProperties = new Property[]{
            contestPermissionProperty, contestEndingPermissionProperty, afterContestPermissionProperty
    };
    resultsAccessPolicyProperty.addSubProperties(resultsAccessPolicyProperties);

    //contest timing
    DefaultProperty selfContestStartProperty = newProperty(
            "selfContestStart",
            boolean.class,
            "Начинать самостоятельно",
            "Возможно ли начинать соревнование самостоятельно"
    );
    DefaultProperty maxContestDurationProperty = newProperty(
            "maxContestDuration",
            int.class,
            "Длительность",
            "Максимальная допустимая длительность соревнования"
    );
    DefaultProperty contestEndingStartProperty = newProperty(
            "contestEndingStart",
            int.class,
            "Начало окончания",
            "Начало окончания"
    );
    DefaultProperty contestEndingFinishProperty = newProperty(
            "contestEndingFinish",
            int.class,
            "Конец окончания",
            "Конец окончания"
    );

    Property[] conteseTimingProperties = new Property[]{
            selfContestStartProperty, maxContestDurationProperty, contestEndingStartProperty, contestEndingFinishProperty
    };
    contestTimingProperty.addSubProperties(conteseTimingProperties);

    contestProperties.setProperties(new Property[0]);

    //add all properties
    contestProperties.addProperty(nameProperty);
    contestProperties.addProperty(descriptionProperty);
    contestProperties.addProperty(startProperty);
    contestProperties.addProperty(finishProperty);
    contestProperties.addProperty(registrationTypeProperty);
    contestProperties.addProperty(resultsAccessPolicyProperty);
    contestProperties.addProperty(contestTimingProperty);
  }

  private void initProblemPropertiesEditor() {
    DefaultProperty nameProperty = newProperty(
            "name",
            String.class,
            "Название",
            "Название задачи"
    );
    DefaultProperty clientPluginAliasProperty = newProperty(
            "pluginAlias",
            String.class,
            "Плагин клиента",
            "Имя плагина стороны клиента"
    );
    DefaultProperty serverPluginAliasProperty = newProperty(
            "serverPluginAlias",
            String.class,
            "Плагин сервера",
            "Имя плагина стороны клиента"
    );
    DefaultProperty statementDataProperty = newProperty(
            "statementData",
            ZipBean.class,
            "Условие",
            "Файл или папка с данными для условия"
    );
    DefaultProperty answerDataProperty = newProperty(
            "answerData",
            ZipBean.class,
            "Ответ",
            "Файл или папка с данными для ответа"
    );

    //statement zip subproperties
    DefaultProperty statementAsFileProperty = newProperty(
            "file",
            File.class,
            "Файл",
            "Задание условия в виде одного файла"
    );
    DefaultProperty statementAsDirectoryProperty = newProperty(
            "file",
            File.class,
            "Директория",
            "Задание условия в виде директории"
    );

    statementDataProperty.addSubProperties(new Property[]{statementAsFileProperty, statementAsDirectoryProperty});

    //answer zip subproperties
    DefaultProperty answerAsFileProperty = newProperty(
            "file",
            File.class,
            "Файл",
            "Задание условия в виде одного файла"
    );
    DefaultProperty answerAsDirectoryProperty = newProperty(
            "file",
            File.class,
            "Директория",
            "Задание условия в виде директории"
    );

    answerDataProperty.addSubProperties(new Property[]{answerAsFileProperty, answerAsDirectoryProperty});

    DCESEditorFactory.getInstance().registerEditor(statementAsDirectoryProperty, DirectoryPropertyEditor.class);
    DCESEditorFactory.getInstance().registerEditor(answerAsDirectoryProperty, DirectoryPropertyEditor.class);
    DCESEditorFactory.getInstance().registerEditor(statementAsFileProperty, FilePropertyEditor.class);
    DCESEditorFactory.getInstance().registerEditor(answerAsFileProperty, FilePropertyEditor.class);
    DCESEditorFactory.getInstance().registerEditor(clientPluginAliasProperty, new DCESEditorFactory.PluginAliasEditor(PluginSide.Client));
    DCESEditorFactory.getInstance().registerEditor(serverPluginAliasProperty, new DCESEditorFactory.PluginAliasEditor(PluginSide.Server));

    DCESRendererFactory.getInstance().registerRenderer(statementAsDirectoryProperty,
            DCESRendererFactory.DirectoryOnlyRenderer.class);
    DCESRendererFactory.getInstance().registerRenderer(answerAsDirectoryProperty,
            DCESRendererFactory.DirectoryOnlyRenderer.class);
    DCESRendererFactory.getInstance().registerRenderer(statementAsFileProperty,
            DCESRendererFactory.FileOnlyRenderer.class);
    DCESRendererFactory.getInstance().registerRenderer(answerAsFileProperty,
            DCESRendererFactory.FileOnlyRenderer.class);

    problemProperties.setProperties(new Property[0]);

    //add all properties
    problemProperties.addProperty(nameProperty);
    problemProperties.addProperty(clientPluginAliasProperty);
    problemProperties.addProperty(serverPluginAliasProperty);
    problemProperties.addProperty(statementDataProperty);
    problemProperties.addProperty(answerDataProperty);
  }

  private DefaultProperty newProperty(String name, Class type, String displayName, String description) {
    DefaultProperty property = new DefaultProperty();

    property.setName(name);
    property.setDisplayName(displayName);
    property.setShortDescription(description);
    property.setType(type);

    return property;
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    contestChoosingPanel.setVisible(Controller.isContestUnknownMode());
  }

  public void deactivate() {
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addContestButton) {
      if (!applyChangesConfirmation()) return;

      bean.setModified(false);
      bean.setContestType(0);
      bean.getContestDescription().setDefault();
      bean.getProblemsListModel().clear();

      contestProperties.readFromObject(bean.getContestDescription());
    } else if (e.getSource() == removeContestButton) {
      if (JOptionPane.showConfirmDialog(
              null,
              "Вы уверены, что хотите удалить это соревнование?",
              "Удаление соревования",
              JOptionPane.YES_NO_CANCEL_OPTION
      ) != JOptionPane.YES_OPTION)        
          return;

      removeContest();

      bean.setData(null);
      bean.setContestType(-1);
    } else if (e.getSource() == applyContestChangesButton) {
      applyChanges();
    } else if (e.getSource() == undoContestChangesButton) {
      bean.setData(bean.getFreshContestData());
      if (bean.getFreshContestData() == null)
        bean.setContestType(-1);
      if (bean.getContestType() != -1)
        contestProperties.readFromObject(bean.getContestDescription());
      bean.setModified(false);
    } else if (e.getSource() == addProblemButton) {

      DefaultListModel listModel = bean.getProblemsListModel();
      ProblemDescriptionBean pdBean = bean.newProblemBean();
      listModel.addElement(pdBean);
      bean.getProblemsListSelectionModel().setSelectionInterval(listModel.getSize() - 1, listModel.getSize() - 1);

    } else if (e.getSource() == removeProblemButton) {

      DefaultListSelectionModel selectionModel = bean.getProblemsListSelectionModel();
      int selectionIndex = selectionModel.getMinSelectionIndex();
      if (selectionIndex == -1) return;
      bean.getProblemsListModel().remove(selectionIndex);

    } else if (e.getSource() == upProblemButton) {

      moveProblem(-1);

    } else if (e.getSource() == downProblemButton) {

      moveProblem(1);

    } else if (e.getSource() == debugProblemButton) {
      ProblemDescriptionBean pdb = (ProblemDescriptionBean) problemsList.getSelectedValue();
      if (pdb == null) return;

      if (bean.isModified()) {
        if (JOptionPane.showConfirmDialog(
                null,
                "Перед отладкой задачи все изменения соревнования должны бить применены. Применить их сейчас?",
                "Отладка задачи",
                JOptionPane.YES_NO_CANCEL_OPTION
        ) != JOptionPane.YES_OPTION)
            return;

        applyChanges();
      }
      
      Controller.debugProblem(pdb.getId(), contestChoosingPanel.getContest().contestID);
    } else if (e.getSource() == downloadProblemButton) {
      //TODO implement
    } else if (e.getSource() == contestChoosingPanel) {
      contestChanged();
    }
  }

  private void removeContest() {
    RemoveContestRequest req = new RemoveContestRequest();
    req.sessionID = Controller.getContestConnection().getSessionID();
    req.contestID = bean.getContestDescription().getContestID();
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
    if (bean.isModified()) {
      switch (JOptionPane.showConfirmDialog(
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
      }
    }

    return true;
  }

  private boolean mayMoveProblem(int dir) {
    int i = bean.getProblemsListSelectionModel().getMinSelectionIndex();
    return i != -1 && i + dir >= 0 && i + dir < bean.getProblemsListModel().getSize();
  }

  private void moveProblem(int dir) {
    if (!mayMoveProblem(dir)) return;

    DefaultListModel listModel = bean.getProblemsListModel();
    int selectionIndex = bean.getProblemsListSelectionModel().getMinSelectionIndex();

    Object temp = listModel.get(selectionIndex);
    listModel.set(selectionIndex, listModel.get(selectionIndex + dir));
    listModel.set(selectionIndex + dir, temp);

    bean.getProblemsListSelectionModel().setSelectionInterval(selectionIndex + dir, selectionIndex + dir);
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

    //fill bean with new Data
    if (contestData == null) {
      bean.getProblemsListModel().clear();
      bean.setData(null);
      bean.setContestType(-1);
    } else {
      bean.setData(contestData);
      bean.setContestType(1);
      contestProperties.readFromObject(bean.getContestDescription());
    }

    bean.setModified(false);
  }

  private void applyChanges() {
    if (bean.getContestType() == -1)
      return;

    AdjustContestRequest adjustContestRequest = null;
    CreateContestRequest createContestRequest = null;
    if (bean.getContestType() == 1) {
      adjustContestRequest = new AdjustContestRequest();

      adjustContestRequest.contest = bean.getAdjustmentForContestDescription(bean.getFreshContestData().contest);      

      adjustContestRequest.problems = bean.getAdjustmentForProblemsDescription(bean.getFreshContestData().problems);

      adjustContestRequest.sessionID = Controller.getContestConnection().getSessionID();
    } else {
      createContestRequest = new CreateContestRequest();

      createContestRequest.contest = bean.getContestDescription().getData();
      createContestRequest.contest.data = bean.getUserDataFields();
      createContestRequest.sessionID = Controller.getContestConnection().getSessionID();
    }

    try {
      if (adjustContestRequest != null) {
        Controller.getServer().doRequest(adjustContestRequest);
        contestChanged();
      } else /*if (createContestRequest != null)*/ {
        CreateContestResponse resp = Controller.getServer().doRequest(createContestRequest);
        createContestRequest.contest.contestID = resp.createdContestID;
        contestChoosingPanel.setContest(createContestRequest.contest);
        bean.setContestType(1);
      }
    } catch (ServerReturnedError e) {
      Controller.log(e);
    } catch (GeneralRequestFailureException e) {
      //do nothing
    }
  }

}
