package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Controller
public class TokenController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/user/token/{token}",produces="application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		E3Result result = userService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			return callback+"("+JsonUtils.objectToJson(result)+");";
			
		}
		return JsonUtils.objectToJson(result);
	}
}
