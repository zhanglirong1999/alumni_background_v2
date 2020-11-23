package cn.edu.seu.alumni_background.service;

import cn.edu.seu.alumni_background.error.ServiceException;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
public interface DatumService {

    Map<String, Double> getDemandsNumber() throws ServiceException;

    Map<String, Long> getCollegesSEUerNumber();

    Long getTotalSEUerNumber();

    Map<String, Long> getCitiesSEUerNumber();

    Map<String, Long> getAgesSEUerNumber();

    Map<String, Long> getGraduateSEUerNumber();

    Map<String, Long> getIndustriesNumber();

    Map<String, Long> getJobsNumber();

    Map<String, Long> getNewcomersNumber();
}
