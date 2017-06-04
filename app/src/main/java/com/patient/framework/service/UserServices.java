package com.patient.framework.service;

import android.content.Context;
import com.patient.framework.model.User;
import com.patient.framework.repository.UserRepository;

public class UserServices {

    private UserRepository userRepository;

    public UserServices(Context context) {
        userRepository=new UserRepository(context);
    }


    public String register(User user){
        int result=userRepository.addUser(user);
        switch(result){
            case 1: return "注册成功！即将返回首页登录...";
            case 0: return "注册失败，请您稍后再次尝试";
            case -1: return "注册失败，用户已存在";
            default: return "后台出现未知错误";
        }
    }

    public String login(User user){
        int result=userRepository.isValid(user);
        switch(result) {
            case 1:
                return "登录成功";
            case 0:
                return "登录失败，邮箱或密码错误";
            case -1:
                return "用户不存在";
            default:
                return "登录失败，邮箱或密码错误";
        }
    }

    public String update(User user){
        if(userRepository.updateUserInfo(user)){
            return "已更新";
        }else{
            return "更新失败";
        }
    }

    public String resetPsw(User user){
        int result=userRepository.resetUserPsw(user);
        switch(result){
            case 1: return "密码重置成功！即将返回首页登录...";
            case 0: return "后台出现未知错误";
            case -1: return "密码重置失败，填写信息与用户信息不一致";
            default: return "后台出现未知错误";
        }
    }

    public String changePsw(User user,String newPsw){
        if(userRepository.updateUserPsw(user,newPsw)){
            return "密码修改成功";
        }else{
            return "密码修改失败";
        }
    }

    public String resign(User user){
        if(userRepository.deleteUser(user)){
            return "注销成功";
        }else{
            return "注销失败";
        }
    }

}
