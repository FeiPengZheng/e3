package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.domian.EasyUITreeNode;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCategory(long parentId);
	
	
}
