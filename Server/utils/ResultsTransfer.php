<?php

/**
 * Created by IDEA.
 * User: ilya
 * Date: 18.10.2010
 * Time: 14:29:14
 * To change this template use File | Settings | File Templates.
 */

class ResultsTransfer {

    private $settings;

    public function __construct($settings) {
        if (is_null($settings) || !is_string($settings) || trim($settings) === '')
            $this->settings = null;
        else
            $this->settings = $settings;
    }

/**
     * Returns result for result table
     * @static
     * @param  $result result to convert
     * @param  $conversionSettings settings for conversion
     * @return array entry for results table */
    public function convert($res) {

        if ($this->settings) {
            $rt = array();
            eval($this->settings);
        } else
            $rt = $this->defaultConversion($res);

        return $rt;
    }

    private function defaultConversion($res) {
        $rt = array();

        require_once 'utils/Keys.php';

        //transfer transferable keys
        $cnt = 0;
        foreach ($res as $key => $value)
            if (in_array($key, TRANSFERABLE_KEYS)) {
                $rt[$key] = $value;
                $cnt++;
            }

        if ($cnt === 0)
            $rt = $res;

        return $rt;
    }

    public function getResultKeys($cols) {
        $r = array();
        foreach ($cols as $c)
            $r[$c] = 0;
        $cr = $this->convert($r);
        return array_keys($cr);
    }

}
 
