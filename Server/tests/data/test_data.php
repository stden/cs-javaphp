<?php

define('GOOD_DATA', 1);
define('BAD_DATA', 0);

class TestData {

    const TIME_SCALE = 100;
    const RANDOM_TESTS_NUMBER = 20;
    const MAX_USER_DATA_FIELDS = 10;
    const MAX_DATA_LENGTH = 5;
    const MIN_LP_LENGTH = 255;
    const MAX_LP_LENGTH = 24;
    const ALLOWED_UNICODE_LP_LENGTH = 8;

    private static $data = array(

        'registrationType' => array('ByAdmins', 'Self'),
        'accessPermission' => array('FullAccess', 'NoAccess', 'OnlySelfResults'),

        'userTestData' => array('SuperAdmin' => array('admin', 'superpassword'),
            'ContestAdmin' => array('contestAdmin', 'pass'),
            'Participant' => array('participant', 'pass')),


        'resultsAccessPolicyData' => array(
            array(BAD_DATA, null, null, null),
            array(BAD_DATA, 42, 42, 42),
            array(BAD_DATA, '', '', ''),
            array(BAD_DATA, 42, '', null),
        ),

        'registrationTypeData' => array(
            array(BAD_DATA, null),
            array(BAD_DATA, 42),
            array(BAD_DATA, ''),
            array(GOOD_DATA, 'Self'),
            array(GOOD_DATA, 'ByAdmins')
        ),
        'regToContest' => array(

            // ======================== SuperAdmin ============================ //

            array(GOOD_DATA, 'SuperAdmin', 'SuperAdmin', 'new_admin', 'new_pass', 0), //ok
            array(BAD_DATA, 'SuperAdmin', 'SuperAdmin', 'admin', 'superpassword', 0, 17), //already registered
            array(BAD_DATA, 'SuperAdmin', 'SuperAdmin', null, 'pass', 0, 12), //bad login
            array(BAD_DATA, 'SuperAdmin', 'SuperAdmin', '', 'pass', 0, 12), //-||-
            array(BAD_DATA, 'SuperAdmin', 'SuperAdmin', 'new_admin', 'new_pass', 1, 18), //wrong contestID

            array(GOOD_DATA, 'SuperAdmin', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 1), //ok
            array(BAD_DATA, 'SuperAdmin', 'ContestAdmin', 'contestAdmin', 'pass', 1, 17), //already registered
            array(BAD_DATA, 'SuperAdmin', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 0, 14), //correct: wrong contestID
            array(BAD_DATA, 'SuperAdmin', 'ContestAdmin', '', 'pass', 1, 12), //bad login
            array(BAD_DATA, 'SuperAdmin', 'ContestAdmin', null, 'pass', 1, 12), //bad login

            array(GOOD_DATA, 'SuperAdmin', 'Participant', 'new_participant', 'new_pass', 1), //ok
            array(BAD_DATA, 'SuperAdmin', 'Participant', 'participant', 'pass', 1, 17), //already registered
            array(BAD_DATA, 'SuperAdmin', 'Participant', 'new_participant', 'new_pass', 0, 14), //correct: wrong contestID
            array(BAD_DATA, 'SuperAdmin', 'Participant', '', 'pass', 1, 12), //bad login
            array(BAD_DATA, 'SuperAdmin', 'Participant', null, 'pass', 1, 12), //bad login

            array(BAD_DATA, 'SuperAdmin', 'asdf', null, 'pass', 1), //bad whom user type


            // ======================== ContestAdmin ============================ //

            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', 'new_admin', 'new_pass', 0, '?'),
            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', 'new_admin', 'new_pass', 1, '?'),
            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', 'admin', 'superpassword', 0, '?'),
            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', null, 'pass', 0, '?'),
            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', '', 'pass', 0, '?'),
            array(BAD_DATA, 'ContestAdmin', 'SuperAdmin', 'new_admin', 'new_pass', 1, '?'),

            array(GOOD_DATA, 'ContestAdmin', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 1), //ok
            array(BAD_DATA, 'ContestAdmin', 'ContestAdmin', 'contestAdmin', 'pass', 1, 17), //already registered
            array(BAD_DATA, 'ContestAdmin', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 0, 14), //correct: wrong contestID
            array(BAD_DATA, 'ContestAdmin', 'ContestAdmin', '', 'pass', 1, 12), //bad login
            array(BAD_DATA, 'ContestAdmin', 'ContestAdmin', null, 'pass', 1, 12), //bad login
            array(BAD_DATA, 'ContestAdmin', 'ContestAdmin', 'new_admin', 'new_pass', 2, 0), //crosscontest registration

            array(GOOD_DATA, 'ContestAdmin', 'Participant', 'new_participant', 'new_pass', 1), //ok
            array(BAD_DATA, 'ContestAdmin', 'Participant', 'participant', 'pass', 1, 17), //already registered
            array(BAD_DATA, 'ContestAdmin', 'Participant', 'new_participant', 'new_pass', 0, 14), //correct: wrong contestID
            array(BAD_DATA, 'ContestAdmin', 'Participant', '', 'pass', 1, 12), //bad login
            array(BAD_DATA, 'ContestAdmin', 'Participant', null, 'pass', 1, 12), //bad login
            array(BAD_DATA, 'ContestAdmin', 'Participant', 'new_participant', 'new_pass', 2, 0), //crosscontest registration

            array(BAD_DATA, 'ContestAdmin', 'asdf', null, 'pass', 1), //bad whom user type


            // ======================== Participant ============================ //

            array(BAD_DATA, 'Participant', 'SuperAdmin', 'new_admin', 'new_pass', 0, 0),
            array(BAD_DATA, 'Participant', 'SuperAdmin', 'new_admin', 'new_pass', 1, 0),
            array(BAD_DATA, 'Participant', 'SuperAdmin', 'admin', 'superpassword', 0, 0),
            array(BAD_DATA, 'Participant', 'SuperAdmin', null, 'pass', 0, 0),
            array(BAD_DATA, 'Participant', 'SuperAdmin', '', 'pass', 0, 0),
            array(BAD_DATA, 'Participant', 'SuperAdmin', 'new_admin', 'new_pass', 1, 0),

            array(BAD_DATA, 'Participant', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 1, 0), //ok
            array(BAD_DATA, 'Participant', 'ContestAdmin', 'contestAdmin', 'pass', 1, 0), //already registered
            array(BAD_DATA, 'Participant', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 0, 0), //correct: wrong contestID
            array(BAD_DATA, 'Participant', 'ContestAdmin', '', 'pass', 1, 0), //bad login
            array(BAD_DATA, 'Participant', 'ContestAdmin', null, 'pass', 1, 0), //bad login

            array(BAD_DATA, 'Participant', 'Participant', 'new_participant', 'new_pass', 1, 0), //ok
            array(BAD_DATA, 'Participant', 'Participant', 'participant', 'pass', 1, 0), //already registered
            array(BAD_DATA, 'Participant', 'Participant', 'new_participant', 'new_pass', 0, 0), //correct: wrong contestID
            array(BAD_DATA, 'Participant', 'Participant', '', 'pass', 1, 0), //bad login
            array(BAD_DATA, 'Participant', 'Participant', null, 'pass', 1, 0), //bad login

            array(BAD_DATA, 'Participant', 'asdf', null, 'pass', 1, 0), //bad whom user type

            // ======================== Anonymous ============================ //

            array(BAD_DATA, 'Anonymous', 'SuperAdmin', 'new_admin', 'new_pass', 0, 0),
            array(BAD_DATA, 'Anonymous', 'SuperAdmin', 'new_admin', 'new_pass', 1, 0),
            array(BAD_DATA, 'Anonymous', 'SuperAdmin', 'admin', 'superpassword', 0, 0),
            array(BAD_DATA, 'Anonymous', 'SuperAdmin', null, 'pass', 0, 0),
            array(BAD_DATA, 'Anonymous', 'SuperAdmin', '', 'pass', 0, 0),
            array(BAD_DATA, 'Anonymous', 'SuperAdmin', 'new_admin', 'new_pass', 1, 0),

            array(BAD_DATA, 'Anonymous', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 1, 0), //ok
            array(BAD_DATA, 'Anonymous', 'ContestAdmin', 'contestAdmin', 'pass', 1, 0), //already registered
            array(BAD_DATA, 'Anonymous', 'ContestAdmin', 'new_contestAdmin', 'new_pass', 0, 0), //correct: wrong contestID
            array(BAD_DATA, 'Anonymous', 'ContestAdmin', '', 'pass', 1, 0), //bad login
            array(BAD_DATA, 'Anonymous', 'ContestAdmin', null, 'pass', 1, 0), //bad login

            array(GOOD_DATA, 'Anonymous', 'Participant', 'new_participant', 'new_pass', 2), //ok
            array(BAD_DATA, 'Anonymous', 'Participant', 'participant', 'pass', 2, 0), //already registered
            array(BAD_DATA, 'Anonymous', 'Participant', 'new_participant', 'new_pass', 1, 0), //wrong contest id
            array(BAD_DATA, 'Anonymous', 'Participant', 'participant', 'pass', 1, 0), //wrong contest id
            array(BAD_DATA, 'Anonymous', 'Participant', 'new_participant', 'new_pass', 0, 0), //correct: wrong contestID
            array(BAD_DATA, 'Anonymous', 'Participant', '', 'pass', 2, 0), //bad login
            array(BAD_DATA, 'Anonymous', 'Participant', null, 'pass', 2, 0), //bad login

            array(BAD_DATA, 'Anonymous', 'asdf', null, 'pass', 1, 0), //bad whom user type
        ),
        'disconnectContest' => array(
            array(GOOD_DATA, 'SuperAdmin'),
            array(GOOD_DATA, 'ContestAdmin'),
            array(GOOD_DATA, 'Participant'),
            array(BAD_DATA, 'Anonymous'),
            array(BAD_DATA, 'other', 'empty session id'),
            array(BAD_DATA, 'other', ''),
            array(BAD_DATA, 'other', 42),
        ),
        'createPluginFilename' => array(
            array(BAD_DATA, 'Client', 'a?b'),
            array(BAD_DATA, 'Server', 'a?b'),

            array(BAD_DATA, 'Client', 'a/b'),
            array(BAD_DATA, 'Server', 'a/b'),

            array(BAD_DATA, 'Client', 'a"b'),
            array(BAD_DATA, 'Server', 'a"b'),

            array(BAD_DATA, 'Client', 'a*b'),
            array(BAD_DATA, 'Server', 'a*b'),

            array(BAD_DATA, 'Client', 'a\\b'),
            array(BAD_DATA, 'Server', 'a\\b'),

            array(BAD_DATA, 'Client', 'a|b'),
            array(BAD_DATA, 'Server', 'a|b'),

            array(BAD_DATA, 'Client', 'a:b'),
            array(BAD_DATA, 'Server', 'a:b'),

            array(BAD_DATA, 'Client', 'a<b'),
            array(BAD_DATA, 'Server', 'a<b'),

            array(BAD_DATA, 'Client', 'a>b'),
            array(BAD_DATA, 'Server', 'a>b')
        ),
    );

    public static function getData($name) {
        return TestData::$data[$name];
    }

    public static function getRandomValue($ar) {
        return $ar[rand(0, sizeof($ar) - 1)];
    }

    public static function genUnicodeStr($length) {
        $res = '';

        for ($i = 0; $i < $length; $i++)
            $res .= mb_convert_encoding('&#' . rand(32, 65535) . ';', 'UTF-8', 'HTML-ENTITIES');

        return $res;
    }

    public static function genASCIIStr($length) {
        $res = '';

        for ($i = 0; $i < $length; $i++)
            $res .= chr(rand(32, 127));

        return $res;
    }

    public static function genFileName($length) {
        $res = '';

        $i = 0;
        while ($i < $length)
        {
            switch (rand(1, 2)) {
                case 1:
                    $ch = rand(ord('a'), ord('z'));
                    break;
                case 2:
                    $ch = rand(ord('A'), ord('Z'));
                    break;
                //TODO generate russian, greek and other L-class UNICODE letters
                /*   case 3:
                $ch = rand(ord('А'), ord('Я'));
                break;
            case 4:
                $ch = rand(ord('а'), ord('я'));
                break;*/
            }

            if (FALSE !== array_search($ch, array(ord('?'), ord('\\'), ord('/'), ord('|'), ord('*'), ord('"'), ord('<'), ord('>'), ord('?'), ord('?'), ord(':'))))
                continue;

            $res .= chr($ch);
            $i++;
        }

        return $res;
    }

    public static function gB() {
        return rand(0, 1) ? TRUE : FALSE;
    }

    public static function genStrArray($size, $length) {
        $res = array();

        for ($i = 0; $i < $size; $i++)
            $res[] = TestData::genUnicodeStr($length);

        return $res;
    }

    public static function genContestDecription($isSelfStart = false) {
        $cd = new ContestDescription();

        //create contest descr

        $cd->name = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
        $cd->description = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);

        $int = time() / TestData::TIME_SCALE;

        $cd->start = time() + rand(-$int, $int);
        $cd->finish = $cd->start + rand(1, $int);
        $cd->registrationType = TestData::getRandomValue(TestData::getData('registrationType'));

        $ct = new ContestTiming();
        $ct->contestEndingStart = rand(1, ($cd->finish - $cd->start) / 60);
        $ct->contestEndingFinish = rand(1, $int);
        $ct->maxContestDuration = rand(1, $isSelfStart ? $cd->finish - $cd->start : $int);
        $ct->selfContestStart = $isSelfStart;

        $cd->contestTiming = $ct;

        $udfs = array();
        for ($j = 0; $j < rand(1, TestData::MAX_USER_DATA_FIELDS); $j++)
        {
            $udf = new UserDataField();

            $udf->compulsory = rand(0, 1) ? true : false;
            $udf->data = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
            $udf->showInResult = rand(0, 1) ? true : false;

            $udfs[] = $udf;
        }

        $cd->data = $udfs;

        $ar = TestData::getData('accessPermission');

        $rap = new ResultsAccessPolicy();
        $rap->afterContestPermission = TestData::getRandomValue($ar);
        $rap->contestEndingPermission = TestData::getRandomValue($ar);
        $rap->contestPermission = TestData::getRandomValue($ar);

        $cd->resultsAccessPolicy = $rap;

        return $cd;
    }

    public static function genUserDescription($dataFieldsQnt) {

        if ($dataFieldsQnt < 0) throw new InvalidArgumentException();

        $ud = new UserDescription();

        $ud->login = TestData::genUnicodeStr(TestData::ALLOWED_UNICODE_LP_LENGTH);
        $ud->password = TestData::genUnicodeStr(TestData::ALLOWED_UNICODE_LP_LENGTH);
        $ud->userType = TestData::getRandomValue(array('Participant', 'ContestAdmin'));

        for ($i = 0; $i < $dataFieldsQnt; $i++)
            $ud->dataValue[] = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);

        return $ud;
    }

    public static function fillRequest($name, $params = array()) {
        $req = new $name();

        foreach ($params as $field => $value)
            $req->$field = $value;

        return $req;
    }
}

?>