package com.huihui;

import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/8/2 0002.
 */
public class CookiesTest {
    @Test
    public void parseCookie(){
        String cookies = "asd=czx; fef=cxvxcv; czxczx=czxcxz; JSESSIONID=79D3EE25DF703EAC3BAF8D6334D69711";
        List<Cookie> list = new ArrayList<>();
        for(String cookie:cookies.split(";") ){
            cookie = cookie.trim();
            String[] arr = cookie.split("=");
            list.add(new Cookie(arr[0],arr[1]));
        }
//        for(Cookie cookie:list){
//            System.out.println(cookie.getName() + " " + cookie.getValue());
//        }
    }

    @Test
    public void parseCookieBy(){
        String cookies = "asd=czx; fef=cxvxcv; czxczx=czxcxz; JSESSIONID=79D3EE25DF703EAC3BAF8D6334D69711";
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isname = true;
        List<Cookie> list = new ArrayList<>();
        for(char c:cookies.toCharArray()){
            if(c==';'){
                list.add(new Cookie(name.toString().trim(),value.toString()));
                name = new StringBuilder();
                value = new StringBuilder();
                isname = true;
            }else if(c=='='){
                isname = !isname;
            }else {
                if(isname)
                    name.append(c);
                else
                    value.append(c);
            }
        }
        list.add(new Cookie(name.toString().trim(),value.toString()));
//        for(Cookie cookie:list){
//            System.out.println(cookie.getName()+" "+cookie.getValue());
//        }
    }
    @Test
    public void testTime(){
        long begin = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            parseCookie();
            //parseCookieBy();
        }
        long end = System.currentTimeMillis();
        System.out.println(end-begin);
    }
}
