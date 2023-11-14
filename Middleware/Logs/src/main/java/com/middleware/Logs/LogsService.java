package com.middleware.Logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogsService {
    @Autowired
    public LogsRepository logsRepository;

    public Logs saveLog(Logs log) {
        return logsRepository.save(log);
    }
}