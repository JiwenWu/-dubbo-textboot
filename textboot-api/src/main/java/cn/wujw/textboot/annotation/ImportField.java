package cn.wujw.textboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

/**
 * Desc: 导入字段注解
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImportField {
    /**
     * 表头列名
     * @return
     */
    String column() default "";

    /**
     * 属性名
     * @return
     */
    String field() default "";

    /**
     * 是否必填
     * @return
     */
    boolean required() default false;

    /**
     * 日期格式
     * @return
     */
    String dataFormat() default "yyyy-MM-dd HH:mmLss";

    /**
     * 正则校验
     * @return
     */
    String regex() default "";

    /**
     * 正则校验不通过的提示信息
     * @return
     */
    String regexMessage() default "正则校验不通过";

    /**
     * BigDecimal 精度
     * @return
     */
    int scale() default 2;

    /**
     * BigDecimal 舍入规则，默认BigDecimal.ROUND_HALF_EVEN
     * @return
     */
    int roundingMode() default BigDecimal.ROUND_HALF_EVEN;
}
