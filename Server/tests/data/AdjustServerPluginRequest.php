<?php
class AdjustServerPluginRequest{
    public $sessionID;
    public $pluginAlias;
    public $pluginData;
    public $description;

    function __construct(){
        $this->sessionID = null;
        $this->pluginAlias = 'Test server plugin';
        $this->pluginData = 'jar file content';
        $this->description = 'Test server plugin description';
    }
};
?>
