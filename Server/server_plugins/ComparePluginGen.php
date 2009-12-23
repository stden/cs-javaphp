<?php

//this plugin doesn't use a plugin folder to store any info

class ComparePluginGen extends ServerPlugin {

	private function getFileIndByUser($user_id, $num_files) {
		$fld = 0;
		for ($i = 0; $i < strlen($this->folder); $i++)
		$fld += ord($this->folder[$i]);
		return ($user_id + $fld) % $num_files;
	}

	private function prepareString($s) {
		$s = strtolower($s);
		$s = preg_replace('/ /', '', $s);
	}
	
	private function getRightAnswer($answer_data, $user_id) {
		$ind = $this->getFileIndByUser($user_id, $answer_data['num_probs']);
		return $answer_data["q_$ind"];
	}

	private function compareAns($ans, $rans) {		
		$ans = $this->prepareString($ans);
		$rans = $this->prepareString($rans);

		return $ans === $rans;
	}

	public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {
		$res = array();
		$res['result'] = "no submissions left";

		//DEBUG HACK
		if ($solution['answer'] === '___clear') {
			$current_result['subs'] = 0;
			$current_result['chk'] = "-";
			return $res;
		}
			
		if ($current_result['chk'] === "+")
		return $res;

		if (is_null($current_result))
		$subs = 1;
		else
		$subs = $current_result['subs'] + 1;

		if ($subs > 2)
		return $res;

		$ans = $solution['answer'];
		$rans = $this->getRightAnswer($answer_data, $user_id);
		if ($this->compareAns($ans, $rans))
			$chk = "+";
		else
			$chk = "-";

		$table_cols = array($ans, $rans, $chk, $subs,
			$this->getFileIndByUser($user_id, $answer_data['num_probs'])
		);
		$res['result'] = $chk === "+" ? "answer accepted" : "wrong answer";

		$current_result['chk'] = $chk;
		$current_result['subs'] = $subs;

		return $res;
	}

	public function updateStatementData($statement_zip) {
		//iterate files in archive and zip them in the problem folder
		$ind = 0;
		while ($file = $statement_zip->getFromName("$ind.html"))
		{
			$zip = new ZipArchive();
			if (! $zip->open($this->folder . '/' . $ind . '.zip', ZIPARCHIVE::CREATE)) {
				die('[' . $this->folder . '/' . $ind . '.zip]');
				return false;
			}
			$zip->addFromString("statement.html", $file);
			$zip->close();
			$ind ++;
		}
		return $ind;
	}

	public function updateAnswerData($answer_zip) {
		//$p = $answer_zip->getFromName('answer.txt');
		$p = $answer_zip->getFromIndex(0);
		if (! $p) return false;

		$ad = array();

		$ans = "";
		$ind = 0;
		for ($i = 0; $i < strlen($p); $i++) {
			if ($p[$i] == ';') {
				$ad["q_$ind"] = $ans;
				$ans = "";
				$ind ++;
			} else {
				$ans .= $p[$i];
			}
		}

		$ad['num_probs'] = $ind;
			
		return $ad;
	}

	public function getStatement($user_id, $statement_data) {
		//statement_data = number of files
		$ind = $this->getFileIndByUser($user_id, $statement_data);
		return @file_get_contents($this->folder . '/' . $ind . '.zip');
	}

	public function getColumnNames($statementData) {
		return array("answer", "right_answer", "accepted", "submissions", "ind");
	}

}

?>