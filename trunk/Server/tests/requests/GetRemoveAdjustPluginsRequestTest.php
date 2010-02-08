<?php

class GetRemoveAdjustPluginsTestCase extends DCESWithAllRolesTestCase {
    
    protected $clientPluginAliases = array();
    protected $clientPluginDescriptions = array();
    protected $serverPluginAliases = array();
    protected $serverPluginDescriptions = array();
    
    public function setUp()
    {
        parent::setUp();
        
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++)
        {
            $req = TestData::fillRequest('AdjustPluginRequest', array('sessionID'=>$this->sessionID,
                                                                      'side'=>'Client',
                                                                      'description'=>'cd'.$i,
                                                                      //'description'=>TestData::genUnicodeStr(42),
                                                                      'pluginAlias'=>'ca'.$i,
                                                                      //'pluginAlias'=>TestData::genUnicodeStr(12),
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
            
            $this->clientPluginAliases[] = $req->pluginAlias;
            $this->clientPluginDescriptions[] = $req->description;
            
            
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        
        
            $req = TestData::fillRequest('AdjustPluginRequest', array('sessionID'=>$this->sessionID,
                                                                      'side'=>'Server',
                                                                      //'description'=>TestData::genUnicodeStr(42),
                                                                      'description'=>'sd'.$i,
                                                                      //'pluginAlias'=>TestData::genUnicodeStr(12),
                                                                      'pluginAlias'=>'sa'.$i,
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
        
            $this->serverPluginAliases[] = $req->pluginAlias;
            $this->serverPluginDescriptions[] = $req->description;
        
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        }
    }
    
    public function testAvailablePlugins() 
    {
        $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>'Client', 'sessionID'=>$this->sessionID));
        $res = RequestSender::send($req);
        
        sort($this->clientPluginAliases);
        sort($res->aliases);
        sort($this->clientPluginDescriptions);
        sort($res->descriptions);
        
        $this->assertEquals($this->clientPluginAliases, $res->aliases);
        $this->assertEquals($this->clientPluginDescriptions, $res->descriptions);

        $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>'Server', 'sessionID'=>$this->sessionID));
        $res = RequestSender::send($req);

        sort($this->serverPluginAliases);
        sort($res->aliases);
        sort($this->serverPluginDescriptions);
        sort($res->descriptions);

        $this->assertEquals($this->serverPluginAliases, $res->aliases);
        $this->assertEquals($this->serverPluginDescriptions, $res->descriptions);
    }
    
    public function testRemovePlugin() {
        $removedAliases = array();
        sort($this->clientPluginAliases);
        foreach($this->clientPluginAliases as $alias) {
            
            $req = TestData::fillRequest('RemovePluginRequest', array('pluginAlias'=>$alias, 
                                                                      'sessionID'=>$this->sessionID,
                                                                      'side'=>'Client'));
            //assert that request completed successfully                                                                      
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
            $removedAliases[] = $alias;
            
            //check
            $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>'Client', 'sessionID'=>$this->sessionID));
            $res = RequestSender::send($req);
            
            $union = array_merge($removedAliases, $res->aliases);
            sort($union);
            $this->assertEquals($this->clientPluginAliases, $union);
        }
        
        //WARNING: COPY-PASTE
        $removedAliases = array();
        sort($this->serverPluginAliases);
        foreach($this->serverPluginAliases as $alias) {
            
            $req = TestData::fillRequest('RemovePluginRequest', array('pluginAlias'=>$alias, 
                                                                      'sessionID'=>$this->sessionID,
                                                                      'side'=>'Server'));
            //assert that request completed successfully                                                                      
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
            $removedAliases[] = $alias;
            
            //check
            $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>'Server', 'sessionID'=>$this->sessionID));
            $res = RequestSender::send($req);
            
            $union = array_merge($removedAliases, $res->aliases);
            sort($union);
            $this->assertEquals($this->serverPluginAliases, $union);
        }
    }
    
    /**
     *@dataProvider badFilenameProvider
     */
    public function testBadAliases($isGood, $type, $fn) 
    {
        $req = TestData::fillRequest('AdjustPluginRequest', array('sessionID'=>$this->sessionID,
                                                                      'side'=>$type,
                                                                      'description'=>TestData::genUnicodeStr(42),
                                                                      'pluginAlias'=>$fn,
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
        $this->assertEquals(createFailRes(239), RequestSender::send($req));    
    }
    
    public function badFilenameProvider()
    {
        return TestData::getData('createPluginFilename');    
    }
}

?>
