package hyj.xw.hook.newHook;

import android.hardware.Sensor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class uI9oCr52KBbqPYj1dkTi extends DHHdslt4SqYQ1hSj1a4Y
{
    public uI9oCr52KBbqPYj1dkTi(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.hardware.SensorManager", "getSensorList");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        ArrayList localArrayList;
        if (("getSensorList".equals(paramMethodHookParam.method.getName())) && (((Integer)paramMethodHookParam.args[0]).intValue() == -1)){
            localArrayList = new ArrayList();
            try
            {
                Constructor localConstructor = Class.forName("android.hardware.Sensor").getDeclaredConstructor(new Class[0]);
                localConstructor.setAccessible(true);
                Random localRandom = new Random();
                int i = 67500000 + localRandom.nextInt(5000);
                Sensor localSensor1 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField1 = Sensor.class.getDeclaredField("mName");
                localField1.setAccessible(true);
                localField1.set(localSensor1, "Accelerometer");
                Field localField2 = Sensor.class.getDeclaredField("mVendor");
                localField2.setAccessible(true);
                localField2.set(localSensor1, "InvenSense");
                Field localField3 = Sensor.class.getDeclaredField("mVersion");
                localField3.setAccessible(true);
                localField3.set(localSensor1, Integer.valueOf(i));
                Field localField4 = Sensor.class.getDeclaredField("mType");
                localField4.setAccessible(true);
                localField4.set(localSensor1, Integer.valueOf(1));
                Field localField5 = Sensor.class.getDeclaredField("mMaxRange");
                localField5.setAccessible(true);
                localField5.set(localSensor1, Float.valueOf(19.623106F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField6 = Sensor.class.getDeclaredField("mResolution");
                localField6.setAccessible(true);
                localField6.set(localSensor1, Float.valueOf(0.0392266F));
                Field localField7 = Sensor.class.getDeclaredField("mPower");
                localField7.setAccessible(true);
                localField7.set(localSensor1, Float.valueOf(0.0F));
                Field localField8 = Sensor.class.getDeclaredField("mMinDelay");
                localField8.setAccessible(true);
                localField8.set(localSensor1, Integer.valueOf(10000));
                Sensor localSensor2 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField9 = Sensor.class.getDeclaredField("mName");
                localField9.setAccessible(true);
                localField9.set(localSensor2, "Magnetometer");
                Field localField10 = Sensor.class.getDeclaredField("mVendor");
                localField10.setAccessible(true);
                localField10.set(localSensor2, "AKM");
                Field localField11 = Sensor.class.getDeclaredField("mVersion");
                localField11.setAccessible(true);
                localField11.set(localSensor2, Integer.valueOf(i));
                Field localField12 = Sensor.class.getDeclaredField("mType");
                localField12.setAccessible(true);
                localField12.set(localSensor2, Integer.valueOf(1));
                Field localField13 = Sensor.class.getDeclaredField("mMaxRange");
                localField13.setAccessible(true);
                localField13.set(localSensor2, Float.valueOf(19.623106F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField14 = Sensor.class.getDeclaredField("mResolution");
                localField14.setAccessible(true);
                localField14.set(localSensor2, Float.valueOf(0.0392266F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField15 = Sensor.class.getDeclaredField("mPower");
                localField15.setAccessible(true);
                localField15.set(localSensor2, Float.valueOf(0.0F));
                Field localField16 = Sensor.class.getDeclaredField("mMinDelay");
                localField16.setAccessible(true);
                localField16.set(localSensor2, Integer.valueOf(10000));
                Sensor localSensor3 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField17 = Sensor.class.getDeclaredField("mName");
                localField17.setAccessible(true);
                localField17.set(localSensor3, "Gyroscope");
                Field localField18 = Sensor.class.getDeclaredField("mVendor");
                localField18.setAccessible(true);
                localField18.set(localSensor3, "InvenSense");
                Field localField19 = Sensor.class.getDeclaredField("mVersion");
                localField19.setAccessible(true);
                localField19.set(localSensor3, Integer.valueOf(i));
                Field localField20 = Sensor.class.getDeclaredField("mType");
                localField20.setAccessible(true);
                localField20.set(localSensor3, Integer.valueOf(4));
                Field localField21 = Sensor.class.getDeclaredField("mMaxRange");
                localField21.setAccessible(true);
                localField21.set(localSensor3, Float.valueOf(34.924038F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField22 = Sensor.class.getDeclaredField("mResolution");
                localField22.setAccessible(true);
                localField22.set(localSensor3, Float.valueOf(0.001064651F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField23 = Sensor.class.getDeclaredField("mPower");
                localField23.setAccessible(true);
                localField23.set(localSensor3, Float.valueOf(0.0F));
                Field localField24 = Sensor.class.getDeclaredField("mMinDelay");
                localField24.setAccessible(true);
                localField24.set(localSensor3, Integer.valueOf(10000));
                Sensor localSensor4 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField25 = Sensor.class.getDeclaredField("mName");
                localField25.setAccessible(true);
                localField25.set(localSensor4, "Pressure Sensor");
                Field localField26 = Sensor.class.getDeclaredField("mVendor");
                localField26.setAccessible(true);
                localField26.set(localSensor4, "Bosch");
                Field localField27 = Sensor.class.getDeclaredField("mVersion");
                localField27.setAccessible(true);
                localField27.set(localSensor4, Integer.valueOf(i));
                Field localField28 = Sensor.class.getDeclaredField("mType");
                localField28.setAccessible(true);
                localField28.set(localSensor4, Integer.valueOf(6));
                Field localField29 = Sensor.class.getDeclaredField("mMaxRange");
                localField29.setAccessible(true);
                localField29.set(localSensor4, Float.valueOf(1100.0F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField30 = Sensor.class.getDeclaredField("mResolution");
                localField30.setAccessible(true);
                localField30.set(localSensor4, Float.valueOf(0.01F));
                Field localField31 = Sensor.class.getDeclaredField("mPower");
                localField31.setAccessible(true);
                localField31.set(localSensor4, Float.valueOf(0.0F));
                Field localField32 = Sensor.class.getDeclaredField("mMinDelay");
                localField32.setAccessible(true);
                localField32.set(localSensor4, Integer.valueOf(10000));
                Sensor localSensor5 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField33 = Sensor.class.getDeclaredField("mName");
                localField33.setAccessible(true);
                localField33.set(localSensor5, "Ambient Light Sensor");
                Field localField34 = Sensor.class.getDeclaredField("mVendor");
                localField34.setAccessible(true);
                localField34.set(localSensor5, "Capella");
                Field localField35 = Sensor.class.getDeclaredField("mVersion");
                localField35.setAccessible(true);
                localField35.set(localSensor5, Integer.valueOf(i));
                Field localField36 = Sensor.class.getDeclaredField("mType");
                localField36.setAccessible(true);
                localField36.set(localSensor5, Integer.valueOf(5));
                Field localField37 = Sensor.class.getDeclaredField("mMaxRange");
                localField37.setAccessible(true);
                localField37.set(localSensor5, Float.valueOf(200000.0F));
                Field localField38 = Sensor.class.getDeclaredField("mResolution");
                localField38.setAccessible(true);
                localField38.set(localSensor5, Float.valueOf(0.1F));
                Field localField39 = Sensor.class.getDeclaredField("mPower");
                localField39.setAccessible(true);
                localField39.set(localSensor5, Float.valueOf(0.0F));
                Field localField40 = Sensor.class.getDeclaredField("mMinDelay");
                localField40.setAccessible(true);
                localField40.set(localSensor5, Integer.valueOf(0));
                Sensor localSensor6 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField41 = Sensor.class.getDeclaredField("mName");
                localField41.setAccessible(true);
                localField41.set(localSensor6, "Proximity Sensor");
                Field localField42 = Sensor.class.getDeclaredField("mVendor");
                localField42.setAccessible(true);
                localField42.set(localSensor6, "Capella");
                Field localField43 = Sensor.class.getDeclaredField("mVersion");
                localField43.setAccessible(true);
                localField43.set(localSensor6, Integer.valueOf(i));
                Field localField44 = Sensor.class.getDeclaredField("mType");
                localField44.setAccessible(true);
                localField44.set(localSensor6, Integer.valueOf(8));
                Field localField45 = Sensor.class.getDeclaredField("mMaxRange");
                localField45.setAccessible(true);
                localField45.set(localSensor6, Float.valueOf(5.0F));
                Field localField46 = Sensor.class.getDeclaredField("mResolution");
                localField46.setAccessible(true);
                localField46.set(localSensor6, Float.valueOf(5.0F));
                Field localField47 = Sensor.class.getDeclaredField("mPower");
                localField47.setAccessible(true);
                localField47.set(localSensor6, Float.valueOf(0.0F));
                Field localField48 = Sensor.class.getDeclaredField("mMinDelay");
                localField48.setAccessible(true);
                localField48.set(localSensor6, Integer.valueOf(0));
                Sensor localSensor7 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField49 = Sensor.class.getDeclaredField("mName");
                localField49.setAccessible(true);
                localField49.set(localSensor7, "Gravity");
                Field localField50 = Sensor.class.getDeclaredField("mVendor");
                localField50.setAccessible(true);
                localField50.set(localSensor7, "Movea");
                Field localField51 = Sensor.class.getDeclaredField("mVersion");
                localField51.setAccessible(true);
                localField51.set(localSensor7, Integer.valueOf(i));
                Field localField52 = Sensor.class.getDeclaredField("mType");
                localField52.setAccessible(true);
                localField52.set(localSensor7, Integer.valueOf(9));
                Field localField53 = Sensor.class.getDeclaredField("mMaxRange");
                localField53.setAccessible(true);
                localField53.set(localSensor7, Float.valueOf(9.816457F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField54 = Sensor.class.getDeclaredField("mResolution");
                localField54.setAccessible(true);
                localField54.set(localSensor7, Float.valueOf(0.009806651F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField55 = Sensor.class.getDeclaredField("mPower");
                localField55.setAccessible(true);
                localField55.set(localSensor7, Float.valueOf(0.0F));
                Field localField56 = Sensor.class.getDeclaredField("mMinDelay");
                localField56.setAccessible(true);
                localField56.set(localSensor7, Integer.valueOf(10000));
                Sensor localSensor8 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField57 = Sensor.class.getDeclaredField("mName");
                localField57.setAccessible(true);
                localField57.set(localSensor8, "Linear Acceleration");
                Field localField58 = Sensor.class.getDeclaredField("mVendor");
                localField58.setAccessible(true);
                localField58.set(localSensor8, "Movea");
                Field localField59 = Sensor.class.getDeclaredField("mVersion");
                localField59.setAccessible(true);
                localField59.set(localSensor8, Integer.valueOf(i));
                Field localField60 = Sensor.class.getDeclaredField("mType");
                localField60.setAccessible(true);
                localField60.set(localSensor8, Integer.valueOf(10));
                Field localField61 = Sensor.class.getDeclaredField("mMaxRange");
                localField61.setAccessible(true);
                localField61.set(localSensor8, Float.valueOf(29.439564F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField62 = Sensor.class.getDeclaredField("mResolution");
                localField62.setAccessible(true);
                localField62.set(localSensor8, Float.valueOf(0.0392266F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField63 = Sensor.class.getDeclaredField("mPower");
                localField63.setAccessible(true);
                localField63.set(localSensor8, Float.valueOf(0.0F));
                Field localField64 = Sensor.class.getDeclaredField("mMinDelay");
                localField64.setAccessible(true);
                localField64.set(localSensor8, Integer.valueOf(10000));
                Sensor localSensor9 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField65 = Sensor.class.getDeclaredField("mName");
                localField65.setAccessible(true);
                localField65.set(localSensor9, "Orientation");
                Field localField66 = Sensor.class.getDeclaredField("mVendor");
                localField66.setAccessible(true);
                localField66.set(localSensor9, "Movea");
                Field localField67 = Sensor.class.getDeclaredField("mVersion");
                localField67.setAccessible(true);
                localField67.set(localSensor9, Integer.valueOf(i));
                Field localField68 = Sensor.class.getDeclaredField("mType");
                localField68.setAccessible(true);
                localField68.set(localSensor9, Integer.valueOf(3));
                Field localField69 = Sensor.class.getDeclaredField("mMaxRange");
                localField69.setAccessible(true);
                localField69.set(localSensor9, Float.valueOf(360.0F));
                Field localField70 = Sensor.class.getDeclaredField("mResolution");
                localField70.setAccessible(true);
                localField70.set(localSensor9, Float.valueOf(1.0F));
                Field localField71 = Sensor.class.getDeclaredField("mPower");
                localField71.setAccessible(true);
                localField71.set(localSensor9, Float.valueOf(0.0F));
                Field localField72 = Sensor.class.getDeclaredField("mMinDelay");
                localField72.setAccessible(true);
                localField72.set(localSensor9, Integer.valueOf(10000));
                Sensor localSensor10 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField73 = Sensor.class.getDeclaredField("mName");
                localField73.setAccessible(true);
                localField73.set(localSensor10, "Rotation Vector");
                Field localField74 = Sensor.class.getDeclaredField("mVendor");
                localField74.setAccessible(true);
                localField74.set(localSensor10, "Movea");
                Field localField75 = Sensor.class.getDeclaredField("mVersion");
                localField75.setAccessible(true);
                localField75.set(localSensor10, Integer.valueOf(i));
                Field localField76 = Sensor.class.getDeclaredField("mType");
                localField76.setAccessible(true);
                localField76.set(localSensor10, Integer.valueOf(11));
                Field localField77 = Sensor.class.getDeclaredField("mMaxRange");
                localField77.setAccessible(true);
                localField77.set(localSensor10, Float.valueOf(1.0F));
                Field localField78 = Sensor.class.getDeclaredField("mResolution");
                localField78.setAccessible(true);
                localField78.set(localSensor10, Float.valueOf(0.01F));
                Field localField79 = Sensor.class.getDeclaredField("mPower");
                localField79.setAccessible(true);
                localField79.set(localSensor10, Float.valueOf(0.0F));
                Field localField80 = Sensor.class.getDeclaredField("mMinDelay");
                localField80.setAccessible(true);
                localField80.set(localSensor10, Integer.valueOf(10000));
                Sensor localSensor11 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField81 = Sensor.class.getDeclaredField("mName");
                localField81.setAccessible(true);
                localField81.set(localSensor11, "Game Rotation Vector");
                Field localField82 = Sensor.class.getDeclaredField("mVendor");
                localField82.setAccessible(true);
                localField82.set(localSensor11, "InvenSense");
                Field localField83 = Sensor.class.getDeclaredField("mVersion");
                localField83.setAccessible(true);
                localField83.set(localSensor11, Integer.valueOf(i));
                Field localField84 = Sensor.class.getDeclaredField("mType");
                localField84.setAccessible(true);
                localField84.set(localSensor11, Integer.valueOf(15));
                Field localField85 = Sensor.class.getDeclaredField("mMaxRange");
                localField85.setAccessible(true);
                localField85.set(localSensor11, Float.valueOf(1.0F));
                Field localField86 = Sensor.class.getDeclaredField("mResolution");
                localField86.setAccessible(true);
                localField86.set(localSensor11, Float.valueOf(0.01F));
                Field localField87 = Sensor.class.getDeclaredField("mPower");
                localField87.setAccessible(true);
                localField87.set(localSensor11, Float.valueOf(0.0F));
                Field localField88 = Sensor.class.getDeclaredField("mMinDelay");
                localField88.setAccessible(true);
                localField88.set(localSensor11, Integer.valueOf(10000));
                Sensor localSensor12 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField89 = Sensor.class.getDeclaredField("mName");
                localField89.setAccessible(true);
                localField89.set(localSensor12, "Geomagnetic Rotation Vector");
                Field localField90 = Sensor.class.getDeclaredField("mVendor");
                localField90.setAccessible(true);
                localField90.set(localSensor12, "Movea");
                Field localField91 = Sensor.class.getDeclaredField("mVersion");
                localField91.setAccessible(true);
                localField91.set(localSensor12, Integer.valueOf(i));
                Field localField92 = Sensor.class.getDeclaredField("mType");
                localField92.setAccessible(true);
                localField92.set(localSensor12, Integer.valueOf(20));
                Field localField93 = Sensor.class.getDeclaredField("mMaxRange");
                localField93.setAccessible(true);
                localField93.set(localSensor12, Float.valueOf(1.0F));
                Field localField94 = Sensor.class.getDeclaredField("mResolution");
                localField94.setAccessible(true);
                localField94.set(localSensor12, Float.valueOf(0.01F));
                Field localField95 = Sensor.class.getDeclaredField("mPower");
                localField95.setAccessible(true);
                localField95.set(localSensor12, Float.valueOf(0.0F));
                Field localField96 = Sensor.class.getDeclaredField("mMinDelay");
                localField96.setAccessible(true);
                localField96.set(localSensor12, Integer.valueOf(10000));
                Sensor localSensor13 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField97 = Sensor.class.getDeclaredField("mName");
                localField97.setAccessible(true);
                localField97.set(localSensor13, "Magnetometer Uncalibrated");
                Field localField98 = Sensor.class.getDeclaredField("mVendor");
                localField98.setAccessible(true);
                localField98.set(localSensor13, "AKM");
                Field localField99 = Sensor.class.getDeclaredField("mVersion");
                localField99.setAccessible(true);
                localField99.set(localSensor13, Integer.valueOf(i));
                Field localField100 = Sensor.class.getDeclaredField("mType");
                localField100.setAccessible(true);
                localField100.set(localSensor13, Integer.valueOf(14));
                Field localField101 = Sensor.class.getDeclaredField("mMaxRange");
                localField101.setAccessible(true);
                localField101.set(localSensor13, Float.valueOf(200.10001F));
                Field localField102 = Sensor.class.getDeclaredField("mResolution");
                localField102.setAccessible(true);
                localField102.set(localSensor13, Float.valueOf(0.1F));
                Field localField103 = Sensor.class.getDeclaredField("mPower");
                localField103.setAccessible(true);
                localField103.set(localSensor13, Float.valueOf(0.0F));
                Field localField104 = Sensor.class.getDeclaredField("mMinDelay");
                localField104.setAccessible(true);
                localField104.set(localSensor13, Integer.valueOf(20000));
                Sensor localSensor14 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField105 = Sensor.class.getDeclaredField("mName");
                localField105.setAccessible(true);
                localField105.set(localSensor14, "Gyroscope Uncalibrated");
                Field localField106 = Sensor.class.getDeclaredField("mVendor");
                localField106.setAccessible(true);
                localField106.set(localSensor14, "InvenSense");
                Field localField107 = Sensor.class.getDeclaredField("mVersion");
                localField107.setAccessible(true);
                localField107.set(localSensor14, Integer.valueOf(i));
                Field localField108 = Sensor.class.getDeclaredField("mType");
                localField108.setAccessible(true);
                localField108.set(localSensor14, Integer.valueOf(16));
                Field localField109 = Sensor.class.getDeclaredField("mMaxRange");
                localField109.setAccessible(true);
                localField109.set(localSensor14, Float.valueOf(34.924038F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField110 = Sensor.class.getDeclaredField("mResolution");
                localField110.setAccessible(true);
                localField110.set(localSensor14, Float.valueOf(0.001064651F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField111 = Sensor.class.getDeclaredField("mPower");
                localField111.setAccessible(true);
                localField111.set(localSensor14, Float.valueOf(0.0F));
                Field localField112 = Sensor.class.getDeclaredField("mMinDelay");
                localField112.setAccessible(true);
                localField112.set(localSensor14, Integer.valueOf(10000));
                Sensor localSensor15 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField113 = Sensor.class.getDeclaredField("mName");
                localField113.setAccessible(true);
                localField113.set(localSensor15, "Significant Motion Detector");
                Field localField114 = Sensor.class.getDeclaredField("mVendor");
                localField114.setAccessible(true);
                localField114.set(localSensor15, "Movea");
                Field localField115 = Sensor.class.getDeclaredField("mVersion");
                localField115.setAccessible(true);
                localField115.set(localSensor15, Integer.valueOf(i));
                Field localField116 = Sensor.class.getDeclaredField("mType");
                localField116.setAccessible(true);
                localField116.set(localSensor15, Integer.valueOf(17));
                Field localField117 = Sensor.class.getDeclaredField("mMaxRange");
                localField117.setAccessible(true);
                localField117.set(localSensor15, Float.valueOf(19.623106F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField118 = Sensor.class.getDeclaredField("mResolution");
                localField118.setAccessible(true);
                localField118.set(localSensor15, Float.valueOf(1.0F));
                Field localField119 = Sensor.class.getDeclaredField("mPower");
                localField119.setAccessible(true);
                localField119.set(localSensor15, Float.valueOf(0.0F));
                Field localField120 = Sensor.class.getDeclaredField("mMinDelay");
                localField120.setAccessible(true);
                localField120.set(localSensor15, Integer.valueOf(-1));
                Sensor localSensor16 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField121 = Sensor.class.getDeclaredField("mName");
                localField121.setAccessible(true);
                localField121.set(localSensor16, "Step Detector");
                Field localField122 = Sensor.class.getDeclaredField("mVendor");
                localField122.setAccessible(true);
                localField122.set(localSensor16, "Movea");
                Field localField123 = Sensor.class.getDeclaredField("mVersion");
                localField123.setAccessible(true);
                localField123.set(localSensor16, Integer.valueOf(i));
                Field localField124 = Sensor.class.getDeclaredField("mType");
                localField124.setAccessible(true);
                localField124.set(localSensor16, Integer.valueOf(18));
                Field localField125 = Sensor.class.getDeclaredField("mMaxRange");
                localField125.setAccessible(true);
                localField125.set(localSensor16, Float.valueOf(255.0F));
                Field localField126 = Sensor.class.getDeclaredField("mResolution");
                localField126.setAccessible(true);
                localField126.set(localSensor16, Float.valueOf(1.0F));
                Field localField127 = Sensor.class.getDeclaredField("mPower");
                localField127.setAccessible(true);
                localField127.set(localSensor16, Float.valueOf(0.0F));
                Field localField128 = Sensor.class.getDeclaredField("mMinDelay");
                localField128.setAccessible(true);
                localField128.set(localSensor16, Integer.valueOf(0));
                Sensor localSensor17 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField129 = Sensor.class.getDeclaredField("mName");
                localField129.setAccessible(true);
                localField129.set(localSensor17, "Step Counter");
                Field localField130 = Sensor.class.getDeclaredField("mVendor");
                localField130.setAccessible(true);
                localField130.set(localSensor17, "Movea");
                Field localField131 = Sensor.class.getDeclaredField("mVersion");
                localField131.setAccessible(true);
                localField131.set(localSensor17, Integer.valueOf(i));
                Field localField132 = Sensor.class.getDeclaredField("mType");
                localField132.setAccessible(true);
                localField132.set(localSensor17, Integer.valueOf(19));
                Field localField133 = Sensor.class.getDeclaredField("mMaxRange");
                localField133.setAccessible(true);
                localField133.set(localSensor17, Float.valueOf(6.5535E+008F + 1.0E-007F * localRandom.nextInt(1000)));
                Field localField134 = Sensor.class.getDeclaredField("mResolution");
                localField134.setAccessible(true);
                localField134.set(localSensor17, Float.valueOf(1.0F));
                Field localField135 = Sensor.class.getDeclaredField("mPower");
                localField135.setAccessible(true);
                localField135.set(localSensor17, Float.valueOf(0.0F));
                Field localField136 = Sensor.class.getDeclaredField("mMinDelay");
                localField136.setAccessible(true);
                localField136.set(localSensor17, Integer.valueOf(0));
                Sensor localSensor18 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField137 = Sensor.class.getDeclaredField("mName");
                localField137.setAccessible(true);
                localField137.set(localSensor18, "Facing Sensor");
                Field localField138 = Sensor.class.getDeclaredField("mVendor");
                localField138.setAccessible(true);
                localField138.set(localSensor18, "Movea");
                Field localField139 = Sensor.class.getDeclaredField("mVersion");
                localField139.setAccessible(true);
                localField139.set(localSensor18, Integer.valueOf(i));
                Field localField140 = Sensor.class.getDeclaredField("mType");
                localField140.setAccessible(true);
                localField140.set(localSensor18, Integer.valueOf(65541));
                Field localField141 = Sensor.class.getDeclaredField("mMaxRange");
                localField141.setAccessible(true);
                localField141.set(localSensor18, Float.valueOf(3.0F));
                Field localField142 = Sensor.class.getDeclaredField("mResolution");
                localField142.setAccessible(true);
                localField142.set(localSensor18, Float.valueOf(1.0F));
                Field localField143 = Sensor.class.getDeclaredField("mPower");
                localField143.setAccessible(true);
                localField143.set(localSensor18, Float.valueOf(0.0F));
                Field localField144 = Sensor.class.getDeclaredField("mMinDelay");
                localField144.setAccessible(true);
                localField144.set(localSensor18, Integer.valueOf(0));
                Sensor localSensor19 = (Sensor)localConstructor.newInstance(new Object[0]);
                Field localField145 = Sensor.class.getDeclaredField("mName");
                localField145.setAccessible(true);
                localField145.set(localSensor19, "Activity Monitor");
                Field localField146 = Sensor.class.getDeclaredField("mVendor");
                localField146.setAccessible(true);
                localField146.set(localSensor19, "Movea");
                Field localField147 = Sensor.class.getDeclaredField("mVersion");
                localField147.setAccessible(true);
                localField147.set(localSensor19, Integer.valueOf(i));
                Field localField148 = Sensor.class.getDeclaredField("mType");
                localField148.setAccessible(true);
                localField148.set(localSensor19, Integer.valueOf(65542));
                Field localField149 = Sensor.class.getDeclaredField("mMaxRange");
                localField149.setAccessible(true);
                localField149.set(localSensor19, Float.valueOf(12.0F));
                Field localField150 = Sensor.class.getDeclaredField("mResolution");
                localField150.setAccessible(true);
                localField150.set(localSensor19, Float.valueOf(1.0F));
                Field localField151 = Sensor.class.getDeclaredField("mPower");
                localField151.setAccessible(true);
                localField151.set(localSensor19, Float.valueOf(0.0F));
                Field localField152 = Sensor.class.getDeclaredField("mMinDelay");
                localField152.setAccessible(true);
                localField152.set(localSensor19, Integer.valueOf(0));
                localArrayList.add(localSensor1);
                localArrayList.add(localSensor2);
                localArrayList.add(localSensor3);
                localArrayList.add(localSensor4);
                localArrayList.add(localSensor5);
                localArrayList.add(localSensor6);
                localArrayList.add(localSensor7);
                localArrayList.add(localSensor8);
                localArrayList.add(localSensor9);
                localArrayList.add(localSensor10);
                localArrayList.add(localSensor11);
                localArrayList.add(localSensor12);
                localArrayList.add(localSensor13);
                localArrayList.add(localSensor14);
                localArrayList.add(localSensor15);
                localArrayList.add(localSensor16);
                localArrayList.add(localSensor17);
                localArrayList.add(localSensor18);
                localArrayList.add(localSensor19);
                paramMethodHookParam.setResult(localArrayList);
                super.afterHookedMethod(paramMethodHookParam);
                return;
            }
            catch (Exception localException)
            {
                localException.printStackTrace();
            }
        }
    }
}