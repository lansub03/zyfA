package com.shiro.admin.shiro_admin.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
/**
 * 此类基于Shiro实现自定义Realm类
 * shiro的常用过滤器
 * authc:所有已经登录用户可以访问，登录才可以访问
 * roles:有指定角色的用户可以访问，通过[]指定具体角色，这里的角色名称和数据库中配置一致
 * perms:有指定权限的用户可以访问，通过[]自定具体权限，这里的权限名称与数据库中配置一致
 * anon:所有用户都可以访问，相当于是放行的资源，通常作为指定页面的静态资源时候进行使用
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    //授权  查询用户的权限并设置
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证用户是否登录成功
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        return null;
    }
}
