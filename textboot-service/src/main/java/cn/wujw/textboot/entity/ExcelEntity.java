package cn.wujw.textboot.entity;


import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class ExcelEntity{
    /**
     * 实体集合
     */
    private List<ExcelPropertyEntity> propertyEntityList;

    public List<ExcelPropertyEntity> getPropertyEntityList() {
        return propertyEntityList;
    }

    public void setPropertyEntityList(List<ExcelPropertyEntity> propertyEntityList) {
        this.propertyEntityList = propertyEntityList;
    }
}
