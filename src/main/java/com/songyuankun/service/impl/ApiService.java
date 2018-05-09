package com.songyuankun.service.impl;

import com.songyuankun.entity.Attendance;
import com.songyuankun.entity.User;
import com.songyuankun.repository.AttendanceRepository;
import com.songyuankun.repository.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 主要业务逻辑
 *
 * @author Administrator
 */
@Service
public class ApiService {


    @Autowired
    private ExcelService excelService;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserRepository userRepository;



    /**
     * 生成结果文件
     *
     * @param multipartFile 源文件
     */
    public void generateResultFile(MultipartFile multipartFile) {
        Workbook workbook = excelService.readExcel(multipartFile);

        // 获得第一个工作表对象
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        int i = 1;
        while (i <= rowNum) {
            Row row = sheet.getRow(i);
            String department = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            String number = row.getCell(2).getStringCellValue();
            Date date = row.getCell(3).getDateCellValue();
            User userSave = userRepository.findByUserId(number);
            if (userSave == null) {
                userRepository.save(new User(number, name, department));
            }
            userSave = userRepository.findByUserId(number);
            Attendance save = attendanceRepository.findByUserNumberAndDate(userSave.getId(), date);
            if (save == null) {
                attendanceRepository.save(new Attendance(userSave.getId(), date));
            }
            i++;
        }
    }
}
