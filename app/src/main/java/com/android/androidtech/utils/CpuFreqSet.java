package com.android.androidtech.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yuchengluo on 2016/7/5.
 */

public class CpuFreqSet {
    public class CPUFreqSetting {
        /**
         * cpu cat命令大全
         * cat [%cpuFreqPath%]/cpuinfo_cur_freq   (当前cpu频率)
         * cat [%cpuFreqPath%]/cpuinfo_max_freq     (最大cpu频率)
         * cat [%cpuFreqPath%]/cpuinfo_min_freq     (最小cpu频率)
         * cat [%cpuFreqPath%]/related_cpus     (cpu数量标号,从0开始,如果是双核,结果为0,1)
         * cat [%cpuFreqPath%]/scaling_available_frequencies    (cpu所有可用频率)
         * cat [%cpuFreqPath%]/scaling_available_governors  (cpu所有可用调控模式)
         * cat [%cpuFreqPath%]/scaling_available_governors  (cpu所有可用调控模式)
         * cat [%cpuFreqPath%]/scaling_cur_freq     (?????)
         * cat [%cpuFreqPath%]/scaling_driver   (?????)
         * cat [%cpuFreqPath%]/scaling_governor (?????)
         * cat [%cpuFreqPath%]/scaling_max_freq (?????)
         * cat [%cpuFreqPath%]/scaling_min_freq (?????)
         * cat [%cpuFreqPath%]/scaling_setspeed (?????)
         * cat [%cpuFreqPath%]/cpuinfo_transition_latency   (?????)
         */
        private final String TAG = "CpuFreqSet";
        private final String cpuFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq";
//        private final static String PERFORMANCE_GOVERNOR = "performance";
//        private final static String POWER_SAVE_GOVERNOR = "performance";
//        private final static String ONDEMAND_GOVERNOR = "performance";
//        private final static String CONSERVATIVE_GOVERNOR = "performance";
//        private final static String USERSAPCE_GOVERNOR = "performance";
        /**
         * 获取当前CPU调控模式
         */
        public void getCpuCurGovernor() {
            try {
                DataInputStream is = null;
                Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_governor");
                is = new DataInputStream(process.getInputStream());
                String line = is.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 设置CPU调控模式
         */
        private boolean writeCpuGovernor(String governor) {
            DataOutputStream os = null;
            byte[] buffer = new byte[256];
            String command = "echo " + governor + " > " + cpuFreqPath + "/scaling_governor";
            try {
                Process process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(command + "\n");
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        /**
         * 获得CPU所有调控模式
         * @return
         */
        private ArrayList<String> readCpuGovernors() {
            ArrayList<String> governors = new ArrayList<String>();
            DataInputStream is = null;
            try {
                Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_available_governors");
                is = new DataInputStream(process.getInputStream());
                String line = is.readLine();

                String[] strs = line.split(" ");
                for (int i = 0; i < strs.length; i++)
                    governors.add(strs[i]);
            } catch (IOException e) {
            }
            return governors;
        }
    }
}
