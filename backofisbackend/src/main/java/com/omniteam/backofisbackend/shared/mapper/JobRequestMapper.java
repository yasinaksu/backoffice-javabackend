package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.jobrequest.JobRequestAddDto;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestDto;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestUpdateDto;
import com.omniteam.backofisbackend.entity.JobRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface JobRequestMapper {
    JobRequestDto toJobRequestDto(JobRequest jobRequest);
    JobRequest toJobRequest(JobRequestDto jobRequest);
    JobRequest toJobRequestAdd(JobRequestAddDto jobRequest);

    void update(@MappingTarget JobRequest target, JobRequestUpdateDto updateDto);

    List<JobRequestDto> toJobRequestDtoList(List<JobRequest> jobRequestList);
}
