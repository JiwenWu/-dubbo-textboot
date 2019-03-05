package cn.wujw.textboot.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class DubboProvider {
    public static void main(String[] args) throws Exception {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/application.xml","classpath:META-INF/spring/dubbo-config.xml");
        context.start();
        System.out.println("DubboProvider start success");
        System.in.read(); // press any key to exit
    }
}
