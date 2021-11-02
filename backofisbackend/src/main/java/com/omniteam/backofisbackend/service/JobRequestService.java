package com.omniteam.backofisbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestAddDto;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestUpdateDto;
import com.omniteam.backofisbackend.entity.JobRequest;
import com.omniteam.backofisbackend.entity.RequestStatus;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.data.domain.Pageable;

public interface JobRequestService {
    DataResult<PagedDataWrapper<JobRequest>> getAll(Pageable pageable);

    Result add(JobRequestAddDto jobRequestAdd);

    Result update(JobRequestUpdateDto jobRequestUpdate) throws JsonProcessingException;

    Result setStatus(Integer JobRequestId, RequestStatus requestStatus) throws JsonProcessingException;


}
