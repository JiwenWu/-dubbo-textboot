package cn.wujw.textboot.enums;

/**
 * Desc: 文件类型
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-03-01
 */
public enum FileSuffix {
    /**
     * excle
     */
    XLSX("xlsx"),
    /**
     * pdf
     */
    PDF("pdf"),
    /**
     * word
     */
    WORD("docx");

    private String suffix;

    FileSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix(){
        return this.suffix;
    }
}
