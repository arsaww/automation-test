import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest {


    private CustomDriver d;

    public void test() throws InterruptedException {
        d = startTest("Test 1");
        login("nm","master");
        startWorkflow("AutomationTest");
        reassignTask("jfeuillebois", "bruce wayne");
        reopenTask("AutomationTest");
        endTest();
    }

    public CustomDriver startTest(String testName){
        ExtentManager m = ExtentManager.getInstance();
        CustomDriver d = m.startTest(testName);
        return d;
    }

    public void startWorkflow(String name) throws InterruptedException {
        d.accessUrl("http://localhost:8080/process/");
        d.logInformation("Init Workflow...");
        List<WebElement> l = d.findElements(".aw_BorderLayoutManager_east a");
        WebElement e = null;
        for(WebElement n : l){
            if(n.getAttribute("innerHTML").equalsIgnoreCase(name)){
                e = n;
            }
        }
        d.clickOnElement(e);
        d.sleep();
        l = d.findElements("input[type=text]");
        d.writeInTextField(l.get(0),"Jérôme");
        d.writeInTextField(l.get(1),"FEUILLEBOIS");
        d.clickOnElement("#Submit");
        d.sleep();
        l = d.findElements("input[type=number]");
        d.writeInTextField(l.get(0),"10");
        d.writeInTextField(l.get(1),"22");
        l = d.findElements("#Submit");
        d.assertTrue("The Workflow is properly initiated",l.get(0) != null);
        d.clickOnElement(l.get(0));
    }

    public void endTest(){
        d.quit();
    }

    public void login(String login, String password) throws InterruptedException {
        d.accessUrl("http://localhost:8080/admin");
        d.logInformation("Login as "+login+"...");
        d.sleep();
        d.writeInTextField(d.findElement("input[type=text]"),login);
        d.writeInTextField(d.findElement("input[type=password]"),password);
        d.clickOnElement("input[type=submit].loginbutton");
        d.highlight(d.findElement("div.studio_user_name"));
        d.assertTrue("The user is logged as "+login, true);
    }

    public void reassignTask(String from, String to) throws InterruptedException {
        d.accessUrl("http://localhost:8080/admin");
        d.logInformation("Reassigning task...");
        d.clickOnElement(d.findElements("div[data-aw-module=Console]").get(0));
        d.sleep();
        d.clickOnElement(d.findElement("div[data-aw-module=Workitems]"));
        while(d.elementExists("#mod_console #mod_workitems .aw_widgets_datatable_FilterPanel_rule_clear")){
            d.sleep();
        }
        d.clickOnElement("#mod_console #mod_workitems .aw_widgets_datatable_FilterPanel_add_button");
        d.clickOnElement(".aw_widgets_popup_dialog_Dialog div:nth-child(1) div:nth-child(4)");
        d.writeInTextField(d.findElement(".aw_widgets_datatable_filter_TextFilter_input"),from);
        d.clickOnElement(".aw_widgets_datatable_FilterDialog_apply_button");
        int i = 0;
        while(!d.elementExists("#mod_console #mod_workitems div.aw_widgets_datatable_HeaderPanel .aw_widgets_datatable_HeaderPanel_table tr " +
                "td:nth-child(2) .aw_widgets_datatable_HeaderPanel_label .aw_widgets_datatable_HeaderPanel_desc") && i < 3){
            d.clickOnElement("#mod_console #mod_workitems div.aw_widgets_datatable_HeaderPanel .aw_widgets_datatable_HeaderPanel_table tr " +
                    "td:nth-child(2) .aw_widgets_datatable_HeaderPanel_label");
            i++;
        }
        d.sleep();
        d.rightClickOnElement("#mod_console #mod_workitems div.aw_widgets_datatable_ContentPanel .aw_widgets_datatable_ContentPanel_page " +
                "tbody tr:nth-child(1) td:nth-child(1)");
        d.clickOnElement(".popup_menu .item.enabled:nth-child(1)");
        d.clickOnElement(".modal_window input[type=radio]:nth-child(1)");
        d.selectOption(".modal_window select","NM");
        d.logInformation("Capture screen before task reassignment");
        d.assertTrue("Task reassignment",d.clickOnElement(".modal_window button:nth-child(1)"));

    }

    public void reopenTask(String taskName){
        d.accessUrl("http://localhost:8080/process/");
        d.logInformation("Reopening task...");
        d.assertTrue("Task Reopen", d.clickOnElement(".wil-content td[title="+taskName+"]"));
    }
}
