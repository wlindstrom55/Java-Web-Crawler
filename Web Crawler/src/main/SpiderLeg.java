package main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
//we'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser	

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>(); //just a list of urls
	private Document htmlDocument; //This is our web page, or in other words, our doc

	/** 
	 * crawl() performs all the work. It makes an HTTP request, checks the response, and then
	 * gathers up all the links on the page. Performs a searchForWord after the successful crawl
	 * @param nextUrl - the URL to visit
	 * @return whether or not the crawl was successful
	 */
		public boolean crawl(String url) {
			try {
				Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
				Document htmlDocument = connection.get();
				this.htmlDocument = htmlDocument;
				
				if(connection.response().statusCode() == 200) {
					System.out.println("\n**Visiting** Received web page at " + url);
				}
				
				if(!connection.response().contentType().contains("text/html")) {
					System.out.println("**Failure** Retrieved something other than HTML");
					return false;
				}
	
				Elements linksOnPage = htmlDocument.select("a[href]");
				System.out.println("Found (" + linksOnPage.size() + ") links");
				for(Element link : linksOnPage) { //stores the links in a private field within method
					this.links.add(link.absUrl("href"));
				}
				
				return true;
				
			} catch(IOException ioe) {
				//we were not successful in our HTTP request..
				System.out.println("Error in our HTTP request" + ioe);
				return false;
			}
		}
		/**
		 * Performs a search on the body of the HTML doc that is retrieved. This method 
		 * should only be called after a successful crawl.
		 * @param searchWord - The word or string to look for
		 * @return whether or not the word was found
		 */
		public boolean searchForWord(String searchWord) {
			//Defensive coding. This method should only be used after a successful crawl
			if(this.htmlDocument == null) {
				System.out.println("ERROR! Call crawl() before performing analysis on the document.");
				return false;
			}
			System.out.println("Searching for the word " + searchWord + "....");
			String bodyText = this.htmlDocument.body().text();
			return bodyText.toLowerCase().contains(searchWord.toLowerCase());
		}
		public List<String> getLinks(){
			//Returns a list of all the URLS on the page
			return this.links;
		}
}
