package com.songyuankun.controller;

import com.songyuankun.common.ResponseObject;
import com.songyuankun.service.impl.ApiService;
import com.songyuankun.service.impl.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("api")
public class ApiController {
    private final ApiService apiService;
    private final ExcelService excelService;

    @Autowired
    public ApiController(ApiService apiService, ExcelService excelService) {
        this.apiService = apiService;
        this.excelService = excelService;
    }

    @RequestMapping("inputExcel")
    public ResponseObject inputExcel(@RequestParam("file") MultipartFile multipartFile) {
        ResponseObject responseObject = new ResponseObject();
        if (multipartFile.isEmpty()) {
            return responseObject;
        }
        apiService.generateResultFile(multipartFile);
        return responseObject;
    }

    @RequestMapping("api")
    public void api(@RequestParam("file") MultipartFile multipartFile, @Param("startTimeString") String startTimeString, @Param("endTimeString") String endTimeString, HttpServletResponse response) {

        if (multipartFile.isEmpty()) {
            return;
        }
        apiService.generateResultFile(multipartFile);
        LocalDateTime startTime = LocalDateTime.parse(startTimeString);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString);
        byte[] excelData = excelService.generateResultFile(startTime, endTime);

        outputStream(response, excelData);
    }

    private void outputStream(HttpServletResponse response, byte[] excelData) {
        try (OutputStream os = response.getOutputStream(); BufferedOutputStream bos = new BufferedOutputStream(os)) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + "excelData.xls");
            bos.write(excelData);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("queryAttendanceByMonth")
    public void queryAttendanceByMonth(HttpServletResponse response, @RequestParam("startTimeString") String startTimeString, @RequestParam("endTimeString") String endTimeString) {
        //2018-02-01T00:00:00
        LocalDateTime startTime = LocalDateTime.parse(startTimeString);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString);
        byte[] excelData = excelService.generateResultFile(startTime, endTime);

        outputStream(response, excelData);
    }

}
