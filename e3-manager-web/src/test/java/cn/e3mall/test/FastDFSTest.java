package cn.e3mall.test;

import org.junit.Test;

import cn.e3mall.utils.FastDFSClient;

public class FastDFSTest {
	
	
	@Test
	public void test1() throws Exception{
		FastDFSClient client = new FastDFSClient("D:/e3/e3-common/src/main/resources/client.conf");
		String uploadFile = client.uploadFile("D:/用户目录/我的图片/BadlandsCycle_ZH-CN11688990875_1920x1080.jpg");
		System.out.println(uploadFile);
	}
}
