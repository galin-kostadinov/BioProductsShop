package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Log;
import org.gkk.bioshopapp.data.repository.LogRepository;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LogServiceModel seedLogInDb(Log logServiceModel) {
        Log log = this.modelMapper.map(logServiceModel, Log.class);
        return this.modelMapper.map(this.logRepository.saveAndFlush(log), LogServiceModel.class);
    }

    @Override
    public void deleteAllByTimeLessThanEqual(LocalDateTime localDateTime) {
        this.logRepository.deleteAllByTimeLessThanEqual(localDateTime);
    }

    @Override
    public Page<LogServiceModel> getPaginated(Pageable pageable) {
        Page<Log> page = this.logRepository.findAll(pageable);
        List<LogServiceModel> result = page.stream()
                .map(e -> modelMapper.map(e, LogServiceModel.class))
                .collect(Collectors.toList());
        return new PageImpl<>(result, page.getPageable(), page.getTotalElements());
    }
}
