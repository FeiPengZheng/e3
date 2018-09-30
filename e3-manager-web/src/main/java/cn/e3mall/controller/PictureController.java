package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.utils.FastDFSClient;
import cn.e3mall.utils.JsonUtils;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value = "/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String pictureUpLoad(MultipartFile uploadFile) {
		/*
		 * 1、接收页面传递的图片信息uploadFile 2、把图片上传到图片服务器。使用封装的工具类实现。需要取文件的内容和扩展名。
		 * 3、图片服务器返回图片的url 4、将图片的url补充完整，返回一个完整的url。 5、把返回结果封装到一个Map对象中返回。
		 */
		try {
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败!");
			return JsonUtils.objectToJson(map);
		}
	}

}
