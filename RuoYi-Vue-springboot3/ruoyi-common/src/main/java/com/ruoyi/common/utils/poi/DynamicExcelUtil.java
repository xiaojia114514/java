package com.ruoyi.common.utils.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 动态Excel工具类
 * 用于处理从Markdown表格或其他动态数据生成Excel文件
 * 
 * @author ruoyi
 */
public class DynamicExcelUtil {

    /**
     * 生成Excel文件
     * 
     * @param tableData 表格数据，第一行作为表头
     * @param sheetName 工作表名称
     * @param filePath 文件保存路径
     * @throws IOException IO异常
     */
    public static void generateExcel(List<List<String>> tableData, String sheetName, String filePath) throws IOException {
        // 创建工作簿和工作表
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建单元格样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 写入数据
        for (int i = 0; i < tableData.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> rowData = tableData.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData.get(j));
                cell.setCellStyle(style);
                // 自动调整列宽
                sheet.autoSizeColumn(j);
            }
        }

        // 确保目录存在
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 保存文件
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }

    /**
     * 生成Excel文件并返回文件路径
     * 
     * @param tableData 表格数据，第一行作为表头
     * @param sheetName 工作表名称
     * @param directory 保存目录
     * @param fileName 文件名（不带扩展名）
     * @return 文件完整路径
     * @throws IOException IO异常
     */
    public static String generateExcel(List<List<String>> tableData, String sheetName, String directory, String fileName) throws IOException {
        String filePath = directory + File.separator + fileName + ".xlsx";
        generateExcel(tableData, sheetName, filePath);
        return filePath;
    }
}