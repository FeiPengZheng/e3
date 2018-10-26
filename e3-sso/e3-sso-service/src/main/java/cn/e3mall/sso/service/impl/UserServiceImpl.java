package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.po.TbUser;
import cn.e3mall.po.TbUserExample;
import cn.e3mall.po.TbUserExample.Criteria;
import cn.e3mall.redis.JedisClient;
import cn.e3mall.sso.service.UserService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;
@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private TbUserMapper mapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result checkRegister(String data, Integer type) {
		// TODO Auto-generated method stub
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if (type == 1) {
		//用户名
			criteria.andUsernameEqualTo(data);
		}else if (type == 2) {
		//手机号
			criteria.andPhoneEqualTo(data);
		}else {
			return E3Result.build(400, "非法参数");
		}
		
		List<TbUser> list = mapper.selectByExample(example);
		
		if (list == null || list.size() == 0) {
			return E3Result.ok(true);
		}
		
		
		return E3Result.ok(false);
	}

	@Override
	public E3Result createUser(TbUser user) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(user.getUsername())) {
			return E3Result.build(400,"用户名不能为空");
		}
		
		if (StringUtils.isBlank(user.getPassword())) {
			return E3Result.build(400,"密码不能为空");
		}
		
		E3Result result = checkRegister(user.getUsername(),1);
		if (!(boolean)result.getData()) {
			return E3Result.build(400,"此用户名已经被使用");
		}
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkRegister(user.getPassword(), 2);
			
			if (!(boolean)result.getData()) {
				return E3Result.build(400,"此手机号已经注册");
			}
			
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		String password = DigestUtils.md5Hex(user.getPassword().getBytes());
		user.setPassword(password);
		
		mapper.insert(user);
		return E3Result.ok();
	}

	@Override
	public E3Result login(String userName, String password) {
		// TODO Auto-generated method stub
		TbUserExample example = new TbUserExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andUsernameEqualTo(userName);
		
		List<TbUser> list = mapper.selectByExample(example);
		
		if (list == null || list.size() == 0) {
			return E3Result.build(400,"用户名或者密码错误");
		}
		
		TbUser user = list.get(0);
		if (!user.getPassword().equals(DigestUtils.md5Hex(password.getBytes()))) {
			return E3Result.build(400,"用户名或者密码错误");
		}
		
		//登录成功后生成token  相当于原来的jssessionId
		String token = UUID.randomUUID().toString();
		
		//客户端不应该携带密码
		user.setPassword(null);
		jedisClient.set("USER_INFO :"+token, JsonUtils.objectToJson(user));
		
		jedisClient.expire("USER_INFO :"+token, SESSION_EXPIRE);
		return E3Result.ok(token);
	}

	/**
	 * 通过token获取tbUser
	 */
	@Override
	public E3Result getUserByToken(String token) {
		// TODO Auto-generated method stub
		
		String string = jedisClient.get("USER_INFO :"+token);
		
		if (StringUtils.isBlank(string)) {
			return E3Result.build(400,"用户登录已经过期");
		}
		jedisClient.expire("USER_INFO :"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(string,TbUser.class);
		
		return E3Result.ok(user);
	}

}
