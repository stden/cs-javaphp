<?php
class ProblemDescription{
    public $id;
    public $clientPluginAlias;
    public $serverPluginAlias;
    public $name;
    public $statement;
    public $statementData;
    public $answerData;

    function __construct(){
        $this->id = -1;
        $this->clientPluginAlias = 'Test client plugin';
        $this->serverPluginAlias = 'Test server plugin';
        $this->name = 'Test problem'.rand(0,239239);
        $this->statement = null;
        $this->statementData = null;
        $this->answerData = null;
    }
};
?>
