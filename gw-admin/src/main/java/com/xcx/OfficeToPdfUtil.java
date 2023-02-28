package com.xcx;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jodconverter.DocumentConverter;
import org.jodconverter.LocalConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.LocalOfficeUtils;
import org.jodconverter.office.OfficeException;
import org.springframework.stereotype.Component;

/**
 * office文档转PDF文档
 */
@Component
public class OfficeToPdfUtil {

    static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    //E:\\LibreOffice 是安装目录
    static LocalOfficeManager localOfficeManager = LocalOfficeManager.builder().officeHome("C:\\Program Files\\LibreOffice").install().build();
    OfficeToPdfUtil() throws OfficeException {
        logger.info("***启动OfficeManager***");
        if(!localOfficeManager.isRunning()){
            localOfficeManager.start();
        }
    }

    /**
     * @Description office转pdf
     * @Param [fromPath--源文件, toPath--目标文件]
     * @return void
     **/
    public static void convertByLocal(File fromPath,File toPath) {
        try {
            logger.info("***开始文件转化pdf***");
            DocumentConverter converter = LocalConverter.builder().officeManager(localOfficeManager).build();
            //使用本地方式转换
            converter.convert(fromPath).to(toPath).execute();
            logger.info("***文件转化pdf成功***");
        } catch (OfficeException officeException) {
            logger.error("文件转换失败，请检查");
            officeException.printStackTrace();
        }finally {
        }
    }

    public static void stopServer() throws OfficeException {
        // Stop the office process
        localOfficeManager.stop();
        logger.info("***停止OfficeManager***");
    }

}
