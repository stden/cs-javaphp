<?php

//this plugin doesn't use a plugin folder to store any info

class ComparePlugin extends ServerPlugin {    

    public function checkSolution($solution) {
        $accepted = $solution === $this->problem->getAnswer();
        return array('accepted' => $accepted ? 1 : 0);
    }

    public function compareResults($res1, $res2) {        
        return $res1['accepted'] - $res2['accepted'];
    }
}

?>