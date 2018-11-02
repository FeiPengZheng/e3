package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Data;

import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.po.TbOrderItem;
import cn.e3mall.po.TbOrderShipping;
import cn.e3mall.redis.JedisClient;
import cn.e3mall.utils.E3Result;
import domain.OrderInfo;
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisClient  jedisClient;
	
	
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		if (!jedisClient.exists("ORDER_GEN_KEY")) {
			String orderItemId = jedisClient.set("ORDER_GEN_KEY", "100544").toString();
		}
		
		String orderId  = jedisClient.incr("ORDER_GEN_KEY").toString();
		
		orderInfo.setOrderId(orderId);
		
		orderInfo.setPostFee("0");
		
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		
		Date date = new Date();
		
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		//订单表中插入
		orderMapper.insert(orderInfo);
		
		//订单明细表中插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		
		for(TbOrderItem orderItem:orderItems){
			
			//明细Id
			String orderItemId = jedisClient.incr("ORDER_ITEM_ID_GEN_KEY").toString();
			orderItem.setId(orderItemId.toString());
			
			orderItem.setOrderId(orderItemId);
			//插入数据
			
			String[] split = orderItem.getPicPath().split(",");
			orderItem.setPicPath(split[0]);
			
			orderItemMapper.insert(orderItem);
		}
		
		//5.向订单物流表中插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		
		orderShippingMapper.insert(orderShipping);
		
		return E3Result.ok(orderId);
	}

}
