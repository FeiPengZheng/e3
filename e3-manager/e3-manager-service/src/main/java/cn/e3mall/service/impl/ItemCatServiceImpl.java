package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.domian.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.po.TbItemCat;
import cn.e3mall.po.TbItemCatExample;
import cn.e3mall.po.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper mapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(long parentId) {
		//根据parentId查询节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = mapper.selectByExample(example);
		
		//转换成EasyUITreeNode的列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbItemCat cat : list){
			
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(cat.getId());
			node.setText(cat.getName());
			node.setState(cat.getIsParent()?"closed":"open");
		
			resultList.add(node);
		}
		
		return resultList;
	}

}
