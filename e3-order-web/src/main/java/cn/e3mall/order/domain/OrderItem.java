package cn.e3mall.order.domain;

import cn.e3mall.po.TbItem;

public class OrderItem extends TbItem{

	public OrderItem() {
		super();
	}
	
	public String[] getImages(){
		
		String images = super.getImage();
		
		String[] images1 = images.split(",");
		
		return images1;
	}
	

}
