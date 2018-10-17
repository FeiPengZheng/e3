package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.domian.SearchItem;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.utils.E3Result;

@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private SearchItemMapper searchItemMapper; 
	
	@Autowired
	private SolrServer solrService;

	@Override
	public E3Result importItems() {
		try {
			List<SearchItem> allSearchItem = searchItemMapper.getAllSearchItem();
			for (SearchItem searchItem : allSearchItem) {
				
			/*	<field name="item_title" type="text_ik" indexed="true" stored="true"/>
				<field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
				<field name="item_price"  type="long" indexed="true" stored="true"/>
				<field name="item_image" type="string" indexed="false" stored="true" />
				<field name="item_category_name" type="string" indexed="true" stored="true" />*/

				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//写入索引库
				solrService.add(document);
			}
			solrService.commit();
			return E3Result.ok();	
		} catch (Exception e) {
			return E3Result.build(500,"导入商品失败");
		}
	}

	/**
	 * 收到消息后  向索引库 添加商品
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	public E3Result addDocument(Long itemId)throws Exception {
			SearchItem searchItem = searchItemMapper.searchItemById(itemId);
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//写入索引库
			solrService.add(document);
			solrService.commit();
			return E3Result.ok();
		}
		
	}

