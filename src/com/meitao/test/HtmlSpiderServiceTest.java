package com.meitao.test;

import static org.junit.Assert.assertSame;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meitao.service.SpiderOrderService;
import com.meitao.common.constants.Constant;
import com.meitao.common.constants.ResponseCode;
import com.meitao.common.util.HtmlSpiderUtil;
import com.meitao.exception.ServiceException;
import com.meitao.model.PageSplit;
import com.meitao.model.ResponseObject;
import com.meitao.model.SpiderOrder;
import com.meitao.model.User;

/**
 * junit需要注掉spring.xml, 里面的multipartResolver
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-mvc.xml", "classpath:/spring.xml"})
public class HtmlSpiderServiceTest {
	@Resource(name="spiderOrderService")
	private SpiderOrderService spiderOrderService;
	
	@Test
	public void testAll(){
		String[] urls = new String[]{
//				"http://www.amazon.com/gp/product/B00IA2NYT4/ref=s9_simh_gw_g63_i3_r?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-2&pf_rd_r=0JX2B34VC108JW2Y79EH&pf_rd_t=36701&pf_rd_p=2091268722&pf_rd_i=desktop",
//				"http://www.6pm.com/product/8641011/color/151",
//				"http://www1.macys.com/shop/product/planet-gold-juniors-racerback-tank-top?ID=966352&CategoryID=17043&tdp=cm_app~zMCOM-NAVAPP~xcm_zone~zPDP_ZONE_A~xcm_choiceId~zcid6N0011-83acbb69-4993-41a8-8b3a-7355c26f3026%40H7%40customers%2Balso%2Bshopped%2417043%24966352~xcm_srcCatID~z17043~xcm_pos~zPos4",
//				"http://www1.macys.com/shop/product/american-rag-racerback-tank-top-only-at-macys?ID=1046093&CategoryID=17043&tdp=cm_app~zMCOM-NAVAPP~xcm_zone~zPDP_ZONE_B~xcm_choiceId~zcid7L0011-e8a36d84-6780-4123-800c-2300d94252a7%40H5%40you%2Bmight%2Balso%2Blike...%2417043%241046093~xcm_srcCatID~z17043~xcm_pos~zPos4",
//				"http://www1.bloomingdales.com/shop/product/7-for-all-mankind-kimmie-bootcut-jeans-in-sildarkink?ID=1587731&CategoryID=5545#fn=spp%3D1%26ppp%3D180%26sp%3D1%26rid%3D%26spc%3D774%26pn%3D1",
//				"http://www.levi.com/US/en_US/womens-clothing-tops/p/234840001",
//				"http://www.lacoste.com/us/lacoste/men/clothing/polos/short-sleeve-original-heathered-pique-polo/L1264-51.html?dwvar_L1264-51_color=CCA",
//				"http://www.jomashop.com/citizen-skyhawk-mens-watch-jy0010-50e.html",
//				"http://www.gnc.com/GNC-CoQ-10-100mg/product.jsp?productId=16120536",
//				"https://www.groupon.com/deals/gg-bio-bidet-bb-600-ultimate-bidet-smart-seat-1",
////				"http://www.abercrombie.cn/en_CN/womens-hoodies-and-sweatshirts-crew/applique-logo-sweatshirt/anf-113053.html?dwvar_anf-113053_color=02#start=1",
////				"http://www.hollisterco.cn/en_CN/girls-tops-t-shirts-and-tanks-sleeveless-tops/must-have-slim-scoop-tank/hol-115182.html?dwvar_hol-115182_color=01#start=1",
//				"http://www.hollisterco.com/shop/uk/girls-sweaters/rib-trim-boyfriend-cardigan-6588602_04",
//				"https://ae.com/web/browse/product_details.jsp?productId=1305_1245_823&catId=cat1680004",
//				"http://www.ugg.cn/product-800.html",
////				"http://www.uggaustralia.com/women-boots/patten/1006011.html?dwvar_1006011_color=CHE",
//				"http://www.ebay.com/itm/Women-Casual-Long-Sleeve-Pullover-Hoodie-Jacket-Sweater-Coat-Hooded-Jumper-Tops-/281871519769?talgo=origal&tfrom=281893593678&tpos=unknow&ttype=price",
//				"https://peterthomasroth.com/Product/All%20Products/3%20PIECE%20MEGA%20RICH%20CELL%20CREME/9908150/each",
//				"http://www.charlotteolympia.com/collections/encore/kitty-flats/E001139PPP0282.html#cgid=KITTY+ALL&start=8",
//				"http://www.carters.com/carters-baby-girl-new-arrivals/VC_121G400.html?cgid=carters-baby-girl-new-arrivals&dwvar_VC__121G400_size=12M&dwvar_VC__121G400_color=Mint#start=1&cgid=carters-baby-girl-new-arrivals",
//				"http://www.stuartweitzman.com/products/nudistsong/?DepartmentId=671&DepartmentGroupId=2&ColMatID=25876",
//				"http://www.gmpvitas.com/gmp-vitas-goutrex-blend/",
		};
		for(String u : urls){
			SpiderOrder spiderOrder = this.testSearch(u);
			if("".equals(spiderOrder.getPrice())){
				System.out.println(spiderOrder.getWebsite());
			}
		}
		
		String url = "http://www.amazon.com/gp/product/B00IA2NYT4/ref=s9_simh_gw_g63_i3_r?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-2&pf_rd_r=0JX2B34VC108JW2Y79EH&pf_rd_t=36701&pf_rd_p=2091268722&pf_rd_i=desktop";
		SpiderOrder spiderOrder = this.testSearch(url);
	}
	

	public SpiderOrder testSearch(String url){
		ResponseObject<SpiderOrder> responseObject = null;
		try {
			responseObject = spiderOrderService.search(url);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		assertSame(ResponseCode.SUCCESS_CODE, responseObject.getCode());
		return responseObject.getData();
	}
}
