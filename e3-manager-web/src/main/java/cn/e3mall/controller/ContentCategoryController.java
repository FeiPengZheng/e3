package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.domian.EasyUITreeNode;
import cn.e3mall.utils.E3Result;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService categoryService;
	
	
	
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getCategoryList(@RequestParam(value="id",defaultValue="0") long parentId){
		
		return categoryService.getContentCategory(parentId);
	}
	
	
	
	
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result addContentCategory(long parentId,String name){
		
		return categoryService.addContentCategory(parentId,name);
		
	}
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result editContentCategory(long id,String name){
		return categoryService.editContentCategory(id,name);
	}
	
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result removeNode(long id){
		
		return categoryService.removeNode(id);
		
	}
	
	
}
