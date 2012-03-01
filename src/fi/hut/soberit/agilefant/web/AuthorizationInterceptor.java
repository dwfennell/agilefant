package fi.hut.soberit.agilefant.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.Interceptor;

import fi.hut.soberit.agilefant.business.UserBusiness;



@Component("authorizationInterceptor")
public class AuthorizationInterceptor extends AbstractInterceptor {

    //@Autowired
    //private UserBusiness userBusiness;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("authorizationinterceptor invocated");
        
        // if we could somehow figure out here what product is the user trying to access, 
        // and check it againts the database
        
        return invocation.invoke();
    }

}
