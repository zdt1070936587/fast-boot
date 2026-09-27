package com.fastboot.starter.excel;

import cn.idev.excel.FastExcel;
import cn.idev.excel.annotation.ExcelProperty;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel 导出/导入工具。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public class ExcelSupport {

    public <T> void export(HttpServletResponse response, String fileName, Class<T> clazz, List<T> data)
            throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encoded + ".xlsx");
        FastExcel.write(response.getOutputStream(), clazz).sheet("data").doWrite(data);
    }

    public <T> List<T> importExcel(InputStream inputStream, Class<T> clazz) {
        return FastExcel.read(inputStream).head(clazz).sheet().doReadSync();
    }

    /** 兼容 FastExcel 注解与自定义 {@link ExcelColumn}：优先使用 FastExcel {@link ExcelProperty}。 */
    public static String columnName(ExcelColumn column) {
        return column == null ? "" : column.value();
    }
}
