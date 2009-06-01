package ru.ipo.dces.plugins;

import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.clientservercommunication.GetContestResultsRequest;
import ru.ipo.dces.clientservercommunication.GetContestResultsResponse;
import ru.ipo.dces.clientservercommunication.StopContestRequest;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import info.clearthought.layout.TableLayout;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 09.05.2009
 * Time: 13:43:54
 */
@DCESPluginLoadable
public class RyzhikResults implements Plugin {

  private JPanel mainPanel;
  private PluginEnvironment environment;
  private String[][] results;

  private enum InterfaceView {
    Nothing, NoResults, Results,
  }

  private InterfaceView interfaceView = InterfaceView.Nothing;

  public RyzhikResults(PluginEnvironment environment) {
    this.environment = environment;

    environment.setTitle("Резюме");

    mainPanel = new JPanel();
  }

  private InterfaceView getNeededInterfaceView() {
    ServerFacade server = environment.getServer();

    InterfaceView interfaceView;

    try {
      GetContestResultsRequest r = new GetContestResultsRequest();
      r.sessionID = environment.getSessionID();
      r.contestID = -1;
      GetContestResultsResponse response = server.doRequest(r);
      String[][] getResults = response.table[response.userLine];

      //remove admin and pdarticipant info if there is some

      int removeFirstColumns = response.headers[0].equals("admin info") ? 2 : 1;
      results = new String[getResults.length - removeFirstColumns - 1][]; //1 column is removed from the end
      System.arraycopy(getResults, removeFirstColumns, results, 0, results.length);
      interfaceView = InterfaceView.Results;
    } catch (ServerReturnedError ignored) {
      interfaceView = InterfaceView.NoResults;
    } catch (GeneralRequestFailureException ignored) {
      interfaceView = InterfaceView.NoResults;
    }
    return interfaceView;
  }

  private void setInterface(InterfaceView interfaceView) {

    if (this.interfaceView == interfaceView) return;

    mainPanel.removeAll();

    this.interfaceView = interfaceView;

    if (interfaceView == InterfaceView.Nothing) {
      environment.log("Ошибка в плагине отображения результов, оратитесь к разработчикам", LogMessageType.Error);
      return;
    }

    if (interfaceView == InterfaceView.NoResults)
      setInterfaceDuringContest();
    else
      setInterfaceWithResults();
  }

  private void setInterfaceWithResults() {

    JTextPane textPane = new JTextPane();
    //StyledDocument doc = textPane.getStyledDocument();
    textPane.setContentType("text/html");
    textPane.setText(getResultsText());
    textPane.setEditable(false);
    JScrollPane scroll = new JScrollPane(textPane);

    mainPanel.setLayout(new GridLayout(1, 1));
    mainPanel.add(scroll);

    mainPanel.validate();
  }

  private void setInterfaceDuringContest() {

    JLabel label = new JLabel("Результаты соревнования можно посмотреть только после окончания соревнования.");
    JButton button = new JButton("Завершить досрочно");
    int border = 50;
    double size[][] = {{border, 50, 400, 50, border, TableLayout.FILL},
            {border, 30, border, 30, TableLayout.FILL}};
    TableLayout tbl = new TableLayout(size);
    mainPanel.setLayout(tbl);
    mainPanel.add(label, "1, 1, 3, 1");
    mainPanel.add(button, "2, 2, c, c");

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(null, "После завершения соревнования Вы не сможете изменить Ваши ответы. Вы уверены, что хотите завершить соревнование досрочно?", "Подтверждение досрочного завершения", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
          stopContest();
      }
    });

    mainPanel.validate();
  }

  private String getResultsText() {

    int d1, r1, w1;
    d1 = 0;
    r1 = 0;
    w1 = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 5; j++) {
        if (results[i][j].charAt(0) == '0') d1++;
        else {
          if (results[i][j].charAt(0) == results[i][j].charAt(1)) r1++;
          else w1++;
        }
      }
    }

    int d2, r2, w2;
    d2 = 0;
    r2 = 0;
    w2 = 0;
    for (int i = 2; i < 4; i++) {
      for (int j = 0; j < 5; j++) {
        if (results[i][j].charAt(0) == '0') d2++;
        else {
          if (results[i][j].charAt(0) == results[i][j].charAt(1)) r2++;
          else w2++;
        }
      }
    }

    int d3, r3, w3;
    d3 = 0;
    r3 = 0;
    w3 = 0;
    for (int i = 4; i < 6; i++) {
      for (int j = 0; j < 5; j++) {
        if (results[i][j].charAt(0) == '0') d3++;
        else {
          if (results[i][j].charAt(0) == results[i][j].charAt(1)) r3++;
          else w3++;
        }
      }
    }

    int w = w1+w2+w3;
    int r = r1+r2+r3;
    int d = d1+d2+d3;
    String ball[] = {" баллов", " балл", " балла", " балла", " балла", " баллов", " баллов", " баллов", " баллов", " баллов", " баллов"};
    String text = "<html>" + "<body><h3>Здравствуйте! Ознакомьтесь, пожалуйста, с Вашими результатами:</h3>";
    String testText[]={"Тест не пройден. Ваши результаты аналогичны результатам, полученным случайно. ",
            "Тест пройден на высшем уровне. ",
            "Тест не пройден. ",
            "Тест пройден, но зафиксированы пробелы в знаниях. ",
            "Тест пройден, но слабые места есть и Вы их знаете. ",
            "Тест пройден плохо. ",
            "Тест пройден очень хорошо. ",
            "Тест пройден хорошо, но слабые места есть и Вы их знаете.",
            "Тест пройден, но зафиксированы некоторые пробелы в знаниях. ",
            "Тест пройден, но есть много слабых мест. "};
    text += "Сумма Ваших баллов: " + (r - w) + "<br>";
    text += ""+testText[Functions.getText(w,r,d)-1]+"<br>";
    text += "по теме <b>Числа</b> Вы набрали: " + (r1 - w1) + ball[Math.abs(r1 - w1)] + ".<br/>";
    text += "по теме <b>Функции</b> Вы набрали: " + (r2 - w2) + ball[Math.abs(r2 - w2)] + ".<br/>";
    text += "по теме <b>Фигуры</b> Вы набрали: " + (r3 - w3) + ball[Math.abs(r3 - w3)] + ".<br/>";

    int t1, t2, t3;
    t1 = Functions.getSecText(w1, r1, d1);   //text number for "Числа"
    t2 = Functions.getSecText(w2, r2, d2);   //text number for "Функции"
    t3 = Functions.getSecText(w3, r3, d3);   //text number for "Фигуры"

    String SecText[][] = {{"По теме ", "По темам ", " знания неудовлетворительные. ", " знания неудовлетворительные. "},
            {"По теме ", "По темам ", " знания превосходные. ", " знания превосходные. "},
            {"Тема ", "Темы ", " не осовена. ", " не освоены. "},
            {"По теме ", "По темам ", " знания удовлетворительные, но не устойчивые.", " знания удовлетворительные, но не устойчивые. "},
            {"Тема ", "Темы ", " освоена плохо. ", " освоены плохо. "},
            {"По теме ", "По темам ", " знания неудовлетворительные.", " знания неудовлетворительные."},
            {"Тема ", "Темы ", " освоена очень хорошо.", " освоены очень хорошо."},
            {"Тема ", "Темы ", " освоена слабо.", " освоены слабо."},
            {"Темой ", "Темами ", " Вы владеете хорошо. ", " Вы владеете хорошо. "},
            {"По теме ", "По темам ", " знания неустойчивые.", " знания неустойчивые. "}};
//if text numbers are different
    if (((t1 != t2) & (t2 != t3)) & (t1 != t3)) {
      text += SecText[t1 - 1][0] + " <b>Числа</b>" + SecText[t1][2];
      text += SecText[t2 - 1][0] + " <b>Функции</b>" + SecText[t2][2];
      text += SecText[t3 - 1][0] + " <b>Фигуры</b>" + SecText[t3][2];
    }

//if text numbers are not different
    else {
      if ((t1 == t2) & (t1 == t3))
        text += SecText[t1 - 1][1] + " <b>Числа, Функции, Фигуры</b>" + SecText[t1 - 1][3] + "<br>";
      else {
        if (t1 == t2)
          text += SecText[t1 - 1][1] + "<b>Числа, Функции</b>" + SecText[t1 - 1][3] + SecText[t3 - 1][0] + "<b>Фигуры</b>" + SecText[t3 - 1][2] + "<br>";
        if (t1 == t3)
          text += SecText[t1 - 1][1] + "<b>Числа, Фигуры</b>" + SecText[t1 - 1][3] + SecText[t2 - 1][0] + "<b>Функции</b>" + SecText[t2 - 1][2] + "<br>";
        if (t2 == t3)
          text += SecText[t2 - 1][1] + "<b>Функции, Фигуры</b>" + SecText[t2 - 1][3] + SecText[t1 - 1][0] + "<b>Числа</b>" + SecText[t1 - 1][2] + "<br>";
      }
    }

    text += "<br/>";

    float e[] = {0, 0, 0, 0, 0};
    float p[] = {0, 0, 0, 0, 0};
    float e0[] = {0, 0, 0, 0, 0};
    float p0[] = {0, 0, 0, 0, 0};
    float s[] = {0, 0, 0, 0, 0};
    float s0[] = {0, 0, 0, 0, 0};

    for (int i = 0; i < 6; i++) {

      for (int j = 0; j < 5; j++) {

        for (int k = 0; k < 5; k++) {
          if (results[i][j].charAt(3 + k) == '+') {
             e0[k]++;
             s0[k]++;

            if (results[i][j].charAt(1) == results[i][j].charAt(0)) {e[k]++; s[k]++;}
          }
          if (results[i][j].charAt(3 + k) == '?') {
             p0[k]++;
             s0[k]+=0.5;

            if (results[i][j].charAt(1) == results[i][j].charAt(0)) {p[k]++;s[k]+=0.5;}
          }
        }
      }
    }

    String param[] = {" умением оперировать фактами",
            " вычислительными умениями",
            " приемами визуального представления знаний",
            " умением переформулировать задачи и переходить от одной формы представления знаний к другой",
            " умением строить логические суждения и выводы "};

    String paramText[] = {"Вы в совершенстве владеете",
            "Вы хорошо владеете",
            "Вероятнее всего, Вы хорошо владеете",
            "Вероятнее всего, Вы не очень хорошо владеете",
            "Вероятнее всего, Вы на удовлетворительном уровне владеете",
            "Вероятнее всего, Вы на низком уровне владеете",
            "Вы показали на низком уровне владение",
    };
    int t[] = {0, 0, 0, 0, 0};
    for (int k = 0; k < param.length; k++) {
      e[k] = Math.round((e[k] / e0[k]) * 100);
      p[k] = Math.round((p[k] / p0[k]) * 100);
      s[k] = Math.round((s[k] / s0[k]) * 100);
      t[k] = Functions.getParamText(e[k], p[k]);
      text += "Владение " + param[k] + ": " + e[k]+"%. " +" Или "+ s[k]+ "%<br/>";
    }

    
    //для объединения случаев, где вариант текста совпал, проводим сравнение вариантов текста
    

    for (int i = 0; i < t.length; i++) {
      for (int j = i + 1; j < t.length; j++) {
        if (t[i] == t[j]) {
          if (!param[j].equals("")) {
            param[i] += ", " + param[j];
            param[j] = "";
          }
        }
      }
    }
    text += " ";
    for (int k = 0; k < param.length; k++) {
      if ((!param[k].equals("")) & (t[k] != 8)) text += " " + paramText[t[k] - 1] + " " + param[k];
    }

    return text + "</body></html>";
  }

  private void stopContest() {
    ServerFacade server = environment.getServer();
    StopContestRequest stopRequest = new StopContestRequest();
    stopRequest.contestID = -1;
    stopRequest.sessionID = environment.getSessionID();
    try {
      server.doRequest(stopRequest);
    } catch (Exception e) {
      environment.log("failed to stop contest: " + e.getMessage(), LogMessageType.Error);
    }

    setInterface(getNeededInterfaceView());
    mainPanel.repaint();
    mainPanel.doLayout();
  }

  public JPanel getPanel
          () {
    return mainPanel;
  }

  public void activate
          () {
    if (this.interfaceView == InterfaceView.Results) return;
    InterfaceView interfaceView = getNeededInterfaceView();
    setInterface(interfaceView);
  }

  public void deactivate
          () {
    //do nothing
  }
}