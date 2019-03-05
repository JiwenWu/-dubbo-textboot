## 简单上手
> Textboot是一款目标专门针对文本处理的独立服务,由Dubbo构建，主要是为了剥离出业务场景中的很多文本处理，例如excel文件的导入导出，pdf文件的生成，word
文件的生成等等，凡是涉及到文本的处理都可以加入到此计划内

### 计划
* 完善excel的数据解析和校验，
* 突破dubbo RPC接口参数对Class自定义类的限制，例如采用序列化文件的方式
* 完善excel的导出功能，希望能支持模版化的数据导出
* ......
* 集成pdf模版导出，word导出

### 功能简介
1. 服务调用者通过传递文件云路径的方式，通过本服务进行数据的解析，返回文件中的数据
2. 支持简单的文本校验，例如必填项，文本正则校验
3. 支持多种数据的返回方式，支持接口的同步返回和中间件redis获取
4. 极大程度上提高了文件解析效率，特别是解决了大文件导入过程中造成的内存溢出，多次测试3000行7列的数据只需要平均300ms就可以完成数据的组装
   经测试10万行10列的百万级数据 耗时大概6000ms左右，推荐存入到redis, 如果同步返回的话可能会超过dubbo传输的大小限制（检验接口已粗发，原因是filed的key组装）
5. 解决含有占位符的空假行造成的读空值问题
6. 数据格式简单便于处理，map的方式可以直接与mybatis结合，List<Map<key,value>>即为行列

### 使用手册
1. 引入maven依赖的公共接口或方法
2. 配置dubbo服务的接口节点
3. 配置导入的实体类或者组装rule数据
4. 调用服务接口

#### pom.xml
```xml
  <!--版本号可能会更新-->
<dependency>
    <artifactId>textboot-api</artifactId>
    <groupId>cn.wujw.textboot</groupId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

```
#### 配置dubbo服务的节点
```xml
<dubbo:reference id="importService" interface="cn.wujw.textboot.service.TextBootService" protocol="dubbo"/>
```
#### 配置导入实体（按需求可有可无）
如果你需要解析的excle文件是不需要进行某些字段的检验或对应关系，或者是自由扩展列的excel文件，不需要配置（具体可看接口说明）
```java
/**
*  配置该导入字段对应类时需要结成BasicModel,利用ImportFiel注解完成简单的规则校验
*  具体可看源码
*/

public class Demo extends BasicModel {

    @ImportField(required = true,column = "班级名称")
    private String className;
    @ImportField(required = true,column = "学生姓名")
    private String studentName;

}
```

#### 服务的调用
```java
    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileUrl 文件url路径
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody urlToExcelData(int startIndex,String fileUrl, DataLocation dataLocation);

    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileUrl 文件路径
     * @param clazz 校验规则配置类
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody urlToValidateExcelData(int startIndex, String fileUrl, ExcelRuleBuilder clazz, DataLocation dataLocation);
    /**
     * 导出excle
     * @param headData 表头数据
     * @param listData 组装好的行列数据
     * @return
     */
    ResultBody exportExcel(String[] headData,List<List<String>> listData);

    /**
     * 导出excle
     * @param listData 组装好的行列数据
     * @return
     */
    ResultBody exportExcel(List<List<String>> listData);

```
### 导入测试
```java
@SpringBootApplication
public class TextbootExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TextbootExampleApplication.class, args);

        String url = "https://hj-upload.oss-cn-hangzhou.aliyuncs.com/exam/201901/201901281429045528.xlsx";
        TextBootService textBootService = run.getBean(TextBootService.class);


        long a = System.currentTimeMillis();
        String  s = textBootService.ping();
        System.out.println(System.currentTimeMillis()-a);
        System.out.println(s);

        long b = System.currentTimeMillis();
        ResultBody data = textBootService.urlToExcelData(0, url, DataLocation.SYNC);
        System.out.println(System.currentTimeMillis() - b);
        System.out.println(data);

        long c = System.currentTimeMillis();
        ResultBody data1 = textBootService.urlToExcelData(0, url, DataLocation.REDIS);
        System.out.println(System.currentTimeMillis() - c);
        System.out.println(data1);

        long d = System.currentTimeMillis();
        ResultBody data2 = textBootService.urlToValidateExcelData(0, url, ExcelMappingFactory.INSTANCE.builder(Demo.class), DataLocation.SYNC);
        System.out.println(System.currentTimeMillis() - d);
        System.out.println(data2);

    }

}
```
#### 文件的导出
文件的导出关键点在于数据的组装，调用者需要非常清除导出数据组装的格式

* `headData`为表头数据，利用数组特性来确定列的顺序，例如head = {"姓名","年龄","性别","班级"},那么表头的格式即为

| 姓名 | 年龄 | 性别 | 班级 |
| ---- | ---- | ---- | ---- |
|      |      |      |      |

* `listData` 的数据则为内容的组装，按照表头字段的顺序组装成一个`List<String>`,那么多行则就组装成`List<List<String>>` 例如我需要导出张三和李四两个人的上述信息，则格式如下

  ```java
  List<List<String>> dataList = new ArrayList<>();
  List<String> zhangsan = new ArrayList<>();
  zhangsan.add("张三");
  zhangsan.add("20");
  zhangsan.add("男");
  zhangsan.add("三年二班");
  dataList.add(zhangsan);
  List<String> lisi = new ArrayList<>();
  lisi.add("李四");
  lisi.add("21");
  lisi.add("女");
  lisi.add("三年二班");
  dataList.add(lisi);
  ```

  然后调用接口，即可生成一个excel文件，接口将返回文件所在的oss路径

| 姓名 | 年龄 | 性别 | 班级 |
| ---- | ---- | ---- | ---- |
|  张三    | 20     | 男     |三年二班      |
| 李四 | 21 | 女 |三年二班 |


