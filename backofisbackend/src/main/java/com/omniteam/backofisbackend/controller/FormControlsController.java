package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.dto.formcontrol.FormControlDto;
import com.omniteam.backofisbackend.entity.FormControl;
import com.omniteam.backofisbackend.service.FormControlService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/formcontrols")
@RequiredArgsConstructor
public class FormControlsController {
    private final FormControlService formControlService;

    @GetMapping(path = "/getformcontrols/{formid}")
    public ResponseEntity<DataResult<List<FormControlDto>>> getFormControlsByFormId(
            @PathVariable(name = "formid", required = true) Integer formId
    ) {
        return ResponseEntity.ok(this.formControlService.getFormControlsByFormId(formId));
    }
}
