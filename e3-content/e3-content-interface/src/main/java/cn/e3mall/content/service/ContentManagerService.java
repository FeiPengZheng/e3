package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.po.TbContent;
import cn.e3mall.utils.E3Result;

public interface ContentManagerService {
	EasyUIDataGridResult getContentList(long categoryId, Integer page, Integer rows);

	E3Result addContent(TbContent tbContent);

	E3Result editContent(TbContent tbContent);

	E3Result deleteContent(String[] split);
	
	List<TbContent> getContentListByCategoryId(long categoryId);
}
