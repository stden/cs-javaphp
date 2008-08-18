package ru.ipo.dces.tests;

import java.awt.Component;
import java.util.List;

import javax.swing.*;

import org.junit.Test;

import ru.ipo.dces.client.*;
import ru.ipo.dces.client.ClientDialog.OpenPanelAction;


import static org.junit.Assert.*;

public class TestClientDialog {
  @Test
  public void baseTest() {
    // При старте клиента появляется диалоговое окно
    ClientDialog cd = new ClientDialog(new JFrame());
    // с заголовком "DCES Client"
    assertEquals("DCES Client", cd.getTitle());
    // Окно разделено на две части с помощью SplitPane
    JSplitPane sp = (JSplitPane) cd.getContentPane().getComponent(0);
    assertNotNull(sp);
    // Левую и правую панель
    JPanel leftPanel = (JPanel) sp.getComponent(1);
    assertNotNull(leftPanel);
    JPanel rightPanel = (JPanel) sp.getComponent(2);
    assertNotNull(rightPanel);
    // На левой панели - кнопки для выбора Plugin'а
    for (Component c : leftPanel.getComponents()) {
      JButton btn = (JButton) c;
      assertNotNull(btn);
      // Для каждой кнопки указан обработчик, который открывает панель
      OpenPanelAction action = (OpenPanelAction) btn.getActionListeners()[0];
      assertNotNull(action);
    }
  }

  @Test
  public void test1() {
    // Участник контеста взаимодействует с системой для получения доступных
    // контестов. Он просит выдать список доступных контестов. Система выдает
    // список.
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.server = new MockServer();
    // TODO: Сделать, чтобы запрос происходил по кнопке
    IServer server = cd.server;
    server.addContest("Example contest #1");
    server.addContest("Example contest #2");
    List<Contest> contestList = server.getAvaibleContests();
    assertEquals("Example contest #1", contestList.get(0).name);
    assertEquals("Example contest #2", contestList.get(1).name);
  }

  @Test
  public void test2() {
    // Анонимный пользователь хочет посмотреть контест.
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.server = new MockServer();
    IServer server = cd.server;
    server.addContest("Example contest #1");
    Contest contest = server.getContest(0);
    assertEquals("Example contest #1", contest.name);
  }

  @Test
  public void test3() {
    // Анонимный пользователь хочет зарегистрироваться в системе для проведения
    // контеста. Он просит выдать регистрационную форму, если регистрация без
    // участия администратора возможна, система выдает эту форму, он вводит свои
    // данные, система проверяет данные на корректность и полноту, и, в случае
    // успеха, выдает список доступных для участия контестов. В случае неудачи
    // система просит пользователя повторно ввести данные.
  }

  @Test
  public void test4() {
    // Участник контеста хочет участвовать в контесте. Он нажимает на элемент
    // списка контеста. Список контестов по-прежнему доступен, так что он может
    // выбрать любой другой контест, но при этом оказывается доступной
    // информация о контесте.
  }

  @Test
  public void test5() {
    // Участник контеста хочет решать задачу. Он выбирает задачу из списка задач
    // на странице контеста, система загружает задачу (отображая прогресс
    // загрузки), далее отображает информацию о самой задаче: условие задачи,
    // механизмы ввода решений, механизмы отправки решений на сервер, механизмы
    // повторной отправки решения - в случае, если администратор разрешил такую
    // возможность). Также система отображает список других задач, оставшееся
    // время (если оно указано), возможность вернуться к странице контеста.
  }

}
