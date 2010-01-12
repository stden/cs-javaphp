ECHO OFF

cls
REM phpunit --bootstrap main.php .
REM phpunit --verbose --bootstrap main.php .

ECHO ON 

phpunit --log-junit dces-tests.log --bootstrap main.php requests/DisconnectRequestTest.php



