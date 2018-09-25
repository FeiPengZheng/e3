package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.domian.EasyUIDataGridResult;
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

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		//设置分页信息
		PageHelper.startPage(page,rows);
		
		//执行查询
		TbItemExample example = new TbItemExample();
		
		List<TbItem> list = mapper.selectByExample(example);
		
		//取信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		
		
		//创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult(info.getTotal(),list);
		
		
		
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	

}
