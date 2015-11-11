Клиент/Серверная система для распределенной обработки информации.
### Клиентская часть (на Java) ###
  * сериализует любой Java-класс
  * шифрует его по RSA
  * соединяется с сервером по HTTP и отправляет пакет
  * ждёт ответа от сервера
  * дешифрует ответ
  * десериализует ответ
  * передаёт ответ назад
### Серверная часть (на PHP) ###
  * принимает пакет
  * расшифровывает его
  * десериализует
  * исполняет
  * создаёт ответный пакет
  * шифрует
  * отправляет обратно клиенту.
Трафик между сервером и клиентом шифруется по RSA.