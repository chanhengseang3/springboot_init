package com.loan24.basic.config.controller;

import com.loan24.basic.config.dto.SystemConfigDTO;
import com.loan24.basic.config.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/system-config")
@RestController
@Api(tags = "SystemConfig API")
public class SystemConfigController {
    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @ApiOperation("Add new data")
    @PostMapping("/save")
    public void save(@RequestBody SystemConfigDTO systemConfig) {
        systemConfigService.save(systemConfig);
    }

    @ApiOperation("Delete based on primary key")
    @GetMapping("/{id}")
    public SystemConfigDTO findById(@PathVariable("id") Long id) {
        Optional<SystemConfigDTO> dtoOptional = systemConfigService.findById(id);
        return dtoOptional.orElse(null);
    }

    @ApiOperation("Find by Id")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        systemConfigService.deleteById(id);
    }

    @ApiOperation("Find all data")
    @GetMapping("/list")
    public List<SystemConfigDTO> list() {
        return systemConfigService.findAll();
    }

    @ApiOperation("Pagination request")
    @GetMapping("/page-query")
    public Page<SystemConfigDTO> pageQuery(Pageable pageable) {
        return systemConfigService.findAll(pageable);
    }

/*    @ApiOperation("Update one data")
    @PutMapping("/update/{id}")
    public SystemConfigDTO update(@RequestBody SystemConfigDTO dto) {
        return systemConfigService.updateById(dto);
    }*/
}