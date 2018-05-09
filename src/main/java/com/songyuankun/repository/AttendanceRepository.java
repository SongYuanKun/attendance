package com.songyuankun.repository;

import com.songyuankun.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    /**
     * 查询打卡记录
     *
     * @param userId 编号
     * @param date   时间
     * @return 打卡记录
     */
    @Query(value = " from Attendance t1 left join t1.user t2  where t2.id=:user_id and t1.date=:date")
    Attendance findByUserNumberAndDate(@Param("user_id") Integer userId, @Param("date") Date date);

    /**
     * 按日期查询打卡记录
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 数组
     */
    @Query(value = "from Attendance t1 join fetch t1.user t2  where  t1.date between :startDate and :endDate order by t2.department")
    List<Attendance> findAllByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}