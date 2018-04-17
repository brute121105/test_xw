package hyj.xw.aw.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hyj.xw.GlobalApplication;
import hyj.xw.hook.createDevice.GenerateDeviceUtil;
import hyj.xw.hook.newHook.NewPhoneInfo;

/**
 * Created by Administrator on 2018/4/16.
 */

public class BuildFileUtil {
    public static List<NewPhoneInfo> getBuildPhoneInfo() {
        final ArrayList<NewPhoneInfo> list = new ArrayList<NewPhoneInfo>();
        for (final String s : Edbw69C30UgVp2ocKByJ.O00000Oo(GlobalApplication.getContext(), "BuildFile")) {
            if (s.trim().length() > 0) {
                final String[] split = s.split("_");
                String buildBrand = null;
                String buildModel = null;
                String s2 = null;
                String cpuName = null;
                String buildDevice = null;
                String buildManufacturer = null;
                String buildProduct = null;
                Label_0106: {
                    if (split.length == 4) {
                        buildBrand = split[0];
                        buildModel = split[1];
                        s2 = split[2];
                        cpuName = split[3];
                        buildDevice = split[1];
                        buildManufacturer = split[0];
                        buildProduct = split[1];
                    }
                    else {
                        String s3;
                        String s4;
                        String s5;
                        if (split.length == 7) {
                            buildBrand = split[0];
                            buildModel = split[1];
                            s2 = split[2];
                            cpuName = split[3];
                            s3 = split[4];
                            if (s3.contains("=")) {
                                s3 = s3.replaceAll("=", "_");
                            }
                            s4 = split[5];
                            s5 = split[4];
                            if (split[6].contains("=")) {
                                final String replaceAll = split[6].replaceAll("=", "_");
                                buildManufacturer = s4;
                                buildDevice = s3;
                                buildProduct = replaceAll;
                                break Label_0106;
                            }
                        }
                        else {
                            if (split.length != 8) {
                                Log.d("error", s);
                                buildProduct = null;
                                buildManufacturer = null;
                                buildDevice = null;
                                cpuName = null;
                                s2 = null;
                                buildModel = null;
                                buildBrand = null;
                                break Label_0106;
                            }
                            buildBrand = split[0];
                            buildModel = split[1];
                            s2 = split[2];
                            cpuName = split[3];
                            s3 = split[4];
                            if (s3.contains("=")) {
                                s3 = s3.replaceAll("=", "_");
                            }
                            s4 = split[5];
                            s5 = split[6];
                            if (s5.contains("=")) {
                                final String replaceAll2 = s5.replaceAll("=", "_");
                                buildManufacturer = s4;
                                buildDevice = s3;
                                buildProduct = replaceAll2;
                                break Label_0106;
                            }
                        }
                        final String s6 = s5;
                        buildManufacturer = s4;
                        buildDevice = s3;
                        buildProduct = s6;
                    }
                }
                if (TextUtils.isEmpty((CharSequence)buildBrand) || TextUtils.isEmpty((CharSequence)buildModel) || TextUtils.isEmpty((CharSequence)s2) || TextUtils.isEmpty((CharSequence)cpuName) || TextUtils.isEmpty((CharSequence)buildDevice) || TextUtils.isEmpty((CharSequence)buildManufacturer) || TextUtils.isEmpty((CharSequence)buildProduct)) {
                    continue;
                }
                final NewPhoneInfo phoneInfo = new NewPhoneInfo();
                phoneInfo.setBuildBrand(buildBrand);
                phoneInfo.setBuildModel(buildModel);
                phoneInfo.setCpuName(cpuName);
                phoneInfo.setBuildDevice(buildDevice);
                phoneInfo.setBuildManufacturer(buildManufacturer);
                phoneInfo.setBuildProduct(buildProduct);
                final int int1 = Integer.parseInt(s2.toLowerCase().split("x")[0]);
                final int int2 = Integer.parseInt(s2.toLowerCase().split("x")[1]);
                phoneInfo.setWidth(int1);
                phoneInfo.setHeight(int2);
                list.add(phoneInfo);
            }
        }
        return list;
    }

    public static NewPhoneInfo createOneDevice(final String s) {
        final List<NewPhoneInfo> o000000o = getBuildPhoneInfo();
        final NewPhoneInfo phoneInfo = o000000o.get(new Random().nextInt(o000000o.size()));
        /*if (!(boolean)aRXnzOtj0HbW58z9PstP.O00000Oo(context, "HookDisplay", false)) {
            phoneInfo.setWidth(0);
            phoneInfo.setHeight(0);
        }*/
        final NewPhoneInfo o000000o2 =  GenerateDeviceUtil.createPhoneInfo(phoneInfo, s);
       // O000000o(context, o000000o2);
        return o000000o2;
    }
}


