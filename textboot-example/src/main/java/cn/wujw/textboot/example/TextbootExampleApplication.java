package cn.wujw.textboot.example;

import cn.wujw.textboot.example.model.Demo;
import cn.wujw.textboot.factory.ExcelMappingFactory;
import cn.wujw.textboot.enums.DataLocation;
import cn.wujw.textboot.model.ResultBody;
import cn.wujw.textboot.service.TextBootService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.List;


@ImportResource("classpath:consumer.xml")
@SpringBootApplication
public class TextbootExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TextbootExampleApplication.class, args);
        // 十万行十列
        String url = "https://ys-files.oss-cn-hangzhou.aliyuncs.com/export/20190302/2019030218073702752305.xlsx";
      //  String url = "https://ys-files.oss-cn-hangzhou.aliyuncs.com/export/20190301/2019030113450105374924228093.xlsx";
       // String url = "https://ys-files.oss-cn-hangzhou.aliyuncs.com/export/20190301/2019030114173101261107.xlsx";
        TextBootService textBootService = run.getBean(TextBootService.class);

//
        long b = System.currentTimeMillis();
        ResultBody data = textBootService.urlToExcelData(0, url, DataLocation.REDIS);
        System.out.println(System.currentTimeMillis() - b);
        System.out.println(data);
//
        long c = System.currentTimeMillis();
        ResultBody data1 = textBootService.urlToExcelData(0, url, DataLocation.REDIS);
        System.out.println(System.currentTimeMillis() - c);
        System.out.println(data1);

        long d = System.currentTimeMillis();
        ResultBody data2 = textBootService.urlToValidateExcelData(0, url, ExcelMappingFactory.INSTANCE.builder(Demo.class), DataLocation.REDIS);
        System.out.println(System.currentTimeMillis() - d);
        System.out.println(data2);

        List<List<String>> dataList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("1");
        list1.add("1");
        list1.add("1");
        list1.add("1");
        dataList.add(list);
        dataList.add(list1);
        String[] headName = {"1","2","3","4","5"};
        ResultBody body = textBootService.exportExcel(headName, dataList);
        System.out.println(body);
    }

}
