package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.domian.EasyUITreeNode;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.po.TbContentCategory;
import cn.e3mall.po.TbContentCategoryExample;
import cn.e3mall.po.TbContentCategoryExample.Criteria;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	
	@Override
	public List<EasyUITreeNode> getContentCategory(long parentId) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		
		Criteria criteria = example.createCriteria();
		
		criteria.andParentIdEqualTo(parentId);
		
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		
		List<EasyUITreeNode> resultList = new ArrayList<>();
		
		for (TbContentCategory category : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			node.setState(category.getIsParent()?"closed":"open");
			
			resultList.add(node);
		}
		
		
		return resultList;
	}

}
