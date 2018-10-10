package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentManagerService;
import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.po.TbContent;
import cn.e3mall.utils.E3Result;

@Controller
public class ContentManagerController {

	@Autowired
	private ContentManagerService contentManagerService;
	
	
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId,Integer page,Integer rows){
		
		return contentManagerService.getContentList(categoryId, page, rows);
	}
	
	
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent tbContent){
		
		return contentManagerService.addContent(tbContent);
		
	}
	
	
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3Result editContent(TbContent tbContent){
		
		return contentManagerService.editContent(tbContent);
	}
	
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(String ids){
		return contentManagerService.deleteContent(ids.split(","));
	}
	
	
	
	
}
