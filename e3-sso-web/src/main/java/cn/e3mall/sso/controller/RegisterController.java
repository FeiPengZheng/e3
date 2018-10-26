package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.po.TbUser;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.E3Result;

@Controller
public class RegisterController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/page/register")
	public String showRegister(){
		
		return "register";
	}
	
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		
		return userService.checkRegister(param, type);
	}
	
	
	@RequestMapping(value = "/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = userService.createUser(user);
		return result;
	}
	
}
