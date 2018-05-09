package com.songyuankun.service.impl;

import com.songyuankun.common.Constant;
import com.songyuankun.entity.Attendance;
import com.songyuankun.repository.AttendanceRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class ExcelService {
    private static final List<String> WEEKS = Arrays.asList("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public ExcelService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }


    public byte[] generateResultFile(LocalDateTime startTimeLocalDateTime, LocalDateTime endTimeLocalDateTime) {
        Date startTime = Date.from(startTimeLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(endTimeLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        List<Attendance> attendanceList = attendanceRepository.findAllByDate(startTime, endTime);
        //定义一个新的工作簿
        Workbook wb = new HSSFWorkbook();
        //创建sheet1
        Sheet sheet = wb.createSheet("sheet1");
        //创建行
        Row row0 = sheet.createRow(0);
        Row row1 = sheet.createRow(1);
        //创建单元格
        row0.createCell(0).setCellValue("部门");
        row0.createCell(1).setCellValue("姓名");
        row0.createCell(2).setCellValue("日期");
        Calendar calendar = Calendar.getInstance();
        ZoneId zone = ZoneId.systemDefault();
        calendar.setTime(startTime);
        Integer monthNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= monthNum; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            row0.createCell(i + 2).setCellValue(i);
            row1.createCell(i + 2).setCellValue(WEEKS.get(calendar.get(Calendar.DAY_OF_WEEK)));
        }

        Map<String, Integer> userRowMap = new HashMap<>(16);
        Integer rowNum = 3;
        for (Attendance attendance : attendanceList) {
            String department = attendance.getUser().getDepartment();
            String userName = attendance.getUser().getName();
            Date dateTime = attendance.getDate();
            Instant dateTimeInstant = dateTime.toInstant();
            LocalDateTime localDateTime = dateTimeInstant.atZone(zone).toLocalDateTime();
            Integer day = localDateTime.getDayOfMonth();
            if (!userRowMap.containsKey(userName)) {
                userRowMap.put(userName, rowNum);
                rowNum++;
            }
            Row row3 = sheet.getRow(userRowMap.get(userName));
            if (row3 == null) {
                row3 = sheet.createRow(userRowMap.get(userName));
                row3.createCell(0).setCellValue(department);
                row3.createCell(1).setCellValue(userName);
            }

            Cell cell = row3.getCell(2 + day);
            if (cell == null) {
                cell = row3.createCell(2 + day);
                cell.setCellValue("√早退");
            }
            if (localDateTime.getHour() >= 18) {
                String status = cell.getStringCellValue();
                status = status.replace("早退", "");
                cell.setCellValue(status);
            }
        }

        byte[] contentBytes = new byte[0];
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            wb.write(os);
            contentBytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBytes;
    }

    /**
     * 读取excel
     *
     * @param multipartFile 文件
     * @return Workbook
     */
    Workbook readExcel(MultipartFile multipartFile) {
        String extString = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        try (InputStream fileInputStream = multipartFile.getInputStream()) {
            if (Constant.FILE_SUFFIX_XLS.equals(extString)) {
                return new HSSFWorkbook(fileInputStream);
            } else if (Constant.FILE_SUFFIX_XLSX.equals(extString)) {
                return new XSSFWorkbook(fileInputStream);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
