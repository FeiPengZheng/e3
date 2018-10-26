package cn.e3mall.cart.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbUser;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Controller
public class CartController {

	@Value("${CART}")
	private String CART;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemid}")
	public String addCartItem(@PathVariable Long itemid,@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request,HttpServletResponse response){
	
		
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if (tbUser != null) {
			
			cartService.addCartItem(tbUser.getId(), itemid, num);
			
			return"cartSuccess";
		}
		
		
		//1从coolkie中查询商品列表
		List<TbItem> cartList = getCartList(request);
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemid.longValue()) {
				
				tbItem.setNum(num+tbItem.getNum());
				
				hasItem = true;
				break;
			}
		}
		
		if (!hasItem) {
			TbItem item = itemService.getItemById(itemid);
			String image = item.getImage();
			//与isNotBlank的区别
			if (StringUtils.isNoneBlank(image)) {
				String[] split = image.split(",");
				item.setImage(split[0]);
			}
			
			//设置购买商品数量
			item.setNum(num);
			
			
			cartList.add(item);
			
		}
		CookieUtils.setCookie(request, response, CART,
				JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
		
		return"cartSuccess";
	}
	
	
	/**
	 * 从cookie中获取购物车列表
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request){
		
		String json = CookieUtils.getCookieValue(request, CART,true);
		
		if (StringUtils.isNotBlank(json)) {
			List jsonToList = JsonUtils.jsonToList(json,TbItem.class);
			return jsonToList;
		}
		
		return new ArrayList<>();
	}
	
	/**
	 * 展现购物车
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response,Model model){
		TbUser tbUser = (TbUser) request.getAttribute("user");
		//从cookie中获取数据
		List<TbItem> cartList = getCartList(request);
		
		if (tbUser != null) {
			cartService.mergeCart(tbUser.getId(), cartList);
			cartList = cartService.getCartItemListFromRedis(tbUser.getId());
			CookieUtils.deleteCookie(request, response, CART);
		}
		
		model.addAttribute("cartList", cartList);
		
		return "cart";
	}
	
	/**
	 * 更改商品数量
	 * @param itemid
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemid}/{num}")
	@ResponseBody
	//http://localhost:8090/cart/update/num/153959025750983/17.action
	public E3Result updateNum(@PathVariable Long itemid ,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemid, num);
			return E3Result.ok();
		}
		
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().equals(itemid)) {
				tbItem.setNum(num);
				break;
			}
		}
		
		CookieUtils.setCookie(request, response, CART, JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
		
		return E3Result.ok();
	}
	
	/**
	 * 删除某列商品
	 * @param cartid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{cartid}")
	public String deleteItem(@PathVariable Long cartid,HttpServletRequest request,HttpServletResponse response){
		
		
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCart(user.getId(), cartid);
			return "redirect:/cart/cart.html";
		}
		
		List<TbItem> cartList = getCartList(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().equals(cartid)) {
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART, JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
		
		return "redirect:/cart/cart.html";
	}
	
}
