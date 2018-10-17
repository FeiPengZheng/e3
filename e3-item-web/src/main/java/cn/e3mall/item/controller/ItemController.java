package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.domian.Item;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("item/{id}")
	public String showIndex(@PathVariable Long id, Model model) {

		TbItem itemById = itemService.getItemById(id);
		Item item = new Item(itemById);
		TbItemDesc findDescById = itemService.findDescById(id);

		model.addAttribute("item", item);
		model.addAttribute("itemDesc", findDescById);

		return "item";
	}

}
