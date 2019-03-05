package cn.wujw.textboot.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Desc:普通类调用Spring注解方式的Service层bean
 *      获取Dubbo服务对象
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2018-11-23
 */
@Component
public class SpringBeanFactoryUtils implements ApplicationContextAware {

    private static ApplicationContext application;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = applicationContext;
    }

    /**
     * 这是一个便利的方法，帮助我们快速得到一个BEAN
     *
     * @param beanName bean的名字
     * @return 返回一个bean对象
     * @author hzc
     */
    public static Object getBean(String beanName) {
        return application.getBean(beanName);
    }

    /**
     * 根据类获取
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return application.getBean(clazz);
    }

}
