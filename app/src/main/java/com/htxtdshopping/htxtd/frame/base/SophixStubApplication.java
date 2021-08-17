package com.htxtdshopping.htxtd.frame.base;

import android.content.Context;
import android.util.Log;

import com.android.dsly.common.base.BaseApp;
import com.htxtdshopping.htxtd.frame.BuildConfig;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import androidx.annotation.Keep;

/**
 * @author 陈志鹏
 * @date 2018/11/26
 */
public class SophixStubApplication extends SophixApplication {
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(BaseApp.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initSophix();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("25324416-1", "5ae8c0acccce82b6e2d556d62dbb3e34",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCE+2ToKrXn2YtpGbwd/7Q2hm+F+AOi5tr6mK42derER1I1BPiQLmqpNBGgoIvTPPvtRWOygbIaetbv17oaU4fxFe/pr5iiZLjtOh1JM+XWN+akybqmWOS1gBbeQPJD0Gdk8i2Ut/tRuCliNq0Mn6vXN+PIrGKr3Bv9qJRmtJSQV7VXrRacSanJiyr1FcYC6quEfFcacH/EMy6iAdKJR28UfALv9q7ld3C+dSCNgkYL6xQ+ToXVxn5ijZGkwkKgklBlRSzwWf/nYKJaUIrMcsYVhskT2+jAMgLij4hmzPVBiQ1LVe7f/4lccn+e3cUrEa895qJRUc2XjpxrZIWksVkhAgMBAAECggEAVvYy+aQcir53bUZ3/0IdLl7hhUE314qR/rhjjJe1658cQjG6/kZsV7QGJv12ErBZqsLqvGwV9T0JW5S+TcL8LgsDddTvwpqNWAzd4+X9UgYCXRbSaT/OaGaYDcV46CHby2aXqh5aSgldfq10osGz1SzmV29roylwYIOicDt8v5q4yriy2rv9aCAAOSGB5/nFtq7Mfrak5jZh2ok+RTdSsgxv4oCUvTrVZe3OzHx8Vn4GqA4uLdYyeLqZZ15OqykLC2TFNI6YJUwSY5oJW3iAUR6WUYa1pS6RK6XmsFJpquHAk3z/B0TeO3jLEqa4QeQ9biewokTgOFPp2agSEBmcwQKBgQDOs8WXr6gi5AYPZuUhzDaRtrHQpxj93+O8mcDwuQE5SlzpWsmoeaaKFe0DSt48B8+HPtfcKKxw3dXVxS2CnwmwbLLZ7EJDzqJCe73TioFH6i3u6vV1i/20UuW1fpWEqPzrV0WPWXywIJ+cP2iaXQvo8JykXBKBslWcWrhOrWJWyQKBgQCksqArpzi2L9mUqhTJmRewveen+12+8kMs/6nGe5ScZEBJzMwFuTEAplmmMptKCeAuRBksJWQSoEdBPZjuX9StKFRjlAECBwBhxVnfqIfRzGzTEBAVUL5lkbrPYCGKpNgRss05PZna3MUIYAnQjLsYBWAHa4dO7DRn2s40a54jmQKBgA0kgxDE+3TUeMor6a+/c0zKh/3TUhR6UtmgERN9P+Wro8K2ohHTQlKPWFWYQg8hFAHubf0ynNxETp9b/SvL/uq3zptqmUAEeG6vvX8g1F2FN0x0pgzGTOhVSQPEO6+GqyYvk12s+++a82sU4QrHlcVG3z7YF9klRKo/Xm6qOZF5AoGAfN7188MIi5GB/WZq9mK9hIKnlkROUozqA78f4N4n0d9sosfE1H4RujVl+U2bUaplL47wKvl6g2jEPaHPRsSfwl22hf1cOkZGstpnht0HhTjiNbCTESY0BVxVcA+pvq23KMwRL1oWQctrFCuEpXteCxtgMKAFagBKx4ctGq+RqdkCgYEAl4PfxjF1VZYOlvfPFNgw6fhmLzng+/iJGb2GZNaeo42C2svq6Ljy6UujdQM3QqOnRzfF0zFdxfTcMWmVOOHi7280X+5DgskrUrw6X0LFrHxNTciOuURQqgH8hUF74SA05wonh9cFgDw8IpuhQi1G66xYBxr+TXjaJ4Jk6rweTpE=")
                .setEnableDebug(BuildConfig.DEBUG ? true : false)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i("sophix", "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i("sophix", "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }
}
