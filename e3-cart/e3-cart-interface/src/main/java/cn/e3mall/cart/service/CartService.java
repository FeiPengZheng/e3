package cn.e3mall.cart.service;


import java.util.List;

import cn.e3mall.po.TbItem;
import cn.e3mall.utils.E3Result;

public interface CartService {
	E3Result addCartItem(Long userId,Long itemId,Integer num);

	List<TbItem>  getCartItemListFromRedis(Long id);
	
	E3Result mergeCart(Long userId,List<TbItem> list);
	
	E3Result updateCartNum(Long userId,Long itemId,Integer num);
	
	E3Result deleteCart(Long userId,Long itemId);
	
	E3Result deleteCart(Long userId);
		
}
