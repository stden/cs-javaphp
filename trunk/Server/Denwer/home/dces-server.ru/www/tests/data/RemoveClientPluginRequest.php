<?php
class RemoveClientPluginRequest{
    public $sessionID;
    public $pluginAlias;

    function __construct(){
        $this->sessionID = null;
        $this->pluginAlias = 'Test client plugin';
    }
};
?>
