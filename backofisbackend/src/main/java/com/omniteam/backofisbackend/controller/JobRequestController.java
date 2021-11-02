package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.service.JobRequestService;
import com.omniteam.backofisbackend.service.implementation.JobRequestServiceImpl;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/jobRequests")
public class JobRequestController {

    JobRequestService jobRequestService;

    @Autowired
    public JobRequestController(JobRequestService requestService)
    {
        jobRequestService=requestService;
    }

   @GetMapping("/getall")
    public ResponseEntity getAllJobRequests(Pageable pageable)
    {
        return ResponseEntity.ok(this.jobRequestService.getAll(pageable));
    }


}
