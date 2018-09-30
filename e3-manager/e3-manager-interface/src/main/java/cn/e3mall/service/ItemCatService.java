package cn.e3mall.service;

import java.util.List;

import cn.e3mall.domian.EasyUITreeNode;

public interface ItemCatService {

	List<EasyUITreeNode> getCatList(long parentId);
}
