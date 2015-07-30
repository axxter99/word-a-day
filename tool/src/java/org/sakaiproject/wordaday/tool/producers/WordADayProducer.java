package org.sakaiproject.wordaday.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.news.api.NewsItem;
import org.sakaiproject.news.api.NewsService;
import org.sakaiproject.wordaday.model.WordADay;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

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

			if (word == null || "".equals(word) || word.getUpdated().before(check)) {
				List<NewsItem> items = newsService.getNewsitems(url);
				log.info("items:" + items.size());
				int k = items.size();
				for (int i = 0; i < k; i++) {
					NewsItem ni = items.get(i);
					word = new WordADay(ni.getTitle(), ni.getDescription());
					// i = items.get(i) + 1;
					UIBranchContainer row = UIBranchContainer.make(tofill, "user-row:");
					UIOutput.make(row, "word", word.getWord());
					UIVerbatim.make(row, "description", word.getDefinition());
					log.info(i);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
