package com.loan24.basic.keyvalue.controller;

import com.loan24.basic.keyvalue.dto.KeyValueDTO;
import com.loan24.basic.keyvalue.service.KeyValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/key-value")
@RestController
@Api(tags = "KeyValue API")
public class KeyValueController {
    private final KeyValueService keyValueService;

    public KeyValueController(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @ApiOperation("Add new data")
    @PostMapping("/save")
    public void save(@RequestBody KeyValueDTO keyValue) {
        keyValueService.save(keyValue);
    }

    @ApiOperation("Delete based on primary key")
    @GetMapping("/{id}")
    public KeyValueDTO findById(@PathVariable("id") Long id) {
        Optional<KeyValueDTO> dtoOptional = keyValueService.findById(id);
        return dtoOptional.orElse(null);
    }

    @ApiOperation("Find by Id")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        keyValueService.deleteById(id);
    }

    @ApiOperation("Find all data")
    @GetMapping("/list")
    public List<KeyValueDTO> list() {
        return keyValueService.findAll();
    }

    @ApiOperation("Pagination request")
    @GetMapping("/page-query")
    public Page<KeyValueDTO> pageQuery(Pageable pageable) {
        return keyValueService.findAll(pageable);
    }

/*    @ApiOperation("Update one data")
    @PutMapping("/update/{id}")
    public KeyValueDTO update(@RequestBody KeyValueDTO dto) {
        return keyValueService.updateById(dto);
    }*/
}