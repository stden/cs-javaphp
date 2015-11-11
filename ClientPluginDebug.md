# Отладка плагина стороны клиента #

DCES API предлагает framework для отладки плагинов стороны клиента. Без него для отладки нужно было бы иметь работающий DCES сервер, что не всегда возможно.

Для удобной отладки в DCES API определены класс `PluginBox` и интерфейс `ServerPluginEmulator`. Первый наследует JFrame и является окном, в котором работает отлаживаемый плагин.
`ServerPluginEmulator` это эмуляция серверного плагина, для отладки она должен быть реализована. Эмулятор должен уметь выдать условие задачи и проверить посылаемое решение.

Примерный код для запуска отладки
```
//создаем эмулятор сервера. Сам класс MyServerPluginEmulator описан далее
ServerPluginEmulator serverEmulator = new MyServerPluginEmulator();

//создаем окно для отладки
PluginBox box = new PluginBox(
                      MyPlugin.class, //класс с отлаживаемым плагином
                      serverEmulator, //эмулятор плагина сервера
                      "Имя Задачи" //имя задачи, параметр необязателен
                );
//делаем так, чтобы после закрытия окна программа закрывалась
box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//показываем окно на экране
box.setVisible(true);
```

Эмуляция сервера будет проверять посланное решение и сравнивать его с правильным ответом. Будем считать, что правильный ответ - это 42. Также эмулятор будет следить, что решение посылается не более чем три раза.

```
public class MyServerPluginEmulator implements ServerPluginEmulator {
     
      //сохраняем ответ
      public static final String RIGHT_ANSWER = "42";

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
    };
```