package com.chs.basic.audit.controller;

import com.chs.basic.audit.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditLog")
public class AuditLogController {

    @Autowired
    private AuditLogRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/monthAction")
    @ResponseBody
    public String getActionEachMonth() {
        return repository.countByMonth().toString();
    }

    @GetMapping("/monthReport")
    @ResponseBody
    public String getMonthReport() {
        return repository.getMonthReport().toString();
    }
}
