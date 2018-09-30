package cn.e3mall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.remoting.exchange.Request;

import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbItemDesc;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;

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
	
	@RequestMapping(value = "/item/save",method = RequestMethod.POST)
	@ResponseBody
	public E3Result addTbItem(TbItem tbItem,String desc){
		E3Result result = itemService.addItem(tbItem, desc);
		return result;
	}
	/**
	 * 查询商品描述
	 * @param id
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3Result findDescById(@PathVariable String id){
		Long descId = Long.valueOf(id);
		TbItemDesc itemDesc = itemService.findDescById(descId);
		E3Result result = new E3Result(itemDesc);
		return result;
	}
	/**
	 * 查询商品参数
	 * @param id
	 * @return
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public E3Result findParamcById(@PathVariable String id){
		Long descId = Long.valueOf(id);
		TbItem itemDesc = itemService.findParamcById(descId);
		E3Result result = new E3Result(itemDesc);
		return result;
	}
	
	/**
	 * 编辑商品
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result editItem(TbItem item,String desc ){
		
		E3Result result = itemService.editItem(item,desc);
		
		return result;
	}
	/**
	 * 删除商品
	 * 
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItems(String ids){
		String[] idList = ids.split(",");
		List<Long> longIdList = new ArrayList<>();
		for (String string : idList) {
			longIdList.add(Long.valueOf(string));
		}
		E3Result result = itemService.deleteItemsByIds(longIdList);
		return result;
	}
	
	/**
	 * 选中的商品上架
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelf(String ids){
		String[] idList = ids.split(",");
		List<Long> longIdList = new ArrayList<>();
		for (String string : idList) {
			longIdList.add(Long.valueOf(string));
		}
		return itemService.reshelf(longIdList);
	} 
	
	/**
	 * 选中的商品下架
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result  instock(String ids){
		String[] idList = ids.split(",");
		List<Long> longIdList = new ArrayList<>();
		for (String string : idList) {
			longIdList.add(Long.valueOf(string));
		}
		return itemService.instock(longIdList);
	}
	
}
