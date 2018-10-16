package cn.e3mall.protal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.domian.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {

	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	
	
	@Autowired
	private SearchService searchService;
	
	
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue= "1") Integer page,Model model) throws Exception{
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		
		//调用service查询商品
		SearchResult result = searchService.search(keyword, page, PAGE_ROWS);
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",result.getTotalPages());
		model.addAttribute("recourdCount",result.getRecourdCount());
		model.addAttribute("page",page);
		model.addAttribute("itemList", result.getItemList());
		
		int a = 1 / 0;
		return"search";
		
		
	}
	
	
	
	
	
	
	
	
}
