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
                                                                      'description'=>TestData::genUnicodeStr(42),
                                                                      'pluginAlias'=>TestData::genFileName(255),
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
            
            $this->clientPluginAliases[] = $req->pluginAlias;
            $this->clientPluginDescriptions[] = $req->description;
            
            
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        
        
            $req = TestData::fillRequest('AdjustPluginRequest', array('sessionID'=>$this->sessionID,
                                                                      'side'=>'Server',
                                                                      'description'=>TestData::genUnicodeStr(42),
                                                                      'pluginAlias'=>TestData::genFileName(255),
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
        
            $this->serverPluginAliases[] = $req->pluginAlias;
            $this->serverPluginDescriptions[] = $req->description;
        
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        }
    }
    
    /**
     *@dataProvider pluginSideProvider
     */
    public function testAvailablePlugins($side) 
    {
        $pluginAliases = $side.'PluginAliases';
        $pluginDescriptions = $side.'PluginDescriptions';
        
        $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>$side, 'sessionID'=>$this->sessionID));
        $res = RequestSender::send($req);
        
        sort($this->$pluginAliases);
        sort($res->aliases);
        sort($this->$pluginDescriptions);
        sort($res->descriptions);
        
        $this->assertEquals($this->$pluginAliases, $res->aliases);
        $this->assertEquals($this->$pluginDescriptions, $res->descriptions);
    }
    
    
    /**
     *@dataProvider pluginSideProvider
     */
    public function testRemovePlugin($side) {
        
        $pluginAliases = $side.'PluginAliases';
        
        $removedAliases = array();
        sort($this->$pluginAliases);
        foreach($this->$pluginAliases as $alias) {
            
            $req = TestData::fillRequest('RemovePluginRequest', array('pluginAlias'=>$alias, 
                                                                      'sessionID'=>$this->sessionID,
                                                                      'side'=>$side));
            //assert that request completed successfully                                                                      
            $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
            $removedAliases[] = $alias;
            
            //check
            $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>$side, 'sessionID'=>$this->sessionID));
            $res = RequestSender::send($req);
            
            $union = array_merge($removedAliases, $res->aliases);
            sort($union);
            $this->assertEquals($this->$pluginAliases, $union);
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
        
        //TODO: test other requests for bad pluginAliasNames and for null, '', 0, etc.
        /*$req = TestData::fillRequest('RemovePluginRequest', array('pluginAlias'=>TestData::getRandomValue(array(null, '', 0)), 
                                                                      'sessionID'=>$this->sessionID,
                                                                      'side'=>$side));
          $this->assertEquals(createFailRes(239), RequestSender::send($req));
        */ 
    }
    
    
     /**
     *@dataProvider pluginSideProvider
     */
    public function testBadContextForPluginRequests($side) 
    {
        $bad = array(null, '', 0, TestData::genASCIIStr(24));
        
        foreach($bad as $sessionID)
        {
            $req = TestData::fillRequest('AvailablePluginsRequest', array('pluginSide'=>$side, 'sessionID'=>$sessionID));
            $this->assertEquals(createFailRes(239), RequestSender::send($req));
            
            $req = TestData::fillRequest('AdjustPluginRequest', array('sessionID'=>$sessionID,
                                                                      'side'=>$side,
                                                                      'description'=>TestData::genUnicodeStr(42),
                                                                      'pluginAlias'=>TestData::genFileName(255),
                                                                      'pluginData'=>TestData::genUnicodeStr(42)));
            $this->assertEquals(createFailRes(239), RequestSender::send($req));
            
            $req = TestData::fillRequest('RemovePluginRequest', array('pluginAlias'=>TestData::genFileName(255), 
                                                                      'sessionID'=>$sessionID,
                                                                      'side'=>$side));
            $this->assertEquals(createFailRes(239), RequestSender::send($req));
        }
    }
    
    public function badFilenameProvider()
    {
        return TestData::getData('createPluginFilename');    
    }
    
    public function pluginSideProvider()
    {
        return array(array('Client'), array('Server'));    
    }
}

?>
