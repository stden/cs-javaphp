package ru.ipo.dces.tests.test;

import ru.ipo.dces.debug.ServerPluginEmulator;

import java.util.HashMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.04.2009
 * Time: 15:37:53
 * To change this template use File | Settings | File Templates.
 */
   public class MYServerPluginEmulator implements ServerPluginEmulator {

      //сохраняем ответ
      public static final String RIGHT_ANSWER = "4gggggggg";

      //метод проверки решения.
      //получает на вход solution решение,  result - пустой мап, который надо заполнить результатом проверки и state - произвольный объект состояния. Мы будем использовать состояние, чтобы хранить одно число, количество раз, сколько посылается решение
      //возвращать необходимо новое состояние задачи
      public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) {

        //приводим состояние к типу, который используем для хранения состояния
        Integer intState = (Integer)state;

        //вычисляем номер текущей посылки решения
        //при первом вызове метода state = null
        if (intState == null)
          intState = 1;
        else
          intState ++ ;

        if (intState <= 3) {
          //если посылаем не более чем третий раз, то можно проверить решение
          String answer = solution.get("answer");
          //сравниваем с правильным ответом
          if (RIGHT_ANSWER.equals(answer))
            result.put("result", "yes");
          else
            result.put("result", "no");
        }
        else {
          //если посылаем более чем третий раз, отвечаем, что проверять не надо
          result.put("result", "no more submissions");
        }

        //возвращаем новое состояние
        return intState;
      }

      //в этом методе необходимо создать условие задачи и вернуть каталог, в котором оно лежит
      //будем считать, что условие уже лежит в каталоге C://dces/problem_folder, его не надо создавать, достаточно вернуть каталог
      public File getStatement() {
        return new File("C://dces/problem_folder");
      }
    }