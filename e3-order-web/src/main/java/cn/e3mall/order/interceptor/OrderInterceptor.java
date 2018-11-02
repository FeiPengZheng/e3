package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbUser;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

public class OrderInterceptor implements HandlerInterceptor{

	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//从cookie中获取是否登录
		String token = CookieUtils.getCookieValue(request,"token");
		//如果没数据 直接跳转到登录界面
		if (StringUtils.isBlank(token)) {
			
			response.sendRedirect("http://localhost:8087/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//如果有token 向redis中查询是否过期
		E3Result e3Result = userService.getUserByToken(token);
		//如果过期 重新登录 登陆后跳转回来
		if (e3Result.getStatus() != 200) {
		
			response.sendRedirect("http://localhost:8087/page/login?redirect="+request.getRequestURL());
			return false;
		
		}
		//如果没有过期 直接放行 并且合并数据
		TbUser tbUser = (TbUser) e3Result.getData();
		request.setAttribute("user",tbUser);
		String cartList = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNoneBlank(cartList)) {
			cartService.mergeCart(tbUser.getId(),JsonUtils.jsonToList(cartList,TbItem.class));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
