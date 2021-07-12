package com.example.fileElaticSearch;

import com.example.fileElaticSearch.tuling.pojo.MoXing;
import com.example.fileElaticSearch.tuling.utils.ESUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class FileElaticSearchApplicationTests {

	@Autowired
	private ESUtils esUtils;

	@Test
	void contextLoads() throws IOException {
		//创建添加内容
		MoXing moXing1 = new MoXing("印刷", "几何图", "/home/axs-spring/data/springFile/ShiJue/jt/ys/jht");
		MoXing moXing2 = new MoXing("印刷", "公式", "/home/axs-spring/data/springFile/ShiJue/jt/ys/gs");
		MoXing moXing3 = new MoXing("印刷", "非几何", "/home/axs-spring/data/springFile/ShiJue/jt/ys/fjh");
		MoXing moXing4 = new MoXing("印刷", "纯文本", "/home/axs-spring/data/springFile/ShiJue/jt/ys/cwb");
		MoXing moXing5 = new MoXing("印刷", "全题", "/home/axs-spring/data/springFile/ShiJue/jt/ys/qt");
		esUtils.addDocument("jt",moXing1);
		esUtils.addDocument("jt",moXing2);
		esUtils.addDocument("jt",moXing3);
		esUtils.addDocument("jt",moXing4);
		esUtils.addDocument("jt",moXing5);

		//根据id删除文档信息
		/*DeleteResponse response = esUtils.deleteRequest("pz","xBrXF3kBQ1b6LAN7b14q");
		DeleteResponse response1 = esUtils.deleteRequest("pz","xRrXF3kBQ1b6LAN7b16D");
		DeleteResponse response2 = esUtils.deleteRequest("pz","xhrXF3kBQ1b6LAN7b16p");
		DeleteResponse response3 = esUtils.deleteRequest("pz","xxrXF3kBQ1b6LAN7b17Y");
		DeleteResponse response4 = esUtils.deleteRequest("pz","yBrXF3kBQ1b6LAN7cF4I");*/
		//System.out.println(response.status());

		//System.out.println(esUtils.search("截图","原始","几何图"));
	}

}
