package cn.wujw.textboot.example.model;

import cn.wujw.textboot.annotation.ImportField;
import cn.wujw.textboot.model.BasicModel;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-28
 */
public class Demo extends BasicModel {

    @ImportField(required = true,column = "一")
    private String one;
    @ImportField(required = true,column = "二")
    private String two;
    @ImportField(required = true,column = "三")
    private String three;
    @ImportField(required = true,column = "四")
    private String four;
    @ImportField(required = true,column = "五")
    private String five;
    @ImportField(required = true,column = "六")
    private String six;
    @ImportField(required = true,column = "七")
    private String seven;
    @ImportField(required = true,column = "八")
    private String eight;
    @ImportField(required = true,column = "九")
    private String nine;
    @ImportField(required = true,column = "十")
    private String ten;

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getSeven() {
        return seven;
    }

    public void setSeven(String seven) {
        this.seven = seven;
    }

    public String getEight() {
        return eight;
    }

    public void setEight(String eight) {
        this.eight = eight;
    }

    public String getNine() {
        return nine;
    }

    public void setNine(String nine) {
        this.nine = nine;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "one='" + one + '\'' +
                ", two='" + two + '\'' +
                ", three='" + three + '\'' +
                ", four='" + four + '\'' +
                ", five='" + five + '\'' +
                ", six='" + six + '\'' +
                ", seven='" + seven + '\'' +
                ", eight='" + eight + '\'' +
                ", nine='" + nine + '\'' +
                ", ten='" + ten + '\'' +
                '}';
    }
}
