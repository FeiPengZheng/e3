package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.po.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	
	
	@RequestMapping("/item/{itemid}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemid){
		TbItem item = 
				itemService.getItemById(itemid);
		
		return item;
		
	} 
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	@RequestMapping("/{pageJsp}")
	public String showPage(@PathVariable String pageJsp){
		
		return pageJsp;
	}
	
	
	
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		EasyUIDataGridResult itemList = itemService.getItemList(page,rows);
		return itemList;
	}
	
	
	
}
