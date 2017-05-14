/**
 * Created by Bonso on 5/7/2017.
 */
public class TestUtil {
    private static long uniqueId = 0;
    public static String getUniqueString(){
        return ""+uniqueId++;
    }
}
