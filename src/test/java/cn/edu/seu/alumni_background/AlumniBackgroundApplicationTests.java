package cn.edu.seu.alumni_background;

import cn.edu.seu.alumni_background.component.cos.COSManager;
import cn.edu.seu.alumni_background.component.redis.StringToObjectRedisStringManager;
import cn.edu.seu.alumni_background.controller.DatumController;
import cn.edu.seu.alumni_background.service.DatumService;
import cn.edu.seu.alumni_background.service.LocalService;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.rowset.serial.SerialException;

@SpringBootTest
class AlumniBackgroundApplicationTests {

    @Autowired
    private COSManager cosManager;

    @Autowired
    private LocalService localService;

    @Autowired
    StringToObjectRedisStringManager stringToObjectRedisManager;

    Logger logger = LoggerFactory.getLogger(ConsoleAppender.class);

    @Autowired
    DatumController datumController;

    @Test
    void contextLoads() {
        System.out.println(datumController.getCitiesSEUerNumber());
    }
}
