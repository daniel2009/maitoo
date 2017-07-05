package com.meitao.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;
import com.meitao.model.SpiderOrder;

public class HtmlSpiderUtil {
	public static final String WEBSITE_AMAZON = "amazon.com";
	public static final String WEBSITE_6PM = "6pm.com";
	public static final String WEBSITE_MACYS = "macys.com";
	public static final String WEBSITE_NORDSTROM = "nordstrom.com";
	public static final String WEBSITE_BLOOMINGDALES = "bloomingdales.com";
	public static final String WEBSITE_LEVI = "levi.com";
	public static final String WEBSITE_LACOSTE = "lacoste.com";
	public static final String WEBSITE_JOMASHOP = "jomashop.com";
	public static final String WEBSITE_GNC = "gnc.com";
	public static final String WEBSITE_GROUPON = "groupon.com";
	public static final String WEBSITE_WALMART = "walmart.com";
	public static final String WEBSITE_ABERCROMBIE = "abercrombie.com";
	public static final String WEBSITE_ABERCROMBIE_CN = "abercrombie.cn";
	public static final String WEBSITE_COSTCO = "costco.com";
	public static final String WEBSITE_HOLLISTERCO = "hollisterco.com";
	public static final String WEBSITE_HOLLISTERCO_CN = "hollisterco.cn";
	public static final String WEBSITE_AE = "ae.com";
	public static final String WEBSITE_UGGAUSTRALIA = "uggaustralia.com";
	public static final String WEBSITE_UGGAUSTRALIA_CN = "ugg.cn";
	public static final String WEBSITE_EBAY = "ebay.com";
	public static final String WEBSITE_PETERTHOMASROTH = "peterthomasroth.com";
	public static final String WEBSITE_CHARLOTTEOLYMPIA = "charlotteolympia.com";
	public static final String WEBSITE_CARTERS = "carters.com";
	public static final String WEBSITE_STUARTWEITZMAN = "stuartweitzman.com";
	public static final String WEBSITE_GMPVITAS = "gmpvitas.com";
	
	private static final String[][] PRICE_TYPE = {{"RMB", "￥", "¥", "CNY"}, {"USD", "$", "US"}, {"EUR", "€"}};
	
	public static SpiderOrder search(String urlString){
		SpiderOrder spiderOrder = null;
		if(!StringUtil.isEmpty(urlString) && (0 == urlString.indexOf("http://") || 0 == urlString.indexOf("https://"))){
			
		}
		
		
	
		return spiderOrder;
	}
	
	public static String getWebsite(String urlString){
		String website = "";
		Pattern pattern = Pattern.compile("[\\w-]+\\.(com|net|org|gov|cc|biz|info|cn|co)\\b()*");
		Matcher matcher = pattern.matcher(urlString);
		if(matcher.find()){
			website = matcher.group();
		}
		return website;
	}
	
	public static Element getElement(Document document, String... cssQuerys){
		Element element = null;
		for(String cssQuery : cssQuerys){
			Elements elements = document.select(cssQuery);
			if(elements.size() > 0){
				element = elements.first();
				break;
			}
		}
		if(null == element){
			element = new Element(Tag.valueOf("div"), "");
		}
		return element;
	}
	public static Elements getElements(Document document, String... cssQuerys){
		Elements elements = null;
		for(String cssQuery : cssQuerys){
			elements = document.select(cssQuery);
		}
		return elements;
	}
	public static String[] getAttrs(Elements elements, String attr){
		String[] array = new String[5];
		for(int i=0; i<elements.size() && i<5; i++){
			array[i] = elements.get(i).attr(attr);
		}
		return array;
	}
	
	public static String getText(Document document, String... cssQuery){
		String result = "";
		Element element = getElement(document, cssQuery);
		if(null != element){
			result = element.text();
		}
		return result;
	}
	public static String getHtml(Document document, String... cssQuery){
		String result = "";
		Element element = getElement(document, cssQuery);
		if(null != element){
			element.select("a").attr("target", "view_window");
			result = element.html();
		}
		return result;
	}
	public static String[] separatePriceInfo(String priceWithCurrency){
		String[] result = {"", ""};
		finished : for(int i=0; i<PRICE_TYPE.length; i++){
			for(int j=0; j<PRICE_TYPE[i].length; j++){
				int index = priceWithCurrency.indexOf(PRICE_TYPE[i][j]);
				if(index >= 0){
					//Matcher matcher = Pattern.compile("[^0-9\\.]").matcher(priceWithCurrency);
					
					//result[1] = matcher.replaceAll("").trim();
					
					//只去掉钱符号，其它的不去掉，便于判断提取是否正确
					
							//int index1 = priceWithCurrency.indexOf(PRICE_TYPE[i][j]);
						
							priceWithCurrency=priceWithCurrency.replace(PRICE_TYPE[i][j], "");
							
							
						
					
					
					
					
					
					result[1] = priceWithCurrency.trim();
					result[0] = PRICE_TYPE[i][0];
					break finished;
				}
			}
		}
		return result;
	}
	
	private static String getHtmlByHttpClient(String urlString) throws ClientProtocolException, IOException {
		String html = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(urlString);
		HttpHost proxy = new HttpHost("127.0.0.1", 1080);//代理
		httpGet.setConfig(RequestConfig.custom().setProxy(proxy).build());//代理
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
				html = EntityUtils.toString(httpEntity);
			}
		}
		
		return html;
	}
	private static String getHtmlByHtmlUnit(String urlString) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException{
		String html = "";
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setProxyConfig(new ProxyConfig("127.0.0.1", 1080));//设置代理
		webClient.setWebConnection(new FalsifyingWebConnection(webClient) {
			@Override
			public WebResponse getResponse(WebRequest webRequest) throws IOException {
				WebResponse response=super.getResponse(webRequest);
		        if(response.getWebRequest().getUrl().toString().endsWith("http://www.ugg.cn/public/js/plugins.js")){//加载这个js会导致假死
		            return createWebResponse(response.getWebRequest(), "", "application/javascript", 200, "Ok");
		        }
		        return super.getResponse(webRequest);
			}
		});
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setCssEnabled(false);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.setTimeout(10000);
		HtmlPage htmlPage = webClient.getPage(urlString);
		Thread.sleep(5000);
		html = htmlPage.asXml();
		webClient.closeAllWindows();
		return html;
	}
	
	private static SpiderOrder searchByAmazonSpider(String urlString) {
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = HtmlSpiderUtil.getText(document, "#productTitle");
			String brand = HtmlSpiderUtil.getText(document, "#brand");
			String[] priceInfo = separatePriceInfo(HtmlSpiderUtil.getText(document, "#priceblock_ourprice", "#priceblock_dealprice"));
		
			
			
			String shippingPrice = "";
			String landingImage = getElement(document, "#landingImage", "imgBlkFront").attr("src");
			String[] smallImages = getAttrs(getElements(document, "li[class=a-spacing-small item] img[src]"), "src");
			List<String> list = new ArrayList<String>();
			list.add(landingImage);
			for(int i=1; i<smallImages.length; i++){
				list.add(smallImages[i]);
			}
			String featureBullets = getHtml(document, "#feature-bullets");
			String productDetailTable = getText(document, "#detail-bullets", "#productDetailsTable");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(list.toArray(new String[list.size()]));
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchBy6pmSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = document.select("a[itemprop=name]").text();
			String brand = document.select("a[itemprop=brand]").text();
			String[] priceInfo = separatePriceInfo(HtmlSpiderUtil.getElement(document, "#priceSlot .price").text());
			String shippingPrice = "";
			String landingImages = document.getElementById("detailImage").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "#prdInfoText");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByMacysSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "#productTitle");
			String brand = "";
			String[] priceInfo = separatePriceInfo(document.getElementById("priceInfo").select("span").last().text());
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "#mainView_1", ".productImageSection img").attr("src");
			String featureBullets = getText(document, "#longDescription");
			String productDetailTable = getText(document, "#bullets");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByNordstromSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getText(document, "section[class=product-title]");
			String brand = getText(document, "section[class=brand-title]");
			String[] priceInfo = separatePriceInfo(getText(document, "div[class=price-display-item regular-price]"));
			String shippingPrice = "";
			String landingImages = getElement(document, "#mainView_1", ".productImageSection img").attr("src");
			String featureBullets = getHtml(document, "section[size-bubble]");
			String productDetailTable = getHtml(document, "div[class=product-details-and-care]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		}// catch //(IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByBloomingdalesSpider(String urlString) {
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getHtml(document, "#priceTitle");
			String brand = getHtml(document, "#brandNameLink");
			String[] priceInfo = separatePriceInfo(getElement(document, "#PriceDisplay span[itemprop=price]", "#orgPrice").html());
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "#productImage").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, ".pdp_longDescription[itemprop=description]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByLeviSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getText(document, "h1[itemprop=name]");
			String brand = "Levi's";
			String[] priceInfo = separatePriceInfo(getText(document, "div[class=buystack-prices]"));
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "img[class=hero-img]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "li[class=fabric]");
			String productDetailTable = HtmlSpiderUtil.getHtml(document, "div[class=pdp-description]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		}// catch (IOException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByLacosteSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHttpClient(urlString));//.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[class=sku-product-name]");
			String brand = "LACOSTE";
			String firstColorUrl = getElement(document, "ul[class=product-colors sku-product-colors] li a[class=swatchanchor]").attr("data-href");
			Document priceDocument = Jsoup.parse(getHtmlByHttpClient(firstColorUrl));
			String[] priceInfo = separatePriceInfo(getText(priceDocument, ".price-sales"));
			String shippingPrice = getText(document, ".pdp-shipping span");
			String landingImages = HtmlSpiderUtil.getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "div[class=product-infos-content-more form]");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}

	private static SpiderOrder searchByJomashopSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "span[class=product-name]");
			String brand = getText(document, "span[itemprop=brand manufacturer]");
			String[] priceInfo = separatePriceInfo(getElement(document, "meta[itemprop=price]").attr("content"));
			String shippingPrice = getText(document, ".pdp-shipping span");
			String landingImages = HtmlSpiderUtil.getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "div[itemporp=description]");
			String productDetailTable = HtmlSpiderUtil.getHtml(document, "div[class=product-attributes]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByGncSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "#productTitle");
			String brand = "GNC";
			String[] priceInfo = separatePriceInfo(getText(document, "#product-info .now"));
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "img[class=prod-image]").attr("data-enh");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "#tab-description-content");
			String productDetailTable = HtmlSpiderUtil.getHtml(document, "#tab-supplement-facts-content");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByGrouponSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[itemprop=name]");
			String brand = "";
			String[] priceInfo = separatePriceInfo(getText(document, "span[class=price]"));
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "img[id^=gallery-image]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "div[itemprop=description]");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByWalmartSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[itemprop=name]");
			String brand = getText(document, "span[itemprop=brand]");
			String[] priceInfo = separatePriceInfo(getText(document, "span[itemprop=price][class=js-price-display Price Price--flair Price--medium hide-content-m price-display]"));
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "section[class=product-about js-about-item]");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByAbercrombieSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getText(document, "h1[itemprop=name]");
 			String brand = "Abercrombie & Fitch";
			String[] priceInfo = separatePriceInfo(getText(document, "span[class=text price-sales]"));
			String shippingPrice = "";
			String landingImages = getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = getHtml(document, "#tab-data2");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		}// catch (IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByCostcoSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getText(document, "h1[itemprop=name]");
 			String brand = "";
			String[] priceInfo = separatePriceInfo(getElement(document, "hidden[name=price]").val());
			String shippingPrice = "";
			String landingImages = getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = getHtml(document, "div[class=feature]");
			String productDetailTable = getHtml(document, "#product-tab1");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} //catch (IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByHollistercoSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));//Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(60000).post();//Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getText(document, "h1[itemprop=name]");
			name = getElement(document, "meta").attr("content");
 			String brand = "HOLLISTER";
			String[] priceInfo = separatePriceInfo(getText(document, "span[class=product-price-v2__price product-price-v2__price--list]", "span[class=text price-sales]"));
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "img[class=product__images--image]", "img[itemprop=image]").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "section[class=product__details]", "#tab-data2");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} //catch (IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	
	private static SpiderOrder searchByUggaustraliaSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));
			String name = getElement(document, "h1[class=product-name]").ownText();
 			String brand = "UGG";
			String[] priceInfo = {getElement(document, "meta[property=og:price:currency]").attr("content"), getElement(document, "meta[property=og:price:amount]").attr("content")};
			String shippingPrice = "";
			String landingImages = HtmlSpiderUtil.getElement(document, "a[class=thumbnail-link] img").attr("src");
			String featureBullets = HtmlSpiderUtil.getHtml(document, "#goodsDetailMain");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} //catch (IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByEbaySpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getElement(document, "#itemTitle").ownText();
 			String brand = getText(document, "h2[itemprop=brand]");
			String[] priceInfo = separatePriceInfo(getText(document, "#prcIsum"));
			String shippingPrice = "";
			String landingImages = getElement(document, "#icImg").attr("src");
			String featureBullets = getElement(document, "#itmSellerDesc").siblingElements().get(1).outerHtml();
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByPeterthomasrothSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "div[class=ProductName]");
 			String brand = "PETERTHOMASROTH";
			String[] priceInfo = separatePriceInfo(getText(document, "span[class=ProductPrice]"));
			String shippingPrice = "";
			String landingImages = getElement(document, "div[class=ProductImagesHolder] img").attr("src");
			String featureBullets = getHtml(document, "div[class=ProducLongDesc]");
			String productDetailTable = getHtml(document, "#tabs-1");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByCharlotteolympiaSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.parse(getHtmlByHtmlUnit(urlString));//.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[itemprop=name]");
 			String brand = "";
			String[] priceInfo = separatePriceInfo(getText(document, "div[class=product-price]"));
			String shippingPrice = "";
			String imageString = getElement(document, "img[class=productthumbnail]").attr("data-lgimg");
			JSONObject jsonObject = JSONObject.fromObject(imageString);
			String landingImages = jsonObject.getString("url");
			String featureBullets = getHtml(document, "div[class=product-tabs]");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} //catch (IOException | FailingHttpStatusCodeException | InterruptedException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByCartersSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[itemprop=name]");
 			String brand = "";
			String[] priceInfo = separatePriceInfo(getText(document, "span[itemprop=price]"));
			String shippingPrice = "";
			String landingImages = getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = getHtml(document, "#tab2");
			String productDetailTable = getHtml(document, "div[class=additional]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByStuartweitzmanSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[itemprop=name]");
 			String brand = "STUART WEITZMAN";
			String[] priceInfo = separatePriceInfo(getText(document, "div[class=pricing]"));
			String shippingPrice = "0.00";//free shipping
			String landingImages = "http://www.stuartweitzman.com/" + getElement(document, "img[itemprop=image]").attr("src");
			String featureBullets = getHtml(document, "div[class=pdpDetails]");
			String productDetailTable = getHtml(document, "div[class=pdpFitCare]");
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
	private static SpiderOrder searchByGmpvitasSpider(String urlString){
		Document document;
		SpiderOrder spiderOrder = new SpiderOrder();
		try {
			document = Jsoup.connect(urlString).cookie("auth", "token").userAgent("Chrome").timeout(30000).post();
			String name = getText(document, "h1[class=title]");
 			String brand = "GMP VITAS";
			String[] priceInfo = separatePriceInfo(getText(document, "em[class=ProductPrice VariationProductPrice]"));
			String shippingPrice = "";
			String landingImages = getElement(document, "div[class=ProductThumbImage] img").attr("src");
			String featureBullets = getHtml(document, "div[class=ProductDescriptionContainer prodAccordionContent]");
			String productDetailTable = "";
			
			spiderOrder.setUrl(urlString);
			spiderOrder.setWebsite(HtmlSpiderUtil.getWebsite(urlString));
			spiderOrder.setName(name);
			spiderOrder.setBrand(brand);
			spiderOrder.setCurrency(priceInfo[0]);
			spiderOrder.setPrice(priceInfo[1]);
			spiderOrder.setShippingPrice(shippingPrice);
			spiderOrder.setPics(new String[]{landingImages});
			spiderOrder.setFeatureHtml(featureBullets);
			spiderOrder.setDetailHtml(productDetailTable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spiderOrder;
	}
}
