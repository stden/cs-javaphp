package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 21.05.2008
 * Time: 13:57:24
 */
public class UserDescription implements InfoFrame {

    public String login;
    public String password;
    public String name; //имя участника
    public String institution; //организация, т.е. школа, университет и т.п.
    //можно сюда добавлять дополнительные поля. Это не очень хороший вариант расширяемости, но хоть как

    public String extraInformation; //чтобы всегда была возможность написать дополнительную информацию, если
                                    //она почему-то не входит в набор стандартных полей
}
