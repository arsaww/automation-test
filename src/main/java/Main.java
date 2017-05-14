
public class Main {

    public static void main(String[] args) throws InterruptedException {
        /*startTestReport();


        Assert.assertTrue("azeaze" != null);
        // creates a toggle for the given test, adds all log events under it
        ExtentTest test = extent.startTest("My First Test", "Sample description");

        // log(LogStatus, details)
        test.log(LogStatus.INFO, "This step shows usage of log(logStatus, details)");

        // report with snapshot


        // end test
        extent.endTest(test);

        endTestReport();*/
        ExtentManager m = ExtentManager.getInstance();
        m.startReport();


        FirstTest f = new FirstTest();
        f.test();



        m.endReport();

    }

}
