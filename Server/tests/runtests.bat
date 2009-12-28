ECHO OFF
REM phpunit --verbose --bootstrap main.php .
REM phpunit --bootstrap main.php .
ECHO ON 

phpunit --log-junit dces-tests.log --bootstrap main.php requests/CreateContestRequestTest.php
