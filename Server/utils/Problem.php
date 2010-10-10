<?php

class Problem {
	
	private $contents = array(); //path to contents
	private $isTeacher = array(); //array of teacher pathes
	private $alias = array(); //alias to real name
		
	private $fname;
	
	//parser 
	private $cur_element;
	private $cur_path;

	public function __construct($fname) {
		$this->fname = $fname;
		
		$zip = new ZipArchive;
		if (!$zip->open($fname))
			throw new Exception("Failed to open zip with problem");
			
	 	//get manifest
		$manifest = $zip->getFromName("MANIFEST");
		
		//parse manifest
		$parser = xml_parser_create();
		xml_set_element_handler(
			$parser,
			array($this, 'start_element_handler'),
			array($this, 'end_element_handler')
		);
		
		xml_set_character_data_handler(
			$parser,
			array($this, 'chars_handler')			
		);
		
		xml_parse($parser, $manifest, /*is final*/true);
			
		$zip->close();
	}	
	
    private function start_element_handler($parser, $name, $attrs) {
    	$this->cur_element = $name; //works because there is only one level of tags 
		switch ($name) {
			case 'CONTENTS':
				if (array_key_exists('PATH', $attrs))
					$this->cur_path = $attrs['PATH'];
				else
					$this->cur_path = 'UNKNOWN'; //should not occur
				$this->contents[$this->cur_path] = '';
				break;
			case 'ALIAS':
				if (array_key_exists('PATH', $attrs))
					$path = $attrs['PATH'];
				else
					$path = 'UNKNOWN'; //should not occur by the way
				if (array_key_exists('TARGET', $attrs))
					$target = $attrs['TARGET'];
				else
					$target = 'UNKNOWN';
				$this->alias[$path] = $target;
				break;
			case 'TEACHER':
				if (array_key_exists('PATH', $attrs))
					$path = $attrs['PATH'];
				else
					$path = 'UNKNOWN'; //should not occur				
				$this->isTeacher[] = $path;
				break;
		}	
	}
	
	private function end_element_handler($parser, $name) {
		$this->cur_element = false;
	}
	
	private function chars_handler($parser, $data) {
		if ($this->cur_element === 'CONTENTS')
			$this->contents[$this->cur_path] .= $data;
	}
	
	public function getResource($path) {
		if (array_key_exists($path, $this->alias))
			$path = $this->alias[$path];
		if (array_key_exists($path, $this->contents))
			return $this->contents[$path];
			
		$zip = new ZipArchive;
		$zip->open($this->fname);
		$data = $zip->getFromName($path);
		$zip->close();		
		return $data;
	}
	
	public function isTeacher($path) {
		return array_search($path, $this->isTeacher) !== false;
	}
	
	function getServerPlugin() {
		return $this->getResource('SERVER PLUGIN');
	}
	
	function getClientPlugin() {
		return $this->getResource('CLIENT PLUGIN');
	}

	function getProblemBytes() {
		return @file_get_contents($this->fname);
	}
	
	function getAnswer() {
		return $this->getResource('ANSWER');
	}
	
	function getName() {
		return $this->getResource('NAME');
	}
	
	/* returns Problem. This is a variant that is sent to participant */
	function getParticipantVersion($user_id) {
		//TODO implement generation of statement, removing of teacher resources
		return this;
	}
	
	function getAnswerVersion($user_id) {
		//TODO implement generation
		return this;
	}

}

?>