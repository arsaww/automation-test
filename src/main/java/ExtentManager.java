import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.io.File;
import java.util.Calendar;

public class ExtentManager {

    private ExtentManager() {
        File file = new File("C:\\workspace\\automation-test\\src\\main\\resources\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
    }


    private static ExtentManager INSTANCE = null;
    private ExtentHtmlReporter report = null;
    private String currentTestName = null;
    private CustomDriver currentTestDriver = null;
    private ExtentTest currentTest = null;
    private String reportDirectory = null;
    private ExtentReports extent = new ExtentReports();

    public static synchronized ExtentManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtentManager();
        }
        return INSTANCE;
    }

    public void startReport() {
        reportDirectory = "Report " +
                Calendar.getInstance().get(Calendar.YEAR) + "-" +
                Calendar.getInstance().get(Calendar.MONTH) + "-" +
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " +
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" +
                Calendar.getInstance().get(Calendar.MINUTE) + "-" +
                Calendar.getInstance().get(Calendar.SECOND);
        createReportDirectory(reportDirectory);
        report = new ExtentHtmlReporter(reportDirectory + "/index.html");
    }

    public void endReport() {
        extent.flush();
    }

    public CustomDriver startTest(String testName) {
        if (currentTestDriver == null || !currentTestDriver.isActive()) {
            currentTestName = testName;
            extent.attachReporter(report);
            currentTest = extent.createTest(testName);
            currentTestDriver= new CustomDriver(currentTest, reportDirectory);
        } else {
            throw new RuntimeException("Error while trying to start a new test, the current test [" + currentTestName + "] is in progress, end it first !");
        }
        return currentTestDriver;
    }

    private void createReportDirectory(String directory) {
        File theDir = new File(directory);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

}