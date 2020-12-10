package com.htxtdshopping.htxtd.frame;

import com.blankj.utilcode.util.NumberUtils;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        String format = NumberUtils.format(0.0162f, 2);
        System.out.println(format);
    }
}