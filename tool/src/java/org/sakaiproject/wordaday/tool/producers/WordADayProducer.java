package org.sakaiproject.wordaday.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.news.api.NewsItem;
import org.sakaiproject.news.api.NewsService;
import org.sakaiproject.wordaday.model.WikiQuoteAPI;
import org.sakaiproject.wordaday.model.WordADay;
import org.sakaiproject.wordaday.tool.util.WikiQuoteImpl;

import info.bliki.api.Connector;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordADayProducer implements DefaultView, ViewComponentProducer {

	public static final String VIEW_ID = "wordaday";
	private WordADay word;
	private NewsItem newsItem;

	private static Log log = LogFactory.getLog(WordADay.class);

	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	private String url;

	public void setUrl(String u) {
		url = u;
	}

	private NewsService newsService;

	public void setNewsService(NewsService ns) {
		newsService = ns;

	}

	private WikiQuoteAPI wikiQuote;
	
	public WikiQuoteAPI getWikiQuote() {
		return wikiQuote;
	}

	// we need an init method
	public void init() {
		//

	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		// TODO Auto-generated method stub
		// for now just create the Word
		// NewsChannel nc =
		// newsService.getChannel("http://www.oed.com/rss.xml");
		try {
			// joda-time?
			Date check = new Date(System.currentTimeMillis());
			
			log.info("Date: " + check.toString());
			//List<NewsItem> items = newsService.getNewsitems(url);
			List<WordADay> items = wikiQuote();
			//if (items == null || "".equals(items) || word.getUpdated().before(check)) {
				
				log.info("items:" + items.size());
				int k = items.size();
				for (int i = 0; i < k; i++) {
					WordADay word = items.get(i);
					//word = new WordADay(ni.getTitle(), ni.getDescription());
					// i = items.get(i) + 1;
					UIBranchContainer row = UIBranchContainer.make(tofill, "user-row:");
					UIOutput.make(row, "word", word.getWord());
					UIVerbatim.make(row, "description", word.getDefinition());
					log.info(i);
				}
			//}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String apiUrl = "https://en.wikiquote.org/w/api.php"; // "https://en.wikipedia.org/w/api.php";

	private static String userWiki = "";
	private static String password = "";
	
	public List<WordADay> wikiQuote() {
		log.info("wiki!");

		String titleURL = Encoder.encodeTitleLocalUrl("Archaeology");
		User user = new User(userWiki, password, apiUrl);
		user.login();

		log.info(user.getDomain() + ":" + user.getActionUrl());

		Connector connector = new Connector();

		List<String> listOfTitleStrings = new ArrayList<String>();
		listOfTitleStrings.add(titleURL);
		//listOfTitleStrings.add("Desmond Tutu");

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
		List<WordADay> news = new ArrayList<WordADay>();
		for (int i = 0; i < k; i++) {
			String n = quoteP.get(i);
			//word = new WordADay(ni.getTitle(), ni.getDescription());
			//BasicNewsItem nu = new BasicNewsItem("q", n, "wike", "puc");
			WordADay day = new WordADay(titleURL, n);
			news.add(day);
		}
		return news;
	}
}
