package kuyun.com.uiautomatortest;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getmem_SELF();
       /* ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        Log.i(TAG, " memoryInfo.availMem " + memoryInfo.availMem + "\n");
        Log.i(TAG, " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n");
        Log.i(TAG, " memoryInfo.threshold " + memoryInfo.threshold + "\n");

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        Map<Integer, String> pidMap = new TreeMap<Integer, String>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
        }
        Collection<Integer> keys = pidMap.keySet();

        for (int key : keys) {
            int pids[] = new int[1];
            pids[0] = key;
            android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
            for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfoArray) {
                Log.i(TAG, String.format("** MEMINFO in pid %d [%s] **\n", pids[0], pidMap.get(pids[0])));
                Log.i(TAG, " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
                Log.i(TAG, " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
                Log.i(TAG, " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
            }
        }*/
    }
    public long getmem_SELF() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> procInfo = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : procInfo) {
            System.out.println(runningAppProcessInfo.processName+ String.format(",pid = %d", runningAppProcessInfo.pid));
            if( runningAppProcessInfo.processName.indexOf(this.getPackageName()) != -1 )
            {
                int pids[] = {runningAppProcessInfo.pid};
                Debug.MemoryInfo self_mi[] = am.getProcessMemoryInfo(pids);
                StringBuffer  strbuf = new StringBuffer();
                strbuf.append(" proccess Name:").append(runningAppProcessInfo.processName)
                        .append("\n pid:").append(runningAppProcessInfo.pid)
                        .append("\n dalvikPrivateDirty:").append(self_mi[0].dalvikPrivateDirty)
                        .append("\n dalvikPss:").append(self_mi[0].dalvikPss)
                        .append("\n dalvikSharedDirty:").append(self_mi[0].dalvikSharedDirty)
                        .append("\n nativePrivateDirty:").append(self_mi[0].nativePrivateDirty)
                        .append("\n nativePss:").append(self_mi[0].nativePss)
                        .append("\n nativeSharedDirty:").append(self_mi[0].nativeSharedDirty)
                        .append("\n otherPrivateDirty:").append(self_mi[0].otherPrivateDirty)
                        .append("\n otherPss:").append(self_mi[0].otherPss)
                        .append("\n otherSharedDirty:").append(self_mi[0].otherSharedDirty)
                        .append("\n TotalPrivateDirty:").append(self_mi[0].getTotalPrivateDirty())
                        .append("\n TotalPss:").append(self_mi[0].getTotalPss())
                        .append("\n TotalSharedDirty:").append(self_mi[0].getTotalSharedDirty());
                Log.v("TEST",strbuf.toString());
            }
        }
        return 0;
    }
}
