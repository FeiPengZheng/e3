package cn.e3mall.order.service;

import cn.e3mall.utils.E3Result;
import domain.OrderInfo;

public interface OrderService {
	
	public E3Result createOrder(OrderInfo orderInfo);
}
