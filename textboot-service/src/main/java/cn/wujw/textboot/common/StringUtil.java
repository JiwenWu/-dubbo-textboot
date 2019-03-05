package cn.wujw.textboot.common;


/**
 * Desc: 字符操作工具
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {
    private StringUtil(){}

    public static boolean isBlank(Object object) {
        return object == null || "".equals(object.toString().trim()) || "".equalsIgnoreCase(object.toString().trim());
    }

    public static String converNullToDefalult(Object object,String value){
        if ("".equals(value) && object == null){
            return "";
        }
        if ("0".equals(value) && isBlank(value)){
            return "0";
        }
        return object.toString();
    }

    public static boolean isNumeric(CharSequence charSequence) {
        if (isBlank(charSequence)){
            return false;
        }
        int size = charSequence.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(charSequence.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
