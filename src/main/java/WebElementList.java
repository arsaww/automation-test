import com.aventstack.extentreports.Status;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.*;

/**
 * Created by Bonso on 5/13/2017.
 */
public class WebElementList implements List<WebElement> {

    private List<WebElement> list;
    private String cssSelector;
    private CustomDriver driver;

    public WebElementList(CustomDriver driver, String cssSelector, List<WebElement> list) {
        this.driver = driver;
        this.list = list != null ? list : new ArrayList<WebElement>();
        this.cssSelector = cssSelector;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    public Iterator<WebElement> iterator() {
        return this.list.iterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    public boolean add(WebElement webElement) {
        return this.list.add(webElement);
    }

    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    public boolean addAll(Collection<? extends WebElement> c) {
        return this.list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends WebElement> c) {
        return this.list.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    public void clear() {
        this.list.clear();
    }

    public WebElement get(int index) {
        if (driver.isActive()) {
            try {
                return this.list.get(index);
            } catch (Exception e) {
                driver.captureScreenAndLogDetails(Status.ERROR,
                        "Impossible to get the WebElement at index '" + index + "' from a collection of " + list.size() + " elements",
                        "The css selector of the list was : " + this.cssSelector);
                driver.quit(true);
                return null;
            }
        }
        return null;
    }

    public WebElement set(int index, WebElement element) {
        return this.list.set(index, element);
    }

    public void add(int index, WebElement element) {
        this.list.add(index, element);
    }

    public WebElement remove(int index) {
        return this.list.remove(index);
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator<WebElement> listIterator() {
        return this.list.listIterator();
    }

    public ListIterator<WebElement> listIterator(int index) {
        return this.list.listIterator(index);
    }

    public List<WebElement> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }


}
