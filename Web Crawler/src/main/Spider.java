package main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Spider {
//Fields
	private static final int MAX_PAGES_TO_SEARCH = 10;
	private Set<String> pagesVisited = new HashSet<String>(); 
	/**
	 * -Why a set for pagesVisited? A set by definition contains unique entries.
	 * In other words, no duplicates. All the pages we visit will be 
	 * unique (or at least their URL).
	 * -Why a list for pagesToVisit? This is just storing a bunch of URLS we
	 * have to visit next. When the crawler visits a page it collects all
	 * the URLS on that page and we just append them to this list. Recall
	 * that Lists have special methods that Sets ordinarily do not, such
	 * as adding an entry to the end of a list or adding an entry to the
	 * beginning of a list.
	 */
	private List<String> pagesToVisit = new LinkedList<String>();
	
	/* Our main launching point for the Spider's functionality. Internally, it creates spider legs
	//that make an HTTP request and parse the response
	//the "url" param is the starting point of the spider, the "searchWord" is the word or String
	//you are searching for.
	 */
	public void search(String url, String searchWord) {
		while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) { //while pages visited is under 10,
			String currentUrl; //init of currentUrl,
			SpiderLeg leg = new SpiderLeg(); //instanciates a new leg,
			if(this.pagesToVisit.isEmpty()) { //if there are no pages to visit, currenturl
				//= starting url, and url is added to pages visited.
				currentUrl = url;
				this.pagesVisited.add(url);
			} else { //else, currenturl gets switched to the next one
				currentUrl = this.nextUrl();
			}
		leg.crawl(currentUrl); //Lots of stuff happening here. Look at the crawl method in Spiderleg
		boolean success = leg.searchForWord(searchWord);
		if(success) {
			System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
			break;
		}
		this.pagesToVisit.addAll(leg.getLinks());
		}
		System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
	}
	
	//Returns the next URL to visit (in the order that they were found). We also do a check to make
	//sure this method doesn't return a URL that has already been visited.
		private String nextUrl() {
			String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while(this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
		}
}
