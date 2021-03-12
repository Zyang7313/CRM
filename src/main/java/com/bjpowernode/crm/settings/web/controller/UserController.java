package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.services.UserService;
import com.bjpowernode.crm.settings.services.imp.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Clue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //拿到web.xml中的url-pattern
        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/xxx.do".equals(path)){

            //xxx(request,response);

        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {


        System.out.println("进入到登录验证操作");

        //取得账号、密码
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);

        //接收ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip------------:"+ip);

        //创建service对象
        //未来业务层开发，统一使用代理类形态的接口对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {

            User user = userService.login(loginAct,loginPwd,ip);

            request.getSession().setAttribute("user",user);




            //如果程序执行到此处，说明业务层没有为controller抛出任何异常
            //表示登陆成功
            //则只需要返回{"success":true}

            PrintJson.printJsonFlag(response,true);

        } catch (Exception e) {
            //一旦程序执行了catch块中的信息，说明业务层验证登录失败，为controller抛出了异常
            //表示登录失败
            //需要返回{"success":false,"msg":?}
            e.printStackTrace();

            String msg = e.getMessage();

            /*
                我们现在作为controller，需要为ajax请求提供多项信息，有两种手段：
                    1）将多项信息打包成map，将map解析为json串
                    2）创建一个vo
                        private boolean success;
                        private String msg;
                如果对于展现的信息将来还会大量使用，则使用vo，使用方便
                如果对于展现的信息只有在这个需求中使用，则使用map
             */

            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }

    }
}
