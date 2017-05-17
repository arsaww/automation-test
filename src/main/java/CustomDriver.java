import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bonso on 5/7/2017.
 */
public class CustomDriver {
    private InternetExplorerDriver driver;
    private ExtentTest currentTest;
    private String reportDirectory;
    private boolean active;

    public CustomDriver(ExtentTest test, String directory) {
        active = true;
        reportDirectory = directory;
        currentTest = test;
        driver = new InternetExplorerDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    public void quit(boolean hasError) {
        if (active) {
            active = false;
            if (hasError) {
                captureScreenAndLogDetails(Status.ERROR, "END", "An error occurred");
            } else {
                captureScreenAndLogDetails(Status.PASS, "END", "End of this test scenario");
            }
            driver.quit();
        }
    }

    public void quit() {
        quit(false);
    }

    public void sleep() {
        sleep(500);
    }

    public void sleep(int time) {
        if (active) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                captureScreenAndLogDetails(Status.ERROR, "An error occured while waiting..(" + time + "ms)", "");
                quit(true);
            }
        }
    }

    public void captureScreenAndLogDetails(Status level, String stepName, String details) {
        TakesScreenshot oScn = driver;
        File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
        String picturePath = reportDirectory + "/" + TestUtil.getUniqueString() + ".png";
        File oDest = new File(picturePath);
        try {
            FileUtils.copyFile(oScnShot, oDest);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            String separator = "";
            if (details != null && !"".equals(details)) {
                separator = " : <br />";
            }
            MediaEntityModelProvider mediaModel = MediaEntityBuilder.createScreenCaptureFromPath(oDest.getName()).build();
            currentTest.log(level, stepName + separator + details, mediaModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sleep();
    }

    public void accessUrl(String url) {
        accessUrl(url, false);
    }

    public void accessUrl(String url, boolean showDetails) {
        if (active) {
            String stepName = "Access URL";
            String details = url;
            try {
                driver.get(url);
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, details);
                quit(true);
            }
            this.sleep();
        }
    }

    public WebElement findElement(String cssSelector) {
        return findElements(cssSelector, false).get(0);
    }

    public WebElement findElement(String cssSelector, boolean showDetails) {
        return findElements(cssSelector, showDetails).get(0);
    }

    public List<WebElement> findElements(String cssSelector) {
        return findElements(cssSelector, false);
    }

    public List<WebElement> findElements(String cssSelector, boolean showDetails) {
        if (active) {
            String stepName = "Finding Web Elements";
            String details = cssSelector;
            List<WebElement> elements = null;
            try {
                elements = new WebElementList(this, cssSelector, driver.findElements(By.cssSelector(cssSelector)));
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, e.getMessage());
                quit(true);
            }
            this.sleep();
            return elements;
        }
        return new WebElementList(this, "empty", null);
    }

    public boolean elementExists(String cssSelector) {
        try {
            List<WebElement> elements = driver.findElements(By.cssSelector(cssSelector));
            this.sleep();
            return elements != null && elements.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void writeInTextField(WebElement element, String inputValue) {
        writeInTextField(element, inputValue, false);
    }

    public void writeInTextField(WebElement element, String inputValue, boolean showDetails) {
        if (active) {
            String stepName = "Write in a text field";
            String details = "\"" + inputValue + "\"";
            try {
                element.clear();
                element.sendKeys(inputValue);
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, e.getMessage());
                quit(true);
            }
            this.sleep();
        }
    }

    public void writeInTextField(String cssSelector, String inputValue) {
        writeInTextField(cssSelector, inputValue, false);
    }

    public void writeInTextField(String cssSelector, String inputValue, boolean showDetails) {
        if (active) {
            String stepName = "Write in a text field";
            String details = cssSelector + " => \"" + inputValue + "\"";
            try {
                WebElement element = this.findElement(cssSelector);
                element.clear();
                element.sendKeys(inputValue);
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, e.getMessage());
                quit(true);
            }
            this.sleep();
        }
    }

    public boolean clickOnElement(String cssSelector) {
        return clickOnElement(cssSelector, false);
    }

    public boolean clickOnElement(String cssSelector, boolean showDetails) {
        if (active) {
            String stepName = "Click";
            String details = cssSelector;
            WebElement element = null;
            try {
                driver.findElement(By.cssSelector(cssSelector)).click();
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
                this.sleep();
                return true;
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, details + "<br />" + e.getMessage());
                quit(true);
            }
        }
        return false;
    }

    public void clickOnElement(WebElement element) {
        clickOnElement(element, false);
    }

    public void clickOnElement(WebElement element, boolean showDetails) {
        if (active) {
            String stepName = "Click on the web element";
            try {
                Actions action = new Actions(driver).click(element);
                action.build().perform();
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, "");
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, "" + "<br />" + e.getMessage());
                quit(true);
            }
            this.sleep();
        }
    }

    public void selectOption(String cssSelector, String value) {
        selectOption(cssSelector, value, false);
    }

    public void selectOption(String cssSelector, String value, boolean showDetails) {
        if (active) {
            String stepName = "Click";
            String details = cssSelector + " => " + value;
            WebElement element = null;
            try {
                element = driver.findElement(By.cssSelector(cssSelector));
                Select se = new Select(element);
                se.selectByValue(value);
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, details + "<br />" + e.getMessage());
                quit(true);
            }
            this.sleep();
        }
    }

    public void rightClickOnElement(String cssSelector) {
        rightClickOnElement(cssSelector, false);
    }

    public void rightClickOnElement(String cssSelector, boolean showDetails) {
        if (active) {
            String stepName = "Roght Click";
            String details = cssSelector;
            WebElement element = null;
            try {
                element = driver.findElement(By.cssSelector(cssSelector));
                Actions action = new Actions(driver).contextClick(element);
                action.build().perform();
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, details + "<br />" + e.getMessage());
                quit(true);
            }
            this.sleep();
        }
    }


    public boolean isActive() {
        return active;
    }

    public List<WebElement> findElementsByText(String text) {
        return findElementsByText(text, false);
    }

    public List<WebElement> findElementsByText(String text, boolean showDetails) {
        if (active) {
            String stepName = "Find Web Elements by text";
            String details = text;
            List<WebElement> elements = null;
            try {
                elements = new WebElementList(this,
                        "//*[contains(text(), '" + text + "')]",
                        driver.findElements(By.xpath("//*[contains(text(), '" + text + "')]")));
                if (showDetails) {
                    captureScreenAndLogDetails(Status.INFO, stepName, details);
                }
            } catch (Exception e) {
                captureScreenAndLogDetails(Status.ERROR, stepName, e.getMessage());
                quit(true);
            }
            this.sleep();
            return elements;
        }
        return null;
    }

    public void assertTrue(String info, boolean test) {
        if (active) {
            if (test) {
                captureScreenAndLogDetails(Status.PASS, info, "Success");
            } else {
                captureScreenAndLogDetails(Status.ERROR, info, "Failure");
            }
            this.sleep();
        }
    }

    public void logInformation(String info) {
        if (active) {
            captureScreenAndLogDetails(Status.INFO, info, "");
        }
    }

    public boolean highlight(WebElement e) {
        if (active) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2])", e, "style", "border:4px orange solid;");
            this.sleep();
            return true;
        }
        return false;
    }

}


