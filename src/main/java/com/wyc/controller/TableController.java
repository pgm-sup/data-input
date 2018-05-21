package com.wyc.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author haima
 */
@Controller
@RequestMapping("/table")
public class TableController {

    @RequestMapping(value = "/index")
    public Object index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        return mv;
    }

    @RequestMapping(value="/SubmitExcelData",method = RequestMethod.POST)
    public String createTable(HttpServletRequest request) throws IOException {
        // 设置响应和请求编码utf-8
        request.setCharacterEncoding("UTF-8");


        JSONObject json= JSONObject.parseObject(request.getParameter("data"));
        String tableName = json.getString("tableName");
        List<String> headers  = (List<String>) json.get("columns");
        // 返回界面端的json对象
        JSONObject jsonResult=new JSONObject();
        if (null == headers || headers.size()==0) {
            jsonResult.put("code","0");
            jsonResult.put("msg","失败，传入参数为空");
            return jsonResult.toString();
        }
        // 将参数信息存入session中
        HttpSession session = request.getSession();
        try {
            session.setAttribute("tableName", tableName);
            session.setAttribute("param", headers);
            jsonResult.put("code","1");
            jsonResult.put("msg","成功！");
        } catch (Exception e) {
            jsonResult.put("code","0");
            jsonResult.put("msg","失败，失败原因："+e.getMessage());
            return jsonResult.toString();
        }
        return jsonResult.toString();
    }

    @RequestMapping(value="/DownloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {
        // 设置响应和请求编码utf-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String tableName = (String) session.getAttribute("tableName");
        List headers = (List) session.getAttribute("param");
        if (headers == null||headers.isEmpty()) {
            response.getWriter().write("失败，失败原因：参数为空！");
            return;
        }else if(tableName == null || "".equals(tableName)){
            response.getWriter().write("失败，失败原因：表名为空！");
            return;
        }
        //创建Excel

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(tableName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);


        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.size(); i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString((String) headers.get(i));
            cell.setCellValue(text);
        }


        //region 设置返回头文件
        // 默认Excel名称
        String strFileName= tableName + ".xls";
        response.setContentType("application/octet-stream; charset=utf-8");
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
            // firefox浏览器
            response.setHeader("Content-Disposition", "attachment; filename="
                    +  new String(strFileName.getBytes("UTF-8"), "ISO8859-1"));
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
            // IE浏览器
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(strFileName, "UTF-8"));
        }else{
            response.setHeader("Content-disposition", "attachment;filename="+strFileName);
        }
        try {
            // endregion
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }catch (Exception e) {
            response.getWriter().write("失败，失败原因："+e.getMessage());
        }
    }
}
