package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseController;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.service.DatumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebResponseController
@RestController
@RequestMapping("/datum")
public class DatumController {

    @Autowired
    DatumService datumService;

    @GetMapping("/demands/number")
    public Object getDemandsNumber() throws ServiceException {
        return datumService.getDemandsNumber();
    }

    @GetMapping("/colleges/seuerNumber")
    public Object getCollegesSEUerNumber() {
        return datumService.getCollegesSEUerNumber();
    }

    @GetMapping("/total/seuerNumber")
    public Object getTotalSEUerNumber() {
        return datumService.getTotalSEUerNumber();
    }

    @GetMapping("/cities/seuerNumber")
    public Object getCitiesSEUerNumber() {
        return datumService.getCitiesSEUerNumber();
    }

    @GetMapping("/ages/seuerNumber")
    public Object getAgesSEUerNumber() {
        return datumService.getAgesSEUerNumber();
    }

    @GetMapping("/graduated/seuerNumber")
    public Object getGraduatedSEUerNumber() {
        return datumService.getGraduateSEUerNumber();
    }

    @GetMapping("/industries/seuerNumber")
    public Object getIndustriesNumber() {
        return datumService.getIndustriesNumber();
    }

    @GetMapping("/jobs/seuerNumber")
    public Object getJobsNumber() {
        return datumService.getJobsNumber();
    }

    @GetMapping("/Newcomers/seuerNumber")
    public Object getNewcomersNumber() {
        return datumService.getNewcomersNumber();
    }
}
