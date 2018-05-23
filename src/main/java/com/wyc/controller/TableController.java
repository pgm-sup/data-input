package com.wyc.controller;

import com.alibaba.fastjson.JSONObject;
import com.wyc.service.TableService;
import com.wyc.utils.ImportExcelUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author haima
 */
@Controller
@RequestMapping("/table")
public class TableController {

    @Autowired
    TableService tableService;

    @RequestMapping(value = "/index")
    public Object index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        return mv;
    }

    @RequestMapping(value = "/upload")
    public Object upload(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("upload");
        return mv;
    }
    @ResponseBody
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
        try {
            System.out.println(tableService.createTable(tableName, headers));
        }catch (Exception e){
            jsonResult.put("code", "0");
            jsonResult.put("msg", "失败，不允许表头存在关键字");
            return jsonResult.toJSONString();
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
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);


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


    @RequestMapping(method = RequestMethod.POST ,value="/uploadExcel")
    @ResponseBody
    public String uploadExcel(MultipartFile myFile){
        String flag ="0";
        try{
            InputStream in = myFile.getInputStream();
            String originalFilename = myFile.getOriginalFilename();
            String tableName = originalFilename.substring(0,originalFilename.indexOf("."));
            //这里得到的是一个集合，里面的每一个元素是String[]数组
            List<Map<String, Object>> list = ImportExcelUtil.parseExcel(in,originalFilename);
            //service实现方法
            tableService.saveData(list, tableName);
        } catch(Exception e){
            e.printStackTrace();
            flag = "1";
        }
        return flag;
    }
}
