package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.domian.EasyUITreeNode;
import cn.e3mall.utils.E3Result;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCategory(long parentId);

	E3Result addContentCategory(long parentId, String name);

	E3Result editContentCategory(long parentId, String name);

	E3Result removeNode(long id);
	
	
}
