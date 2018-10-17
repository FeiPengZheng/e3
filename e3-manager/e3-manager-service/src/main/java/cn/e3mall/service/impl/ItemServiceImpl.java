package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.domian.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.po.TbItem;
import cn.e3mall.po.TbItemDesc;
import cn.e3mall.po.TbItemExample;
import cn.e3mall.po.TbItemExample.Criteria;
import cn.e3mall.redis.JedisClient;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.E3Result;
import cn.e3mall.utils.IDUtils;
import cn.e3mall.utils.JsonUtils;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private JmsTemplate jsmTemplate;
	@Resource
	private Destination topicDesination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${PRE}")
	private String PRE;
	@Value("${REDIS_CACHE_TIME}")
	private long REDIS_CACHE_TIME;

	@Override
	public TbItem getItemById(long id) {

		try {
			String string = jedisClient.get(PRE + ":" + id+"BASE");
			if (StringUtils.isNotBlank(string)) {
				TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
				return item;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		TbItemExample tbItemExample = new TbItemExample();
		Criteria createCriteria = tbItemExample.createCriteria();
		createCriteria.andIdEqualTo(id);

		List<TbItem> list = itemMapper.selectByExample(tbItemExample);

		if (null != list && list.size() > 0) {

			try {
				jedisClient.set(PRE + ":" + id+"BASE", JsonUtils.objectToJson(list.get(0)));
			} catch (Exception e) {
				// TODO: handle exception
			
			e.printStackTrace();
			}
			
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		// 设置分页信息
		PageHelper.startPage(page, rows);

		// 执行查询
		TbItemExample example = new TbItemExample();

		List<TbItem> list = itemMapper.selectByExample(example);

		// 取信息
		PageInfo<TbItem> info = new PageInfo<>(list);

		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult(info.getTotal(), list);

		return result;
	}

	@Override
	public E3Result addItem(TbItem tbItem, String desc) {
		final long id = IDUtils.genItemId();
		tbItem.setId(id);
		// '商品状态，1-正常，2-下架，3-删除',
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		itemMapper.insert(tbItem);

		TbItemDesc itemDesc = new TbItemDesc();

		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		descMapper.insert(itemDesc);

		// 发送一个添加商品的消息
		jsmTemplate.send(topicDesination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage textMessage = session.createTextMessage(id + "");

				return textMessage;
			}
		});

		E3Result result = E3Result.ok();

		return result;
	}

	@Override
	public TbItemDesc findDescById(Long id) {
		try {
			
			String string = jedisClient.get(PRE+":"+id+"DESC");
			if (StringUtils.isNoneBlank(string)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(string, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TbItemDesc tbItemDesc = descMapper.selectByPrimaryKey(id);
		
		try {
			jedisClient.set(PRE+":"+id+"DESC", JsonUtils.objectToJson(tbItemDesc));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return tbItemDesc;
	}

	@Override
	public TbItem findParamcById(Long descId) {
		try {
			String string = jedisClient.get(PRE + ":" + descId+"BASE");
			if (StringUtils.isNotBlank(string)) {
				TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
				return item;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(descId);
		try {
			jedisClient.set(PRE + ":" + descId+"BASE", JsonUtils.objectToJson(item));
		} catch (Exception e) {
			// TODO: handle exception
		
		e.printStackTrace();
		}
		return item;
	}

	@Override
	public E3Result editItem(TbItem item, String desc) {

		// '商品状态，1-正常，2-下架，3-删除',
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);

		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(new Date());
		descMapper.updateByPrimaryKeySelective(itemDesc);

		E3Result result = E3Result.ok();
		return result;
	}

	/**
	 * 删除商品
	 */
	@Override
	public E3Result deleteItemsByIds(List<Long> ids) {
		// '商品状态，1-正常，2-下架，3-删除'
		for (Long id : ids) {
			TbItem tbItem = new TbItem();
			tbItem.setId(id);
			tbItem.setStatus((byte) 3);
			itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		E3Result result = E3Result.ok();

		return result;
	}

	/**
	 * 上架
	 */
	@Override
	public E3Result reshelf(List<Long> longIdList) {
		// '商品状态，1-正常，2-下架，3-删除'
		for (Long id : longIdList) {
			TbItem tbItem = new TbItem();
			tbItem.setId(id);
			tbItem.setStatus((byte) 1);
			itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		E3Result result = E3Result.ok();
		return result;
	}

	@Override
	public E3Result instock(List<Long> longIdList) {
		// '商品状态，1-正常，2-下架，3-删除'
		for (Long id : longIdList) {
			TbItem tbItem = new TbItem();
			tbItem.setId(id);
			tbItem.setStatus((byte) 2);
			itemMapper.updateByPrimaryKeySelective(tbItem);
		}
		E3Result result = E3Result.ok();
		return result;
	}

}
