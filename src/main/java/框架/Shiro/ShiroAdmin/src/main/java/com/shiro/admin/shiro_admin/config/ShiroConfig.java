package com.shiro.admin.shiro_admin.config;


import com.shiro.admin.shiro_admin.shiro.ShiroRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    //1、创建Shiro管理器对象
    @Bean
    public SecurityManager createSM(ShiroRealm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }


    //2、配置Shiro工厂对象
    @Bean
    public ShiroFilterFactoryBean createSFFB(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroBean = new ShiroFilterFactoryBean();
        shiroBean.setSecurityManager(securityManager);
        //设置三个urlssss
        shiroBean.setLoginUrl("/admin/login");//登录接口
        shiroBean.setSuccessUrl("/admin/index");//登录成功
        shiroBean.setUnauthorizedUrl("/admin/error");//登录失败

        //设置拦截的接口，哪些需要放行   那些需要拦截

        //ssss这里用的map集合必须可以保证添加顺序，所以我们使用LinkedHashMap
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //设置放行的资源
        filterChainDefinitionMap.put("/admin/test","anon");//代表这个路径是所有用户都可以访问的
        filterChainDefinitionMap.put("/admin/login","anon");//代表这个路径是所有用户都可以访问的
        //设置拦截的资源
        filterChainDefinitionMap.put("/**","authc");//设置拦截的路径 **代表所有

        //设置拦截规则，设置拦截规则之后，需要将shiroBean对象进行返回
        shiroBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroBean;

    }

}
