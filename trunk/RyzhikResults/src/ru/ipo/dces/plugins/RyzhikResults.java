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
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Arrays;
import java.io.StringReader;
import java.io.IOException;

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
    //TODO нарисовать интферфейс с результатом
    //Метод getResultsTest() как бы выдает результаты (сейчас текстом)
    //mainPanel - панель, положить весь интерфейс на нее

    /*JTextPane resultsText = new JTextPane();
    try {
      resultsText.setContentType("text/html");
      resultsText.read(new StringReader(getResultsText()), null);
      resultsText.setPage("http://localhost");
    } catch (IOException e) {
      //do nothing
    }*/

    JTextArea resultsText = new JTextArea(getResultsText());

    resultsText.setEditable(false);
    resultsText.setFont(resultsText.getFont().deriveFont((float)14));
    JScrollPane scroll = new JScrollPane(resultsText);
    mainPanel.setLayout(new GridLayout(1,1));
    mainPanel.add(scroll);
    
    mainPanel.validate();
  }

  private void setInterfaceDuringContest() {
    //TODO нарисовать инферфейс плагина результатов во время соревнования.
    //mainPanel - панель, положить весь интерфейс на неё
    //чтобы завершить соревнование, пользоваться методом stopContest();
    JLabel label = new JLabel("Результаты соревнования можно посмотреть только после окончания соревнования.");
    JButton button = new JButton("Завершить досрочно");
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(label, BorderLayout.CENTER);
    mainPanel.add(button, BorderLayout.SOUTH);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //TODO отладить вывод подтверждения
        if (JOptionPane.showConfirmDialog(null, "точно?", "заголовок", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
          stopContest();
      }
    });

    mainPanel.validate();
  }

  private String getResultsText() {
    /*String results[][] = {
            {"++ ++?--", "+- ?-++?", "-- +-+-?", "?? +-+-?", "!0 +-++?"},
            {"-0 ++-??", "+0 ++-+?", "++ --?++", "+- ++-?+", "-- +++??"},
            {"++ ++?--", "+- ?-++?", "-- +-+-?", "?? +-+-?", "!0 +-++?"},
            {"-0 ++-??", "+0 ++-+?", "++ --?++", "+- ++-?+", "-- +++??"},
            {"++ ?+?--", "-- ?-++?", "-- +-+-?", "?? ?-+-?", "!0 +-++?"},
            {"-0 ++-??", "+0 ++-+?", "++ --?++", "++ ?+-?+", "-- +++??"},
    };*/
    int d1, r1, w1;
    d1 = 0;
    r1 = 0;
    w1 = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 5; j++) {
        if (results[i][j].charAt(1) == '0') d1++;
        else {
          if (results[i][j].charAt(1) == results[i][j].charAt(0)) r1++;
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
        if (results[i][j].charAt(1) == '0') d2++;
        else {
          if (results[i][j].charAt(1) == results[i][j].charAt(0)) r2++;
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
        if (results[i][j].charAt(1) == '0') d3++;
        else {
          if (results[i][j].charAt(1) == results[i][j].charAt(0)) r3++;
          else w3++;
        }
      }
    }

    String ball[] = {" баллов", " балл", " балла", " балла", " балла", " баллов", " баллов", " баллов", " баллов", " баллов", " баллов"};
    String text = "";/*"<html>" +
            "<head>" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html\">" +
            "<title>" + "результаты" + "</title>" +
            "</head>" +
    "<body><h1>Результаты</h1>";*/
    text += "по теме Числа Вы набрали: " + (r1 - w1) + ball[Math.abs(r1 - w1)] + ".<br/>";
    text += "по теме Функции Вы набрали: " + (r2 - w2) + ball[Math.abs(r2 - w2)] + ".<br/>";
    text += "по теме Фигуры Вы набрали: " + (r3 - w3) + ball[Math.abs(r3 - w3)] + ".<br/>";

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
      text += SecText[t1 - 1][0] + "Числа" + SecText[t1][2];
      text += SecText[t2 - 1][0] + "Функции" + SecText[t2][2];
      text += SecText[t3 - 1][0] + "Фигуры" + SecText[t3][2];
    }

//if text numbers are not different
    else {
      if ((t1 == t2) & (t1 == t3)) text += SecText[t1 - 1][1] + "Числа, Функции, Фигуры" + SecText[t1 - 1][3];
      else {
        if (t1 == t2)
          text += SecText[t1 - 1][1] + "Числа, Функции" + SecText[t1 - 1][3] + SecText[t3 - 1][0] + "Фигуры" + SecText[t3 - 1][2];
        if (t1 == t3)
          text += SecText[t1 - 1][1] + "Числа, Фигуры" + SecText[t1 - 1][3] + SecText[t2 - 1][0] + "Функции" + SecText[t2 - 1][2];
        if (t2 == t3)
          text += SecText[t2 - 1][1] + "Функции, Фигуры" + SecText[t2 - 1][3] + SecText[t1 - 1][0] + "Числа" + SecText[t1 - 1][2];
      }
    }

    float e[] = {0, 0, 0, 0, 0};
    float p[] = {0, 0, 0, 0, 0};
    float e0[] = {0, 0, 0, 0, 0};
    float p0[] = {0, 0, 0, 0, 0};

    for (int i = 0; i < 6; i++) {

      for (int j = 0; j < 5; j++) {

        for (int k = 0; k < 5; k++) {
          if (results[i][j].charAt(3 + k) == '+') {
            e0[k]++;
            if (results[i][j].charAt(1) == results[i][j].charAt(0)) e[k]++;
          }
          if (results[i][j].charAt(3 + k) == '?') {
            p0[k]++;
            if (results[i][j].charAt(1) == results[i][j].charAt(0)) p[k]++;
          }
        }
      }
    }

    String param[] = {"умением оперировать фактами",
            "вычислительными умениями",
            "приемами оперирования визуальными представлениями знаний",
            "умением переформулировать задачи и переходить от одной формы представления знаний к другой",
            "умением строить логические суждения и выводы "};

    String paramText[] = {"Вы в совершенстве владеете",
            "Вы хорошо владеете",
            "Вероятнее всего, Вы хорошо владеете",
            "Вероятнее всего, Вы не очень хорошо владеете",
            "Вероятнее всего, Вы на удовлетворительном уровне владеете",
            "Вероятнее всего, Вы на низком уровне владеете",
            "Показали на низком уровне владение",
            "Не владеете "};
    int t[] = {0, 0, 0, 0, 0};
    for (int k = 0; k < 5; k++) {
      e[k] = Math.round((e[k] / e0[k]) * 100);
      p[k] = Math.round((p[k] / p0[k]) * 100);
      t[k] = Functions.getParamText(e[k], p[k]);
      text += "Владение " + param[k] + ": " + e[k] + "%<br/><br/>";
    }

    boolean condition[] = {true, true, true, true, true};
    for (int i = 0; i < 5; i++) {
      for (int j = i + 1; j < 5; j++) {
        if (t[i] == t[j]) {
          condition[i] = false;
          condition[j] = false;
        }
      }
    }
    for (int k = 0; k < 5; k++) {
      text += paramText[t[k] - 1] + " ";
      if (condition[k]) text += param[k] + ". ";
//     else text+=
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

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    if (this.interfaceView == InterfaceView.Results) return;
    InterfaceView interfaceView = getNeededInterfaceView();
    setInterface(interfaceView);
  }

  public void deactivate() {
    //do nothing
  }
}