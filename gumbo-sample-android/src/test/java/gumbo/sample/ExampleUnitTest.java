package gumbo.sample;

import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void urlEncodeTest() throws Exception {
        String url = "http://172.16.100.34/download/千县亿动/com.protruly.qxyd5.0.apk";
        System.out.println(URLEncoder.encode(url,"utf-8"));

    }
}