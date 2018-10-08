package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.domian.EasyUITreeNode;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.po.TbContentCategory;
import cn.e3mall.po.TbContentCategoryExample;
import cn.e3mall.po.TbContentCategoryExample.Criteria;
import cn.e3mall.utils.E3Result;
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


	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//添加新得叶子节点
		TbContentCategory category = new TbContentCategory();
		category.setIsParent(false);
		category.setName(name);
		category.setParentId(parentId);
		category.setSortOrder(1);
		//1 正常 0 删除
		category.setStatus(1);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		
		categoryMapper.insertSelective(category);
		
		
		
		//更新父叶子节点
		TbContentCategory parent = categoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);;
			categoryMapper.updateByPrimaryKeySelective(parent);
		}
		return new E3Result(category);
	}


	@Override
	public E3Result editContentCategory(long id, String name) {
	     TbContentCategory category = new TbContentCategory();
	     category.setId(id);
	     category.setName(name);
	     category.setUpdated(new Date());
	     
	     categoryMapper.updateByPrimaryKeySelective(category);
		return E3Result.ok();
	}


	@Override
	public E3Result removeNode(long id) {
	    
		TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
		
		if (category.getIsParent()) {
			return E3Result.ok("isParent");
		}else {
			categoryMapper.deleteByPrimaryKey(id);
			
			
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(category.getParentId());
			List<TbContentCategory> list = categoryMapper.selectByExample(example);
			
			if ( list == null || list.size() == 0 ) {
				TbContentCategory parent = categoryMapper.selectByPrimaryKey(category.getParentId());
				parent.setId(category.getParentId());
				parent.setIsParent(false);
				categoryMapper.updateByPrimaryKeySelective(parent);
			}
		}
		
		return E3Result.ok("ok");
	}

}
