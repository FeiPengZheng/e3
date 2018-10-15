package test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class MyTest {

	
	@Test
	public void addDocument() throws Exception {
		// 第一步：把solrJ的jar包添加到工程中。
		// 第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.131:8080/solr");
		// 第三步：创建一个文档对象SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
		//SolrInputDocument(fields: [id=816448, item_title=三星 Note II (N7100) 钻石粉 联通3G手机, item_sell_point=经典回顾！超值特惠！, item_price=169900, item_image=http://image.e3mall.cn/jd/5a45e88aeca046ec88d7b7ffbc47092a.jpg, item_category_name=手机])
		document.addField("id", "816448");
		document.addField("item_title", "三星 Note II (N7100) 钻石粉 联通3G手机");
		document.addField("item_sell_point", "经典回顾！超值特惠！");
		document.addField("item_price", 169900);
		document.addField("item_image", "http://image.e3mall.cn/jd/5a45e88aeca046ec88d7b7ffbc47092a.jpg");
		document.addField("item_category_name","手机");
		// 第五步：把文档添加到索引库中。
		solrServer.add(document);
		// 第六步：提交。
		solrServer.commit();
	}
}
