<?php
class AdjustClientPluginRequest{
    public $sessionID;
    public $pluginAlias;
    public $pluginData;
    public $description;

    function __construct(){
        $this->sessionID = null;
        $this->pluginAlias = 'Test client plugin';
        $this->pluginData = 'jar file content';
        $this->description = 'Test client plugin description';
    }
};
?>
