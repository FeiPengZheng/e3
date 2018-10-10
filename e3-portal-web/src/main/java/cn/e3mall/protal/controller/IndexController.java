package cn.e3mall.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentManagerService;
import cn.e3mall.po.TbContent;

@Controller
public class IndexController {

	@Value("${CONTENT_LUNBO}")
	private long CONTENT_LUNBO;
	
	@Autowired
	private ContentManagerService contentmanagerService;
	
	@RequestMapping("/index")
	public String index(Model model){
		List<TbContent> list = contentmanagerService.getContentListByCategoryId(CONTENT_LUNBO);
		
		model.addAttribute("ad1List",list);
		
		return "index";
	}
	
	
}
