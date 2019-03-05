package cn.wujw.textboot.excel.parser;

import cn.wujw.textboot.common.Constant;
import cn.wujw.textboot.common.DateFormatUtil;
import cn.wujw.textboot.common.RegexUtil;
import cn.wujw.textboot.enums.XssfDataType;
import cn.wujw.textboot.exception.TextBootException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static cn.wujw.textboot.common.Constant.DATE_FORMAT_STR;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-21
 */
public abstract class ExcelReaderAbstract extends DefaultHandler {

    private static Logger logger = LoggerFactory.getLogger(ExcelReaderAbstract.class);

    private SharedStringsTable sharedStringsTable;
    private short formatIndex;
    private String readValue = "";
    private StylesTable stylesTable;
    private int currentRowIndex = 0;
    private String curCellName = "";
    private int currentSheetIndex = -1;
    private XssfDataType dataType;

    private Map<String,String> rowValueMap = new HashMap<>();


    /**
     * 处理单行数据的回调方法
     * @param curRow 当前行号
     * @param rowValueMap 当前行的值
     */
    public abstract void optRows(int curRow,Map<String,String> rowValueMap);

    public void excelReader(String fileName) throws Exception {
        InputStream inputStream = new FileInputStream(fileName);
        this.excelReader(inputStream);
        File file = new File(fileName);
        file.deleteOnExit();
    }

    public void excelReader(InputStream inputStream) throws Exception {
        OPCPackage opcPackage = null;
        InputStream sheet = null;
        InputSource sheetSource = null;

        try {
            opcPackage = OPCPackage.open(inputStream);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable stylesTable = xssfReader.getStylesTable();
            XMLReader parser = this.getSheetParser(xssfReader.getSharedStringsTable(),stylesTable);
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            // 迭代多个sheet,不直接指定第一个sheet rId1
            while (sheets.hasNext()) {
                currentRowIndex = 0;
                currentSheetIndex ++;

                try {
                    sheet = sheets.next();
                    sheetSource = new InputSource(sheet);

                    try {
                        logger.info("开始读取第{}个Sheet!",currentSheetIndex + 1);
                        parser.parse(sheetSource);
                    }catch (Exception e){
                        throw new Exception(e);
                    }
                }finally {
                    if (sheet != null){
                        try {
                            sheet.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }finally {
            if (opcPackage != null){
                try {
                    opcPackage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private XMLReader getSheetParser(SharedStringsTable sharedStringsTable,StylesTable stylesTable) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sharedStringsTable = sharedStringsTable;
        this.stylesTable = stylesTable;
        parser.setContentHandler(this);
        return parser;
    }

    /**
     * 开始读取第一个标签
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes){
        // c => 单元格
        if (Constant.CELL.equals(name)) {
            // 单元格数据类型
            String cellType = attributes.getValue("t");
            String cellStyle = attributes.getValue("s");

            this.dataType = XssfDataType.NUMBER;
            if (Constant.CELL_TYPE_BOOL.equals(cellType)){
                this.dataType = XssfDataType.BOOL;
            } else if (Constant.CELL_TYPE_ERROR.equals(cellType)){
                this.dataType = XssfDataType.ERROR;
            } else if (Constant.CELL_TYPE_FORMULA.equals(cellType)){
                this.dataType = XssfDataType.FORMULA;
            } else if (Constant.CELL_TYPE_SSTINDEX.equals(cellType)){
                this.dataType = XssfDataType.SSTINDEX;
            }else if (cellStyle != null) {
                int styleIndex = Integer.parseInt(cellStyle);
                XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                this.formatIndex = style.getDataFormat();
            }
        }

        //记录当前读取单元格的名称
        String cellName = attributes.getValue("r");
        if (cellName != null && !cellName.isEmpty()) {
            curCellName = cellName;
        }
        // 置空
        readValue = "";

    }


    /**
     * 结束读取第一个标签
     * @param uri
     * @param localName
     * @param name
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String name){
        if (Constant.CELL_VALUE.equals(name)) {
            switch (this.dataType) {
                case BOOL: {
                    char first = readValue.charAt(0);
                    readValue = first == '0' ? "FALSE" : "TRUE";
                    break;
                }
                case ERROR: {
                    readValue = "ERROR:" + readValue;
                    break;
                }
                case INLINESTR: {
                    readValue = new XSSFRichTextString(readValue).toString();
                    break;
                }
                case SSTINDEX: {
                    int idx = Integer.parseInt(readValue);
                    readValue = new XSSFRichTextString(sharedStringsTable.getItemAt(idx).getString()).toString();
                    break;
                }
                case FORMULA: {
                    readValue = readValue;
                    break;
                }
                case NUMBER: {
                    // 判断是否是日期格式
                    if (HSSFDateUtil.isADateFormat(formatIndex, readValue)) {
                        Double d = Double.parseDouble(readValue);
                        Date date = HSSFDateUtil.getJavaDate(d);
                        try {
                            readValue = DateFormatUtil.format(DATE_FORMAT_STR,date);
                        } catch (ExecutionException e) {
                            readValue = DateFormatUtils.format(date,DATE_FORMAT_STR);
                        }
                    } else {
                        readValue = readValue;
                    }
                    break;
                }
                default: {
                    readValue = new XSSFRichTextString(readValue).toString();
                }
                break;
            }
            String value = readValue.trim();
            rowValueMap.put(curCellName, value);
        }

        // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
        if (Constant.CELL_ROW.equals(name)) {
            if (!rowValueMap.isEmpty()) {
                currentRowIndex++;
                String a = rowValueMap.keySet().toArray()[0].toString();
                if (currentRowIndex != RegexUtil.removeLetter(a)){
                   throw new TextBootException("第" + currentRowIndex + "出现空行");
                }
                optRows(currentRowIndex, rowValueMap);
                rowValueMap.clear();
            }
        }
    }

    /**
     * 加载V标签中间的值
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length){
        readValue = readValue.concat(new String(ch, start, length));
    }
}
