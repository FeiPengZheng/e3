package cn.e3mall.service;

import java.util.List;

import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbItemDesc;
import cn.e3mall.utils.E3Result;

public interface ItemService {

	
	TbItem getItemById(long id);
	
	EasyUIDataGridResult getItemList(int page,int rows);
	
	E3Result addItem(TbItem tbItem,String desc);

	TbItemDesc findDescById(Long id);

	TbItem findParamcById(Long descId);

	E3Result editItem(TbItem item, String desc);

	E3Result deleteItemsByIds(List<Long> ids);

	E3Result reshelf(List<Long> longIdList);

	E3Result instock(List<Long> longIdList);
	
}
