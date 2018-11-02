package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.po.TbItem;
import cn.e3mall.redis.JedisClient;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

/**
 * 登录后,添加购物车到redis中
 * 
 * @author Administrator
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbItemMapper tbItemMapper;

	/**
	 * 添加商品
	 */
	@Override
	public E3Result addCartItem(Long userId, Long itemId, Integer num) {
		// TODO Auto-generated method stub
		Boolean exists = jedisClient.hexists(userId + "", itemId + "");

		if (exists) {
			String result = jedisClient.hget(userId + "", itemId + "");
			if (!result.equals("null")) {
				TbItem tbItem_redis = JsonUtils.jsonToPojo(result, TbItem.class);
				tbItem_redis.setNum(tbItem_redis.getNum() + num);
				jedisClient.hset(userId + "", itemId + "", JsonUtils.objectToJson(tbItem_redis));
			}
		} else {
			TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
			tbItem.setNum(num);
			jedisClient.hset(userId + "", itemId + "", JsonUtils.objectToJson(tbItem));
		}
		return E3Result.ok();
	}

	/**
	 * 商品展现
	 */
	@Override
	public List<TbItem> getCartItemListFromRedis(Long id) {
		List<String> list = jedisClient.hvals(id + "");
		List<TbItem> tbItems = new ArrayList<>();
		for (String string : list) {
			TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
			tbItems.add(tbItem);
		}
		return tbItems;
	}

	/**
	 * 合并购物车
	 */
	@Override
	public E3Result mergeCart(Long userId, List<TbItem> list) {
		if (list == null || list.size() == 0) {
			return E3Result.build(404, "参数错误");
		}
		for (TbItem tbItem : list) {
			addCartItem(userId, tbItem.getId(), tbItem.getNum());
		}

		return E3Result.ok();
	}

	@Override
	public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
		// TODO Auto-generated method stub
		String hget = jedisClient.hget(userId + "", itemId + "");
		TbItem tbItem = JsonUtils.jsonToPojo(hget, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(userId + "", itemId + "", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCart(Long userId, Long itemId) {
		// TODO Auto-generated method stub
		jedisClient.hdel(userId + "", itemId + "");

		return E3Result.ok();
	}

	/**
	 * 清空购物车
	 */
	@Override
	public E3Result deleteCart(Long userId) {
		// TODO Auto-generated method stub
		
		jedisClient.del(userId+"");
		return E3Result.ok();
	}

}
