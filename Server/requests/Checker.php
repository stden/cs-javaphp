<?php
/**
 * Created by IDEA.
 * User: ilya
 * Date: 12.10.2010
 * Time: 23:26:24
 * To change this template use File | Settings | File Templates.
 */

function processCheckerRequest($request) {
    RequestUtils::testSession($request->sessionID);
}
