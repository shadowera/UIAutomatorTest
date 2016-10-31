package kuyun.com.uiautomatortest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private static final String TAG = "ApplicationTest";
    private UiDevice mDevice;
    private Context mContext;

    @Before
    public void before() throws InterruptedException {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        assertThat(mDevice, notNullValue());
        mContext = InstrumentationRegistry.getContext();
        String apkRoot = "chmod 777 " + mContext.getPackageCodePath();
        //mDevice.pressHome();
    }

    //从卡片列表进入到某张确定卡片
    public boolean enterCard(String cardName) throws InterruptedException, UiObjectNotFoundException {
        UiObject2 list = waitForObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/ll_scroll_main"));
        int childCount = list.getChildCount();
        // Utils.UiAutomationLog("childcount=" + children.size() + ",count=" + childCount);
        List<UiObject2> children = list.getChildren();
        UiObject2 item = children.get(children.size() - 1);
        // Utils.UiAutomationLog(children.size() + "");
        boolean has = item.hasObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/view_mask"));

        String title = item.findObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/tv_default_title")).getText();
        //Utils.UiAutomationLog(title);
        if (cardName.equals(title)) {
            mDevice.pressEnter();
            return true;
        } else {
            mDevice.pressDPadLeft();
        }
        for (int index = childCount - 1; index > 0; index--) {
            list = mDevice.findObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/ll_scroll_main"));
            children = list.getChildren();
            // Utils.UiAutomationLog(children.size() + "");
            item = children.get(children.size() - 2);
            has = item.hasObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/view_mask"));
            title = item.findObject(By.res("com.kuyun.plugin.app.card.pluginlist:id/tv_default_title")).getText();
            if (cardName.equals(title)) {
                mDevice.pressEnter();
                return true;
            } else {
                mDevice.pressDPadLeft();
            }
        }
        return false;
    }

    //进入直播
    public void enterBroadCast() throws InterruptedException {
        mDevice.pressHome();
        Thread.sleep(2000);
        mDevice.pressEnter();
        waitForObject(By.res("com.xiaomi.mitv.tvplayer:id/root_view"));
        Thread.sleep(7000);//等识台
    }

    @Test
    public void testApi() throws IOException, InterruptedException {
        List<AppEntity> androidProcess = Utils.getAndroidProcess(mContext);
        if(androidProcess!=null){
            for(AppEntity app:androidProcess){
                Utils.UiAutomationLog(TAG+app.getPackageName()+",pss="+app.getPss());
            }
        }

        //Utils.logKuyunMemory("logkuyunmemory",mContext);
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

   /* @Test
    public void TestLogUtils() {
        Utils.logKuyunMemory("test", mContext);
    }*/

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

   /* */

    /**
     * 在设置页面获取勾选和不勾选的图片，保存在sd卡
     */
    @Test
    public void captureCheck() throws FileNotFoundException {

        Rect bounds = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/scItemState")).getVisibleBounds();
        //bottom:592,top=552,left=1799,right=1839
        Log.d("captureCheck", "bottom:" + bounds.bottom + ",top=" + bounds.top + ",left=" + bounds.left + ",right=" + bounds.right);
        takeScreenshot("setting.png");
        Bitmap check = Utils.getSubImage(Utils.getScreenShootPath() + "/setting.png", bounds);
        Utils.saveBitMapToSdcard(check, "check.png");
    }

    @Test
    public void captureUnCheck() throws FileNotFoundException {
        Rect bounds = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/scItemState")).getVisibleBounds();
        //bottom:592,top=552,left=1799,right=1839
        Log.d("captureCheck", "bottom:" + bounds.bottom + ",top=" + bounds.top + ",left=" + bounds.left + ",right=" + bounds.right);
        takeScreenshot("setting.png");
        Bitmap check = Utils.getSubImage(Utils.getScreenShootPath() + "/setting.png", bounds);
        Utils.saveBitMapToSdcard(check, "unCheck.png");
    }

    @Test
    public void captureAllCheck() throws UiObjectNotFoundException, FileNotFoundException {
        String[] items = {"抢购", "节目信息", "即将播放", "预约提醒", "竞猜答题", "vod开关", "滚字卡片", "通用消息", "滚球下注", "互动开关", "礼品商城", "弹幕", "彩票"};
        UiScrollable setting_lv = new UiScrollable(new UiSelector().className("android.widget.ListView").resourceId("com.kuyun.plugin.app.card.personcenter:id/scList"));
        String screenShootPath = Utils.getScreenShootPath();
        for (String item : items) {
            setting_lv.scrollIntoView(new UiSelector().textContains(item));
            boolean isCheck = false;
            UiObject child = setting_lv.getChildByText(new UiSelector().className("android.widget.RelativeLayout"), item);
            Rect rec = child.getChild(new UiSelector().className("android.widget.ImageView").resourceId("com.kuyun.plugin.app.card.personcenter:id/scItemState")).getBounds();
            takeScreenshot("temp.png");
            Utils.saveBitMapToSdcard(Utils.getSubImage(screenShootPath + "/temp.png", rec), item);
        }
    }

    public void enterPersonalCenter() throws InterruptedException {
        //enterBroadCast();
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
        for (int i = 0; i < 7; i++) {
            mDevice.pressDPadLeft();
        }
        mDevice.pressEnter();
        //进入了预约提醒
        //waitForObject(By.text("订阅节目提醒"));
        //进入个人中心
        waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rootL"));
    }

    /**
     * 测试用例：
     * "1、进入服务协议检查服务协议内容
     * 2、遥控器返回键是否能够退出服务协议"
     * 预期结果：
     * "1、服务协议内容文案正确（根据产品经理提供的文案检查）
     * 2、遥控器返回键能够退出服务协议"
     */
    @Test
    public void GRZX_0075() throws InterruptedException, UiObjectNotFoundException {
        //enterPersonalCenter();
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("服务协议"))) {
            fail();
        } else {
            mDevice.findObject(By.text("服务协议")).click();
            //waitForObject()
            assertEquals("服务协议", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            String expText = "\t \t “酷云互动”服务是由北京酷云互动科技有限公司（下称“酷云互动”）推出的电视增强服务，在电视用户收看、使用电视的过程中，提供伴随式的互联网增强服务功能。酷云互动在此特别提醒用户在使用“酷云互动”服务之前认真阅读、充分理解本《服务协议》（下称“协议”）中各条款，包括免除或者限制酷云互动责任的免责条款及对用户的权利限制条款。请您审慎阅读并选择接受或不接受本协议（未成年人应在法定监护人陪同下阅读）。如果您不接受本协议条款，您将无法使用本协议所涉相关服务。您的使用等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。\n" +
                    " \t \t 本协议是您（下称“用户”）与酷云互动公司之间关于用户使用“酷云互动”服务所订立的协议。本协议描述酷云互动与用户之间关于“酷云互动”服务相关方面的权利义务。“用户”是指使用本服务的个人或组织。\n" +
                    " \n" +
                    " 一、协议概述与条款确认\n" +
                    " 1、您对本协议的接受即视为您成为酷云互动的用户，视为您确认自己具有享受酷云互动服务的相应权利能力和行为能力，能够独立承担法律责任，受全部条款的约束，包括接受酷云互动公司对任一服务条款随时所做的任何修改。用户确认：本协议条款是处理双方权利义务的契约，始终有效，法律另有强制性规定或双方另有特别约定的，依其规定。\n" +
                    " 2、根据国家法律法规变化及运营需要，酷云互动有权对本协议条款不时进行更新，更新后的协议条款一经发布即代替原来的协议条款，恕不再另行通知。用户可在电视增强产品的“个人中心”随时查阅最新协议条款。在酷云互动修改协议条款后，用户若不同意更新后的协议，请立即停止使用酷云互动提供的服务，用户继续使用酷云互动提供的服务将被视为已接受了修改后的协议。\n" +
                    " 3、本协议描述了我们收集的个人信息以及我们将如何使用这些信息。如果您同意本协议，您就已经同意我们按照本协议来使用和披露您的个人信息。如果您对本协议有任何疑问，请务必联系我们。\n" +
                    " \n" +
                    " 二、酷云互动服务\n" +
                    " 1、酷云互动提供电视增强服务，在用户收看电视的过程中，酷云提供精准信息、广告推送、互动参与、等不限于诸多形式的互联网增值服务，全维度的丰富用户收看电视体验。\n" +
                    " 2.用户在收看电视的过程中，会接收到酷云互动推送的互联网服务，并可主动通过遥控器使用酷云互动服务、当然也可在产品的功能中随时关闭酷云互动提供的服务能力，不再体验酷云互动服务。\n" +
                    " \n" +
                    " 三、用户个人信息\n" +
                    " 1、用户个人信息是指那些能够单独或者与其他信息结合识别用户的信息，包括但不限于：用户真实姓名，出生日期、身份证号码，住址，手机号码，账号和密码以及用户使用服务的时间、地点等信息。\n" +
                    " 2、用户个人信息收集目的及使用\n" +
                    " 2-1、 酷云互动在向用户提供服务时，为了通过数据获取、信息整理，帮助提高酷云互动的服务，包括维护、改善产品服务，开发新的服务，需要向用户收集必要的用户信息。\n" +
                    " 2-2、 酷云互动在未经用户同意的情况下，不将其用于上述目的以外的其它用途，或不向除合作单位以外的第三方提供。但以下情况除外：\n" +
                    " 2-2-1、为完成用户参加酷云互动活动时所需的配送业务，向配送公司提供配送所必需的最少的用户信息（姓名，地址，电话号码等）的情况；\n" +
                    " 2-2-2、因统计调查、学术研究或市场调查的需要，以不能识别某个个人隐私信息的形式提供的情况；\n" +
                    " 2-2-3、应相应的法律法规或国家司法、行政程序要求的情况；\n" +
                    " 2-2-4、维护酷云互动的合法利益的情况；\n" +
                    " 2-2-5、不可抗力情况；\n" +
                    " 2-2-6、其他酷云互动不存在过错的情况。\n" +
                    " 3、用户个人信息收集方式和范围\n" +
                    " 为满足使用需求和提高服务质量，我们通过自动内容识别技术获取用户终端及局域网周边设备的相关信息和收视数据，包括：终端品牌及型号、mac地址、频道、地理位置、当前播放内容的音视频指纹等。\n" +
                    " 4、尊重和保护用户个人信息是酷云互动的一贯制度和责任，酷云互动将会采取合理的措施保护用户的个人信息，除法律或有法律赋予权限的政府部门要求或用户同意等原因外，酷云互动未经用户同意不向除合作单位以外的第三方公开、 透露用户个人信息。 但是，在经用户选择同意，或用户与酷云互动及合作单位之间就用户个人信息公开或使用另有约定的除外，同时用户应自行承担因此可能产生的任何风险，酷云互动对此不予负责。同时，为了运营和改善酷云互动的技术和服务，酷云互动将可能会自行收集使用或向第三方提供用户的个人信息，这将有助于酷云互动向用户提供更好的用户体验和提高酷云互动的服务质量。\n" +
                    " \n" +
                    " 四、法律责任及免责\n" +
                    " 1、用户违反本协议或相关的服务条款的规定，导致或产生的任何第三方主张的任何索赔、要求或损失，包括合理的律师费，用户同意赔偿酷云互动与合作公司、关联公司，并使之免受损害。\n" +
                    " 2、用户因第三方如电信部门的通讯线路故障、技术问题、网络、电脑故障、系统不稳定性及其他各种不可抗力原因而遭受的一切损失，酷云互动及合作单位不承担责任。\n" +
                    " 3、因技术故障等不可抗力事件影响到服务的正常运行的，酷云互动及合作单位承诺在第一时间内与相关单位配合，及时处理进行修复，但用户因此而遭受的一切损失，酷云互动及合作单位不承担责任。\n" +
                    " 4、用户须明白，使用本服务因涉及Internet服务，可能会受到各个环节不稳定因素的影响。因此，本服务存在因不可抗力、系统不稳定、用户所在位置、用户关机以及其他任何技术、互联网络、通信线路原因等造成的服务中断或不能满足用户要求的风险。用户须承担以上风险，酷云互动公司不作担保。对因此导致用户不能发送和接受阅读信息、或接发错信息，酷云互动公司不承担任何责任。\n" +
                    " 5、酷云互动公司定义的信息内容包括：文字、软件、声音、相片、录像、图表；在广告中全部内容；酷云互动公司为用户提供的商业信息，所有这些内容受版权、商标权、和其它知识产权和所有权法律的保护。所以，用户只能在酷云互动公司和广告商授权下才能使用这些内容，而不能擅自复制、修改、编纂这些内容、或创造与内容有关的衍生产品。\n" +
                    " \n" +
                    " 五、其他条款\n" +
                    " 1、本协议以上各项条款内容的解释权及修改权归酷云互动公司所有。\n" +
                    " 2、本协议所定的任何条款的部分或全部无效，不影响其它条款的效力。\n" +
                    " 3、本协议的解释、效力及纠纷的解决，适用于中华人民共和国法律。若用户和酷云互动之间发生任何纠纷或争议，首先应友好协商解决，协商不成的，用户在此完全同意将纠纷或争议提交酷云互动住所地即北京市有管辖权的人民法院管辖。\n" +
                    " 4、若您对酷云互动公司及本服务有任何意见，欢迎您发送电子邮件到以下邮箱 \n" +
                    " help@kuyun.com 来联系我们。";
            String realText = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/protocolTxt")).getText();
            assertEquals(expText, realText);
            assertEquals("酷云互动用户服务协议", mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/protocolTitle")).getText());
            mDevice.pressBack();
            waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rootL"));
        }
    }

    /**
     * 测试用例：
     * 检查设置里的卡片是否和需求上线的卡片相符
     * 预期结果：
     * 设置里的卡片是否和需求上线的卡片相符
     *
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     */
    @Test
    public void GRZX_47() throws InterruptedException, UiObjectNotFoundException {
        //enterPersonalCenter();
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("设置"))) {
            fail();
        } else {
            mDevice.findObject(By.text("设置")).click();
            String title = waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText();
            assertEquals("设置", title);
            UiScrollable setting_lv = new UiScrollable(new UiSelector().className("android.widget.ListView").resourceId("com.kuyun.plugin.app.card.personcenter:id/scList"));
            //设置中所有项目
            String[] items = {"抢购", "节目信息", "即将播放", "预约提醒", "竞猜答题", "vod开关", "滚字卡片", "通用消息", "滚球下注", "互动开关", "礼品商城", "弹幕", "彩票"};
            for (String item : items) {
                if (!setting_lv.scrollIntoView(new UiSelector().textContains(item))) {
                    fail();
                }
            }
        }
    }

    public void GRZX_14() throws UiObjectNotFoundException, InterruptedException {
        //enterPersonalCenter();
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("支付宝授权"))) {
            fail();
        } else {
            mDevice.findObject(By.text("支付宝授权")).click();
            assertEquals("支付宝授权", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            assertEquals("手机支付宝APP,扫描二维码授权绑定电视支付。", mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/alipayTips")).getText());
            takeScreenshot("支付宝授权.png");
        }
    }

    /**
     * 测试用例：
     * "1、个人中心-消费记录列表页面
     * 2、检查客服电话是否是010-65618948"
     * 预期结果：
     *
     * @throws UiObjectNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void GRZX_0035() throws UiObjectNotFoundException, InterruptedException {
        //enterPersonalCenter();
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("设置"))) {
            fail();
        } else {
            mDevice.findObject(By.text("消费记录")).click();
            assertEquals("消费记录", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            UiObject2 tel = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rfTopTel"));
            assertEquals("010-65618948", tel.getText());
        }
    }

    /**
     * 测试用例：
     * "1、个人中心-消费记录详情页面
     * 2、检查客服电话是否是010-65618948"
     * 预期结果：
     *
     * @throws UiObjectNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void GRZX_0036() throws UiObjectNotFoundException, InterruptedException {
        //enterPersonalCenter();
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("设置"))) {
            fail();
        } else {
            mDevice.findObject(By.text("消费记录")).click();
            assertEquals("消费记录", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            UiScrollable list = new UiScrollable(new UiSelector().resourceId("com.kuyun.plugin.app.card.personcenter:id/rcList"));
            if (list.getChildCount() <= 0) {
                //当没兑换过礼品是，去礼品中心兑换一个礼品
            } else {
                list.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), 0).click();
                Thread.sleep(2000);
                UiObject2 tel = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/rfTopTel"));
                assertEquals("010-65618948", tel.getText());
            }
        }
    }


    /**
     * 测试用例：
     * 1、进入系统设置
     * 2、短按返回
     * 预期结果：
     * 返回个人中心界面
     *
     * @throws UiObjectNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void GRZX_0038() throws UiObjectNotFoundException, InterruptedException {
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("设置"))) {
            fail();
        } else {
            mDevice.findObject(By.text("设置")).click();
            assertEquals("设置", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            mDevice.pressBack();
            waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/headerImg"));
        }
    }

    /**
     * 测试用例：
     * "1、进入设置页面
     * 2、对未勾选的选项做勾选操作
     * 3、查看该选项的变化是否正确"
     * 预期结果：
     * 该未被勾选的选项变为被勾选状态
     *
     * @throws InterruptedException
     */
    @Test
    public void GRZX_0044() throws
            InterruptedException, UiObjectNotFoundException, FileNotFoundException {
        //enterPersonalCenter();
        String[] items = {"抢购", "节目信息", "即将播放", "预约提醒", "竞猜答题", "vod开关", "滚字卡片", "通用消息", "滚球下注", "互动开关", "礼品商城", "弹幕", "彩票"};
        UiScrollable listView = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        if (!listView.scrollIntoView(new UiSelector().textContains("设置"))) {
            fail();
        } else {
            mDevice.findObject(By.text("设置")).click();
            assertEquals("设置", waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/topTitle")).getText());
            //当不是所有卡片都是关闭时
            UiObject2 btn_all = mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/scOnOffTxt"));
            UiScrollable setting_lv = new UiScrollable(new UiSelector().className("android.widget.ListView").resourceId("com.kuyun.plugin.app.card.personcenter:id/scList"));
            String screenShootPath = Utils.getScreenShootPath();
            for (String item : items) {
                setting_lv.scrollIntoView(new UiSelector().textContains(item));
                boolean isCheck = false;
                UiObject child = setting_lv.getChildByText(new UiSelector().className("android.widget.RelativeLayout"), item);
                Rect rec = child.getChild(new UiSelector().className("android.widget.ImageView").resourceId("com.kuyun.plugin.app.card.personcenter:id/scItemState")).getBounds();
                takeScreenshot("temp.png");
                Utils.saveBitMapToSdcard(Utils.getSubImage(screenShootPath + "/temp.png", rec), "temp_isCheck.png");
                if (Utils.similarAs(screenShootPath + "/check.png", screenShootPath + "/temp_isCheck.png", 0.98)) {
                    isCheck = true;
                } else if (Utils.similarAs(screenShootPath + "/unCheck.png", screenShootPath + "/temp_isCheck.png", 0.98)) {
                    isCheck = false;
                } else {
                    fail();
                }
                Log.d(TAG, "ischeck=" + isCheck);
            }
        }
    }

   /* @Test
    private void GRZX_0095() {
        //enterPersonalCenter();
        String[] settingItems = new String[]{"绑定手机号", "支付宝授权", "我的卡券", "我的钱包", "消费记录", "设置", "服务协议"};
        for (String item : settingItems) {

        }
    }*/

    private void comfirmDialog() throws InterruptedException {
        UiObject2 dialogContent = waitForObject(By.res("com.kuyun.plugin.app.card.personcenter:id/dialogTxt"));
        assertEquals("如果选择关闭\n" + "将不再收到弹出提醒", dialogContent.getText());
        mDevice.findObject(By.res("com.kuyun.plugin.app.card.personcenter:id/dialogOk")).click();
        Thread.sleep(2000);
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