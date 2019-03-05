package cn.wujw.textboot.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-25
 */
public class TextBootException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public TextBootException(String msg) {
        super(msg);
    }

    public TextBootException(String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
 }
