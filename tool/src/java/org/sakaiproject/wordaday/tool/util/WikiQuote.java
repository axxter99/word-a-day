package org.sakaiproject.wordaday.tool.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.news.api.NewsItem;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.api.Connector;
import info.bliki.api.Page;


public class WikiQuote {
	
	private static Log log = LogFactory.getLog(WikiQuote.class);
	private static String apiUrl = "https://en.wikiquote.org/w/api.php";// "https://en.wikipedia.org/w/api.php";

	private static String userWiki = "";
	private static String password = "";

	public List<NewsItem> wikiQuote() {
		
		log.info("wiki!");

		String titleURL = Encoder.encodeTitleLocalUrl("Archaeology");
		User user = new User(userWiki, password, apiUrl);
		user.login();

		log.info(user.getDomain() + ":" + user.getActionUrl());

		Connector connector = new Connector();

		List<String> listOfTitleStrings = new ArrayList<String>();
		listOfTitleStrings.add(titleURL);
		listOfTitleStrings.add("Desmond Tutu");

		List<Page> page = connector.queryContent(user, listOfTitleStrings);
		List<String> quoteP = new ArrayList<String>();
		for (int i = 0; i < page.size(); i++) {
			Page p = page.get(i);
			String pS = p.getCurrentRevision().getContent();
			// log.info(pS);

			int paxt = pS.indexOf("*");

			pS = pS.substring(paxt);
			int pLiner = pS.indexOf("\n");
			int pZero = 0;
			pS = pS.trim();
			log.info("*:" + paxt + ", n: " + pLiner + ", line: " + pS.length());
			// log.info(pS);
			
			for (int q = 0; q < pS.length(); q++) {

				if (pLiner >= 0) {
					log.info(q + ",*:" + paxt + ", n: " + pLiner + ", line: " + pS.length());
					// pliner = null

					String quote = pS.substring(pZero, pLiner);
					// log.info(quote);

					if (quote.indexOf("*") > -1) {
						if (quote.indexOf("**") > -1) {

						} else {
							quote = quote.substring(quote.indexOf("*") + 1);
							quote = quote.trim();
							quoteP.add(quote);
							log.info("quote; \"" + quote + "\"");
						}
					}

					paxt = pS.indexOf("* ", paxt);

					// q = paxt;

					log.info("lenght!");
					pZero = pLiner;
					pLiner = pS.indexOf("\n", pLiner + 1);

					log.info("---00000000000----------");
				}

			}
			
		}
		log.info(quoteP.size());
		int k = quoteP.size();
		List<NewsItem> news = new ArrayList<NewsItem>();
		for (int i = 0; i < k; i++) {
			String n = quoteP.get(i);
			//word = new WordADay(ni.getTitle(), ni.getDescription());
			BasicNewsItem nu = new BasicNewsItem("q", n, "wike", "puc");
			news.add(nu);
		}
		return news;
	}

}
