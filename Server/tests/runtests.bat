REM phpunit --verbose --bootstrap main.php .
REM phpunit --bootstrap main.php .

phpunit --log-junit dces-tests.log --bootstrap main.php requests/ConnectToContestRequestTest.php
