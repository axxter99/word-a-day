package org.sakaiproject.wordaday.model;

import java.util.Date;

/**
 * @author dhorwitz
 *
 */
public class WordADay {

	private String word;
	private String definition;
	private Date updated;

	/*
	 * 
	 * constructors
	 */

	public WordADay(String w, String def) {
		this.word = w;
		this.definition = def;
		this.updated = new Date();

	}

	/**
	 * @return
	 */
	public String getWord() {
		return this.word;
	}

	
	
	public void setWord(String wordi) {
		word = wordi;

	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String def) {
		definition = def;
	}

	public void setUpdated(Date date) {
		updated = date;
	}

	
	public Date getUpdated() {
		return updated;
	}

}
