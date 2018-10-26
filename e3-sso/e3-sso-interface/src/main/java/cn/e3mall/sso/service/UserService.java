package cn.e3mall.sso.service;

import cn.e3mall.po.TbUser;
import cn.e3mall.utils.E3Result;

public interface UserService {

	E3Result checkRegister(String data,Integer type);
	
	E3Result createUser(TbUser user);
	
	E3Result login(String userName,String password);
	
	E3Result getUserByToken(String token);
}
