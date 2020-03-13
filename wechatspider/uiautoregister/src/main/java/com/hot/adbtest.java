package com.hot;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;

import java.io.File;

public class adbtest extends SuperRunner {
    private String workPath = "work.py";
    private boolean isExecute = false;

//    private String code = "# -*- coding:utf-8 -*-\n" +
//            "# !/usr/bin/Python3\n" +
//            "import uiautomator2 as u2\n" +
//            "import time\n" +
//            "import requests\n" +
//            "import json\n" +
//            "import uuid\n" +
//            "import os\n" +
//            "\n" +
//            "d = u2.connect('0.0.0.0')\n" +
//            "# d = u2.connect_usb('f7d64448')\n" +
//            "d.set_new_command_timeout(300000)\n" +
//            "# zhihu_type_url = 'http://v3.jqsocial.com:22025/api/common/zhihu/getshot?code=jeqeehot'\n" +
//            "# zhihu_type_submit_url = 'http://v3.jqsocial.com:22025/api/common/zhihu/postshot'\n" +
//            "\n" +
//            "zhihu_type_url = 'http://222.185.251.62:22027/api/business/getzhihushot'\n" +
//            "zhihu_type_submit_url = 'http://222.185.251.62:22027/api/business/submitzhihushot'\n" +
//            "update_phone_url = 'http://222.185.251.62:22027/api/phone/updatephoneinfo'\n" +
//            "headers = {'Content-Type': 'application/json'}\n" +
//            "typecode = '知乎业务管理'\n" +
//            "phonecode = '小米手机07'\n" +
//            "code = 'jeqeehot'\n" +
//            "\n" +
//            "\n" +
//            "class ZhiHuScreen(object):\n" +
//            "    def __init__(self):\n" +
//            "        ZhiHuScreen.open_app(self)\n" +
//            "\n" +
//            "    def open_app(self):\n" +
//            "        d.app_start('com.zhihu.android')\n" +
//            "        time.sleep(5)\n" +
//            "        if d(text='下次再说').exists():\n" +
//            "            d(text='下次再说').click()\n" +
//            "        if d(resourceId='com.zhihu.android:id/iv_equity_close').exists():\n" +
//            "            d(resourceId='com.zhihu.android:id/iv_equity_close').click()\n" +
//            "        ZhiHuScreen.judge_search_page(self)\n" +
//            "        if d(resourceId='com.smile.gifmaker:id/left_btn').exists():\n" +
//            "            d(resourceId='com.smile.gifmaker:id/left_btn').click()\n" +
//            "\n" +
//            "    # 判断是否在搜索页面\n" +
//            "    def judge_search_page(self):\n" +
//            "        for i in range(1, 5):\n" +
//            "            if not d(text='首页').exists():\n" +
//            "                d.press('back')\n" +
//            "                time.sleep(2)\n" +
//            "            else:\n" +
//            "                break\n" +
//            "\n" +
//            "    def update_phone(self, message, logtype):\n" +
//            "        body = {\n" +
//            "            \"PhoneCode\": phonecode,\n" +
//            "            \"TypeCode\": typecode,\n" +
//            "            \"Message\": message,\n" +
//            "            \"LogType\": logtype\n" +
//            "        }\n" +
//            "        requests.post(update_phone_url, headers=headers, data=json.dumps(body))\n" +
//            "\n" +
//            "    def pic_to_byte(self, pic_name):\n" +
//            "        b = []\n" +
//            "        with open(pic_name, 'rb') as f:\n" +
//            "            for i in f.read():\n" +
//            "                b.append(i)\n" +
//            "        return b\n" +
//            "\n" +
//            "    def restart_app(self):\n" +
//            "        d.session('com.zhihu.android')\n" +
//            "        time.sleep(5)\n" +
//            "        ZhiHuScreen.open_app(self)\n" +
//            "\n" +
//            "    def move(self):\n" +
//            "        while True:\n" +
//            "            info = d(description='话题').info\n" +
//            "            top = info['bounds']['top']\n" +
//            "            d(className='android.view.View').gesture(\n" +
//            "                (0, top),\n" +
//            "                (0, top),\n" +
//            "                (0, 216),\n" +
//            "                (0, 216))\n" +
//            "            time.sleep(2)\n" +
//            "            if not d(description='话题').exists():\n" +
//            "                break\n" +
//            "            info = d(description='话题').info\n" +
//            "            top = info['bounds']['top']\n" +
//            "            if top <= 300:\n" +
//            "                break\n" +
//            "\n" +
//            "    def search(self, search_content):\n" +
//            "        b = ''\n" +
//            "        try:\n" +
//            "            d(resourceId='com.zhihu.android:id/input').click()\n" +
//            "            time.sleep(2)\n" +
//            "            if d(text='下次再说').exists():\n" +
//            "                d(text='下次再说').click()\n" +
//            "            d(className='android.widget.EditText').send_keys(search_content)\n" +
//            "            time.sleep(1)\n" +
//            "            d.click(0.921, 0.95)\n" +
//            "            time.sleep(20)\n" +
//            "            name = str(uuid.uuid1()) + '.png'\n" +
//            "            d.screenshot(name)\n" +
//            "            b = ZhiHuScreen.pic_to_byte(self, name)\n" +
//            "            os.remove(name)\n" +
//            "            return b\n" +
//            "        except Exception as e:\n" +
//            "            ZhiHuScreen.restart_app(self)\n" +
//            "        return b\n" +
//            "\n" +
//            "    def execute(self):\n" +
//            "        while True:\n" +
//            "            try:\n" +
//            "                d.app_start('com.zhihu.android')\n" +
//            "                request = requests.post(zhihu_type_url, headers=headers, data=json.dumps(code))\n" +
//            "                if request.status_code == 200:\n" +
//            "                    search_dic = json.loads(request.text)\n" +
//            "                    if '任务为空' in request.text:\n" +
//            "                        ZhiHuScreen.update_phone(self, '无任务', '检测')\n" +
//            "                        time.sleep(5 * 60)\n" +
//            "                        # 检查守护线程 是否运行\n" +
//            "                        d.healthcheck()\n" +
//            "                        d.app_start('com.zhihu.android')\n" +
//            "                        continue\n" +
//            "                    search_dic = json.loads(search_dic)\n" +
//            "                    for key in search_dic:\n" +
//            "                        bt = ZhiHuScreen.search(self, key['key'])\n" +
//            "                        if bt:\n" +
//            "                            json_str = json.dumps(bt)\n" +
//            "                            submit_body = {\n" +
//            "                                'id': key['id'],\n" +
//            "                                'code': 'jeqeehot',\n" +
//            "                                'pic': json_str\n" +
//            "                            }\n" +
//            "                            submit = requests.post(zhihu_type_submit_url, headers=headers, data=json.dumps(submit_body))\n" +
//            "                            ZhiHuScreen.update_phone(self, submit.text, '检测')\n" +
//            "                        else:\n" +
//            "                            ZhiHuScreen.update_phone(self, '截图失败', '检测')\n" +
//            "            except Exception as e:\n" +
//            "                print(e)\n" +
//            "            pass\n" +
//            "\n" +
//            "\n" +
//            "if __name__ == '__main__':\n" +
//            "    ks = ZhiHuScreen()\n" +
//            "    ks.execute()\n" +
//            "    pass\n";

    //外网ip
    private String Host = "222.185.251.62";
    //添加日志
    private String addLogURL = "http://" + Host + ":30008/api/wx/addlog";

    //com.hipipal.qpy3/org.qpython.qpy3.MIndexActivity
    public void testRunner() {
        PackegName = "com.hipipal.qpy3";
        Device = getUiDevice();
//        HttpHelper.DeleteFile(new File("/storage/emulated/0/qpython/scripts3/work.py"));
//        ScriptHelper.appendToSDFile(code, "/storage/emulated/0/qpython/scripts3/work.py");
        jqhelper.reportNormal();
        try {
            if (!isExecute) {
                Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
                Runtime.getRuntime().exec("chmod 755 /data/local/tmp/atx-agent");
                sleep(5000);
                Runtime.getRuntime().exec("/data/local/tmp/atx-agent server -d");
                sleep(5000);
            }
            isExecute = true;
            ExecuteQpython();
//            while (true) {
//                try {
//                    jqhelper.reportNormal();
//                    ExecuteQpython();
//                } catch (Exception e) {
//                } finally {
//                    jqhelper.reportNormal();
//                    sleep(300000);
//                }
//            }
        } catch (Exception e) {
            ScriptHelper.Log(e.toString());
        } finally {
//            sleep(600000);
//            testRunner();
        }

    }

    public void ReceptionCloseAtx() {
        try {
            jqhelper.reportNormal();
            Runtime.getRuntime().exec("am start -n com.github.uiautomator/.MainActivity");
            sleep(5000);
            ob = new UiObject(new UiSelector().text("停止ATXAGENT"));
            if (ob.waitForExists(5000)) {
                ob.click();
                sleep(5000);
                ob = new UiObject(new UiSelector().text("YES"));
                if (ob.waitForExists(2000)) {
                    ob.click();
                    sleep(5000);
                }
            }
            sleep(5000);
        } catch (Exception e) {
            ScriptHelper.Log(e.toString());
        }
    }

    /**
     * 执行Qpython
     */
    public void ExecuteQpython() {
        try {
            Runtime.getRuntime().exec("am force-stop com.hipipal.qpy3");
            sleep(5000);
            Runtime.getRuntime().exec("am start -n com.hipipal.qpy3/org.qpython.qpy3.MIndexActivity");
            ob = new UiObject(new UiSelector().text("程序"));
            if (ob.waitForExists(2000)) {
                ob.clickAndWaitForNewWindow(2000);
                ob = new UiObject(new UiSelector().text(workPath));
                if (ob.waitForExists(2000)) {
                    ob.click();
                    sleep(1000);
                    ob = new UiObject(new UiSelector().text("Run"));
                    if (ob.waitForExists(2000)) {
                        ob.click();
                    }
                }
            }
        } catch (Exception e) {
            ScriptHelper.Log(e.toString());
        }
    }


}
