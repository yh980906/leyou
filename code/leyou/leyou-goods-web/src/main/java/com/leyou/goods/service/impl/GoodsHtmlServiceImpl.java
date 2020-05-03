package com.leyou.goods.service.impl;

import com.leyou.goods.service.IGoodsHtmlService;
import com.leyou.goods.service.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;


@Service
public class GoodsHtmlServiceImpl implements IGoodsHtmlService {

     @Autowired
     private IGoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlServiceImpl.class);

    /**
     * 创建html页面
     *
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) {

        PrintWriter writer = null;
        try {
            // 获取页面数据
            Map<String, Object> spuMap = this.goodsService.loadModel(spuId);

            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariables(spuMap);

            // 创建输出流
            File file = new File("D:\\develop\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            LOGGER.error("页面静态化出错：{}，"+ e, spuId);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void deleteHtml(Long id) {
        File file = new File("D:\\develop\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
//    public void asyncExcute(Long spuId) {
//        ThreadUtils.execute(()->createHtml(spuId));
//        /*ThreadUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                createHtml(spuId);
//            }
//        });*/
//    }
}
