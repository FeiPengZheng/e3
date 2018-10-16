package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.domian.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	/**
	 * 根据关键字查找
	 */
	@Autowired
	private SearchDao searchDao;
	
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		
		SolrQuery query = new SolrQuery();
		
		query.setQuery(keyword);
		
		query.setStart((page - 1) * rows);
		
		query.setRows(rows);
		
		//设置默认搜索域
		query.set("df","item_title");
		
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style = 'color:red'>");
		query.setHighlightSimplePost("</em>");
		
		SearchResult result = searchDao.search(query);
		
		//计算总页数
		
		int recourdCount = result.getRecourdCount();
		
		double pages = Math.ceil(recourdCount / rows);
		
		result.setTotalPages((int) pages);
		
		return result;
	}

}
