package com.omniteam.backofisbackend.controller;


import com.omniteam.backofisbackend.base.ResponsePayload;
import com.omniteam.backofisbackend.dto.attibute.AttributeTermDTO;
import com.omniteam.backofisbackend.entity.AttributeTerm;
import com.omniteam.backofisbackend.service.AttributeService;
import com.omniteam.backofisbackend.service.AttributeTermService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/attributes")
public class AttributesController {

    private final AttributeService attributeService;
    private final AttributeTermService attributeTermService;
    @Autowired
    public AttributesController(AttributeService attributeService, AttributeTermService attributeTermService) {
        this.attributeService = attributeService;
        this.attributeTermService = attributeTermService;
    }

    @GetMapping(
            path = "/{attributeid}/getattributeterms"
    )
    public ResponseEntity<DataResult<List<AttributeTermDTO>>> getAttributeTermsByAttributeId(
            @PathVariable(name = "attributeid") int attributeId)  {

        return ResponseEntity.ok(this.attributeTermService.getByAttributeTermByAttribute(attributeId));
    }

}
