package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.CookieUtils;
import cn.e3mall.utils.E3Result;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/page/login")
	public String showLogin(){
		return "login";
	}
	
	
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,
			HttpServletResponse response){
		
		E3Result result = userService.login(username, password);
		if (result.getStatus() == 200) {
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, "token", token);
		}
		return result;
	}
}
