package com.example.demo.config;

import com.example.demo.dom.all.entity.User;
import com.example.demo.dom.all.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.SpringContextUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * @author Administrator
 * @date 2018/8/31.
 */
public class MyAccessControlFilter extends AccessControlFilter {

    @Autowired
    private UserMapper userMapper;

    static final String LOGIN_URL = "login.html";
    static final String NEW_PASSWORD_URL = "404.html";

    //    isAccessAllowed：即是否允许访问，返回true表示允许；
    //    onAccessDenied：表示访问拒绝时是否自己处理，
    //    如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        System.out.println("AccessControlFilter:isAccessAllowed拦截执行");
        String uri = getPathWithinApplication(servletRequest);
        System.out.println("用户正在访问：" + uri);
        Subject subject = getSubject(servletRequest, servletResponse);
        if (subject.getPrincipal() == null) {
            return false;
        } else {
            User user = (User) subject.getPrincipal();
            UserService userService = SpringContextUtils.getContext().getBean(UserService.class);
            Set<String> powers=userService.findPower(user.getUid().toString());
            if(o==null){
                System.out.println("这个url没设置权限url:"+uri);
                return false;
            }
            String[] passUrls = (String[]) o;
            for (String power:powers) {
                if(power.equals("add")){
                    for (String passUrl : passUrls) {
                        int begin=passUrl.indexOf(":");
                        int last=passUrl.length();
                        String permArr = passUrl.substring(begin+1,last);
                        if (permArr.equals("1")) {
                            return true;
                        }else {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        System.out.println("AccessControlFilter:onAccessDenied拦截执行");
        saveRequest(servletRequest);
        WebUtils.issueRedirect(servletRequest, servletResponse, "/login/loginPage");
        return false;
        //ajax時使用下面的
//        HttpServletResponse res = (HttpServletResponse) servletResponse;
//        res.setHeader("Access-Control-Allow-Origin", "*");
//        res.setStatus(HttpServletResponse.SC_OK);
//        res.setHeader("content-type", "text/html;charset=UTF-8");
//        res.setCharacterEncoding("UTF-8");
//        PrintWriter writer = res.getWriter();
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 702);
//        map.put("msg", "未授权");
//        writer.write(JSON.toJSONString(map));
//        writer.close();
//        return false;
    }
}
