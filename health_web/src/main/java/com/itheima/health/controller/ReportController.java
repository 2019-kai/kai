package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(String[] dates) {
        try {
            Date begin = DateUtils.parseString2Date(dates[0],"yyyy-MM");
            Date end = DateUtils.parseString2Date(dates[1],"yyyy-MM");
            List<String> months = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(begin);
            while (calendar.getTime().getTime() <= end.getTime()){
                String month = DateUtils.parseDate2String(calendar.getTime(), "yyyy-MM");
                months.add(month);
                calendar.add(Calendar.MONTH, 1);
            }
            List<Integer> list = memberService.findCountByRegTime(months);
            Map<String, Object> map = new HashMap<>();
            map.put("months", months);
            map.put("memberCount", list);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            List<Map<String, Object>> list = setmealService.getSetmealReport();

            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : list) {
                setmealNames.add((String) map.get("name"));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", list);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSexReport")
    public Result getSexReport() {
        try {
            List<Map<String, Object>> list = memberService.getSexReport();

            List<String> memberSexNames = new ArrayList<>();
            for (Map<String, Object> map : list) {
                memberSexNames.add((String) map.get("name"));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("memberSexNames", memberSexNames);
            map.put("memberSexCount", list);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getAgeReport")
    public Result getAgeReport() {
        try {

            List<Map<String, Object>> list = memberService.getAgeReport();
            List<String> memberAgeNames = new ArrayList<>();
            for (Map<String, Object> map : list) {
                memberAgeNames.add((String) map.get("name"));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("memberAgeNames", memberAgeNames);
            map.put("memberAgeCount", list);

            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            String reportDate = (String)map.get("reportDate");
            Integer todayNewMember = (Integer)map.get("todayNewMember");
            Integer totalMember = (Integer)map.get("totalMember");
            Integer thisWeekNewMember = (Integer)map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer)map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer)map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer)map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer)map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer)map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer)map.get("thisMonthVisitsNumber");
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>)map.get("hotSetmeal");
            //获取report_template.xlsx路径
            String path = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            //创建工作簿
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File(path));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            Row row = null;
            row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            int i = 12;
            for (Map<String, Object> objectMap : hotSetmeal) {
                row = sheet.getRow(i++);
                row.getCell(4).setCellValue((String) objectMap.get("name"));
                row.getCell(5).setCellValue((Long) objectMap.get("setmeal_count"));
                row.getCell(6).setCellValue(((BigDecimal) objectMap.get("proportion")).doubleValue());
            }

            //通过输出流进行文件下载
            ServletOutputStream os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(os);

            //关闭资源
            os.close();
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }

    }
}
