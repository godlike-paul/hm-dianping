package com.hmdp.utils;

import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        User user= (User) session.getAttribute("user");

        if(user==null){
            //401未授权
            response.setStatus(401);
            return false;
        }
        UserDTO user1=new UserDTO();
        user1.setId(user.getId());
        user1.setNickName(user.getNickName());
        user1.setIcon(user.getIcon());
        UserHolder.saveUser(user1);


        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
