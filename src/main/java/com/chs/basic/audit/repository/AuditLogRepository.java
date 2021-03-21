package com.chs.basic.audit.repository;

import com.chs.basic.audit.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query(nativeQuery = true,
            value = "SELECT JSON_OBJECT('month',MONTH(time),'amount', COUNT(*)) FROM audit_log GROUP BY MONTH(time)")
    List<String> countByMonth();

    @Query(nativeQuery = true,
            value = "SELECT JSON_OBJECT('month',MONTH,'month_action', JSON_ARRAYAGG(user_action)) from " +
                    "(SELECT MONTH,JSON_OBJECT('user_name',user_name,'user_action', JSON_ARRAYAGG(action_amount)) user_action FROM " +
                    "(SELECT MONTH(TIME) month,user_name,JSON_OBJECT('action',action_type,'amount',COUNT(*)) action_amount FROM audit_log " +
                    "GROUP BY MONTH(time),user_name,action_type) X " +
                    "GROUP BY MONTH,user_name) Y " +
                    "GROUP BY month")
    List<String> getMonthReport();
}
