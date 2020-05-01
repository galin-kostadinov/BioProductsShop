package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Log;
import org.gkk.bioshopapp.data.repository.LogRepository;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public LogServiceModel seedLogInDb(LogServiceModel logServiceModel) {
       Log log = this.modelMapper.map(logServiceModel, Log.class);
       return this.modelMapper.map(this.logRepository.saveAndFlush(log), LogServiceModel.class);
    }
}
