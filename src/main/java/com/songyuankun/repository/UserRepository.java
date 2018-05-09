package com.songyuankun.repository;

import com.songyuankun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Administrator
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 通过userId查询用户
     *
     * @param userId userId
     * @return User
     */
    @Query(value = "from User u where u.number=:userId")
    User findByUserId(@Param("userId") String userId);


}