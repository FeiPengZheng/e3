package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.order.domain.OrderItem;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbUser;
import cn.e3mall.utils.E3Result;
import domain.OrderInfo;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		
		 TbUser user = (TbUser) request.getAttribute("user");
		 
		 List<TbItem> list = cartService.getCartItemListFromRedis(user.getId());
		 
		 request.setAttribute("cartList",list);
		
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		
		
		TbUser user = (TbUser) request.getAttribute("user");
		
		orderInfo.setUserId(user.getId());
		
		orderInfo.setBuyerNick(user.getUsername());
		
		E3Result result = orderService.createOrder(orderInfo);
		
		
		if (result.getStatus() == 200) {
			
			//清空购物车
			cartService.deleteCart(user.getId());
		}
		
		//取订单号
		String orderId = result.getData().toString();
		
		
		request.setAttribute("orderId",orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		
		//当前日期加三天
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date",dateTime.toString("yyyy-MM-dd"));
		
		return "success";
	}
	
	
	
	
	
}
