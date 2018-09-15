package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbItemExample;
import cn.e3mall.po.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper mapper;
	
	@Override
	public TbItem getItemById(long id) {

		TbItemExample tbItemExample = new TbItemExample();
		Criteria createCriteria = tbItemExample.createCriteria();
		createCriteria.andIdEqualTo(id);
		
		List<TbItem> list = mapper.selectByExample(tbItemExample);
		
		if (null != list && list.size() > 0) {
			
			return list.get(0);
		}
		
	//	TbItem selectByPrimaryKey = mapper.selectByPrimaryKey(id);
		
		return null;
	}

}
