package cn.e3mall.item.domian;

import cn.e3mall.po.TbItem;

public class Item extends TbItem {

	public String[] getImages() {
		String images = this.getImage();
		if (images != null && !"".equals(images)) {
			String[] split = images.split(",");
			return split;
		}
		return null;
	}

	public Item() {
	}

	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}
	
	
	

}
