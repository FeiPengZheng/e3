package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.content.service.ContentManagerService;
import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.po.TbContent;
import cn.e3mall.po.TbContentExample;
import cn.e3mall.po.TbContentExample.Criteria;
import cn.e3mall.redis.JedisClient;
import cn.e3mall.redis.JedisClientCluster;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.JsonUtils;

@Service
public class ContentManagerServiceImpl implements ContentManagerService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient client;

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	@Override
	public EasyUIDataGridResult getContentList(long categoryId, Integer page, Integer rows) {

		// 设置分页查询
		PageHelper.startPage(page, rows);
		// 执行查询
		TbContentExample example = new TbContentExample();

		Criteria createCriteria = example.createCriteria();

		createCriteria.andCategoryIdEqualTo(categoryId);

		List<TbContent> list = contentMapper.selectByExample(example);

		// 取信息
		PageInfo<TbContent> info = new PageInfo<>(list);

		return new EasyUIDataGridResult(info.getTotal(), list);

	}

	@Override
	public E3Result addContent(TbContent tbContent) {
		Date createTime = new Date();
		tbContent.setCreated(createTime);
		tbContent.setUpdated(createTime);
		contentMapper.insertSelective(tbContent);
		try {
			client.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return E3Result.ok();
	}

	@Override
	public E3Result editContent(TbContent tbContent) {
		// TODO Auto-generated method stub
		Date editTime = new Date();
		tbContent.setUpdated(editTime);
		contentMapper.updateByPrimaryKeySelective(tbContent);
		try {
			client.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContent(String[] split) {
		
		for (String id : split) {
			TbContent content = contentMapper.selectByPrimaryKey(Long.valueOf(id));
			try {
				client.hdel(CONTENT_LIST,content.getCategoryId().toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			contentMapper.deleteByPrimaryKey(Long.valueOf(id));
		}
		
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCategoryId(long categoryId) {
		// TODO Auto-generated method stub
		try {
			String list = client.hget(CONTENT_LIST, categoryId + "");
			if (StringUtils.isNotBlank(list)) {
				List<TbContent> jsonToList = JsonUtils.jsonToList(list, TbContent.class);
				return jsonToList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbContentExample example = new TbContentExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryIdEqualTo(categoryId);

		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);

		try {
			client.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
