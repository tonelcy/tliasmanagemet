package com.lcy.service;

import com.lcy.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {

    JobOption getEmpJobData();

    List<Map> getEmpGenderData();
}
