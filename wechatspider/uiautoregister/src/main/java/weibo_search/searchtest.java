package weibo_search;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.common.SuperRunner;

public class searchtest extends SuperRunner {

    public void test_run() {
        try {
            Device = getUiDevice();
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("聊天"));
            if (ob.waitForExists(2000)) {
                ob.click();
                sleep(2000);
            }
            Device.click(981, 129);
            sleep(2000);
            Device.pressBack();
            sleep(2000);
            ob = new UiObject(new UiSelector().className("android.widget.EditText"));
            ob.setText("1111");
            System.out.println("执行结束");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
