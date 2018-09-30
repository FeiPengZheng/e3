package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.domian.EasyUITreeNode;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService categoryService;
	
	
	
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getCategoryList(@RequestParam(value="id",defaultValue="0") long parentId){
		
		return categoryService.getContentCategory(parentId);
	}
	
	
	
	
	
	
	
}
