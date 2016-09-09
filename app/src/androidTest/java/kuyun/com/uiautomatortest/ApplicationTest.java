package kuyun.com.uiautomatortest;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.view.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ApplicationTest {

    private UiDevice mDevice;

    @Before
    public void before() throws InterruptedException {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        assertThat(mDevice, notNullValue());

    }

    //进入直播
    public void enterBroadCast() throws InterruptedException {
        mDevice.pressHome();
        Thread.sleep(2000);
        mDevice.pressEnter();
        waitForObject(By.res("com.xiaomi.mitv.tvplayer:id/root_view"));
        Thread.sleep(7000);//等识台
    }


    @org.junit.Test
    public void GRZX_0001() throws InterruptedException, UiObjectNotFoundException {
        enterPersonalCenter();
        takeScreenshot("GRZX_0001");
        UiScrollable scroll = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!scroll.scrollIntoView(new UiSelector().textContains("绑定手机号"))) {
            fail();
        }
        if (scroll.scrollIntoView(new UiSelector().textContains("支付宝授权"))) {
            mDevice.findObject(By.text("支付宝授权")).click();
            waitForObject(By.text("支付宝授权"));
            takeScreenshot("支付宝授权.png");
            mDevice.pressBack();

        } else {
            fail();
        }
        //回到首页
        waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rootL"));
        if (scroll.scrollIntoView(new UiSelector().textContains("我的卡券"))) {
            mDevice.findObject(By.text("我的卡券")).click();
            Thread.sleep(3000);
            String cardNum = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/mycardcouponsNum")).getText();
            int totalCount = Integer.parseInt(cardNum);
            UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
            int unchecked = 0;
            int checked = 0;
            while (listView.scrollForward()) {
                unchecked++;
            }
            mDevice.pressDPadRight();
            Thread.sleep(2000);
            while (listView.scrollForward()) {
                checked++;
            }
            Log.d("uiautomatorTest", "unChecked=" + unchecked);
            Log.d("uiautomatorTest", "checked=" + checked);
            //assertEquals(totalCount, unchecked + checked);
            mDevice.pressBack();
        } else {
            fail();
        }
        if (!scroll.scrollIntoView(new UiSelector().textContains("我的钱包"))) {
            fail();
        }
        if (!scroll.scrollIntoView(new UiSelector().textContains("消费记录"))) {
            fail();
        }
        if (scroll.scrollIntoView(new UiSelector().textContains("设置"))) {
            //UiScrollable listSettings = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        } else {
            fail();
        }
        if (!scroll.scrollIntoView(new UiSelector().textContains("服务协议"))) {
            fail();
        }
    }

    @Test
    public void GRZX_0009() throws InterruptedException {
        enterPersonalCenter();
        UiObject2 tv_score = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/score"));
        //获取个人中心积分
        int oriScore = Integer.parseInt(tv_score.getText());
        Log.d("uiautomator", "score=" + tv_score.getText());
        mDevice.pressBack();
        waitForObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/fl_root"));
        mDevice.pressDPadLeft();
        mDevice.pressEnter();
        waitForObject(By.res("com.kuyun.plugin.app.pointsmall:id/layout_card_b_really_content"));
        //您目前有11119酷币
        String pointMallScore = mDevice.findObject(By.res("com.kuyun.plugin.app.pointsmall:id/tv_points_value")).getText();
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(pointMallScore);
        //获取礼品商城积分
        int point = Integer.parseInt(m.replaceAll("").trim());
        assertEquals(oriScore, point);

       /* //兑换商品
        List<UiObject2> objects = mDevice.findObjects(By.res("com.kuyun.plugin.app.pointsmall:id/layout_record_enter_head"));
        int pointCost = 0;
        UiObject2 toBeClick = null;
        for (UiObject2 obj : objects) {
            int requirePoint = Integer.parseInt(obj.findObject(By.res("com.kuyun.plugin.app.pointsmall:id/tv_exchange_list_item_score")).getText());
            if (requirePoint < point && requirePoint > 0) {
                pointCost = requirePoint;
                toBeClick = obj;
                break;
            }
        }
        if (toBeClick != null) {
            toBeClick.click();
        } else {
            fail();
            //翻页
        }
        UiObject2 btn_exchange = waitForObject(By.res("com.kuyun.plugin.app.pointsmall:id/button_to_exchange"));
        btn_exchange.click();*/
    }

    /*@Test
    public void GRZX_() throws InterruptedException, UiObjectNotFoundException {
        enterPersonalCenter();
        UiScrollable scroll = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (scroll.scrollIntoView(mDevice.findObject(new UiSelector().text("绑定手机号")))) {
            mDevice.pressEnter();

        } else {
            //手机号绑定过
        }
    }

    @Test
    public void GRZX_0004() throws InterruptedException {
        enterPersonalCenter();
        UiObject2 tv_score = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/score"));
        Log.d("uiautomator", tv_score.getText());
        //15119
    }

    @Test
    public void GRZX_0011() throws InterruptedException {
        enterPersonalCenter();
        mDevice.pressBack();
        waitForObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/fl_root"));
    }*/


    public void enterPersonalCenter() throws InterruptedException {
        enterBroadCast();
        openPluginList();
        /*UiObject2 editText = waitForObject(By.res("com.example.app:id/numboard_pwd_edittext"));

        takeScreenshot("screenshot-1.png");

        editText.setText("123456");
        UiObject2 protectObject = waitForObject(By.text("Submit"));
        protectObject.click();

        takeScreenshot("screenshot-2.png");

        Thread.sleep(10000);*/
        Thread.sleep(1000);
        Log.d("uiautomator", "currentThread:" + Thread.currentThread().getName());
        //D/uiautomator: currentThread:Instr: android.support.test.runner.AndroidJUnitRunner
        for (int i = 0; i < 1; i++) {
            mDevice.pressDPadLeft();
        }
        mDevice.pressEnter();
        //进入了预约提醒
        //waitForObject(By.text("订阅节目提醒"));
        //进入个人中心
        waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rootL"));
    }

    /* private void openApp(String packageName) {
         Context context = InstrumentationRegistry.getInstrumentation().getContext();
         Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
         context.startActivity(intent);
     }*/

    /**
     * 打开卡片列表
     */
    private void openPluginList() throws InterruptedException {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        //kuyun.com.uiautomatortest.test
        Log.d("uiautomator", context.getApplicationInfo().packageName);
        Intent intent = new Intent();
        intent.setAction("com.tv.KY_LAUNCHED");
        context.sendBroadcast(intent);
        waitForObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/fl_root"));
    }

    private UiObject2 waitForObject(BySelector selector) throws InterruptedException {
        UiObject2 object = null;
        int timeout = 10000;
        int delay = 1000;
        long time = System.currentTimeMillis();
        while (object == null) {
            object = mDevice.findObject(selector);
            Thread.sleep(delay);
            if (System.currentTimeMillis() - timeout > time) {
                fail("time out");
            }
        }
        return object;
    }

    private void takeScreenshot(String name) {
        Log.d("TEST", "takeScreenshot");
        String dir = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), "test-screenshots");
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        mDevice.takeScreenshot(new File(String.format("%s/%s", dir, name)));
    }

    @After
    public void after() {

    }
}