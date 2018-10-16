package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.domian.SearchItem;
import cn.e3mall.domian.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer; 

	public SearchResult search(SolrQuery query) throws Exception {

		QueryResponse response = solrServer.query(query);

		SolrDocumentList solrDocumentList = response.getResults();

		long numFound = solrDocumentList.getNumFound();

		// 创建一个返回结果对象
		SearchResult result = new SearchResult();

		result.setRecourdCount((int) numFound);

		// 创建商品列表
		List<SearchItem> itemList = new ArrayList<>();

		// 取高亮列表
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		for (SolrDocument document : solrDocumentList) {
			// 去商品信息
			SearchItem searchItem = new SearchItem();

			searchItem.setCategory_name((String) document.get("item_category_name"));
			searchItem.setId((String) document.get("id"));
			searchItem.setImage((String) document.get("item_image"));
			searchItem.setPrice((long) document.get("item_price"));
			searchItem.setSell_point((String) document.get("item_sell_point"));

			List<String> list2 = highlighting.get(document.get("id")).get("item_title");

			String itemTitle = "";
			if (list2 != null && list2.size() > 0) {
				itemTitle = list2.get(0);
			} else {
				itemTitle = (String) document.get("item_title");
			}

			searchItem.setTitle(itemTitle);

			itemList.add(searchItem);
		}

		result.setItemList(itemList);

		return result;

	}

}
