package com.debitum.assets.port.adapter.security.configuration;

import com.debitum.assets.AssetsApplication;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

@Configuration
@Profile("!" + AssetsApplication.TEST_PROFILE)
public class HystrixConfiguration implements InitializingBean {

    @Bean
    public FilterRegistrationBean hystrixRequestContextEnablerFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new HystrixRequestContextEnablerFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean securityContextHystrixRequestVariableSetterFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new SecurityContextHystrixRequestVariableSetterFilter());
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new SecurityContextRegistratorCommandHook());
    }

    public static class HystrixRequestContextEnablerFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HystrixRequestContext context = HystrixRequestContext.initializeContext();
            try {
                chain.doFilter(request, response);
            } finally {
                context.shutdown();
            }
        }

        @Override
        public void destroy() {
        }
    }

    public static class SecurityContextHystrixRequestVariableSetterFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            SecurityContextHystrixRequestVariable.getInstance().set(SecurityContextHolder.getContext());
            chain.doFilter(request, response);
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void destroy() {
        }

    }

    public static class SecurityContextHystrixRequestVariable {

        private static final HystrixRequestVariableDefault<SecurityContext> securityContextVariable = new HystrixRequestVariableDefault<>();

        private SecurityContextHystrixRequestVariable() {
        }

        public static HystrixRequestVariableDefault<SecurityContext> getInstance() {
            return securityContextVariable;
        }

    }

    /**
     * A HystrixCommandExecutionHook that makes the Spring SecurityContext available during the execution of HystrixCommands.
     * <p>
     * It extracts the SecurityContext from the SecurityContextHystrixRequestVariable and sets it on the SecurityContextHolder.
     */
    public static class SecurityContextRegistratorCommandHook extends HystrixCommandExecutionHook {

        @Override
        public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
            SecurityContextHolder.setContext(SecurityContextHystrixRequestVariable.getInstance().get());
        }

        /**
         * Clean the SecurityContext
         */
        @Override
        public <T> T onEmit(HystrixInvokable<T> commandInstance, T response) {
            SecurityContextHolder.clearContext();
            return response;
        }

        /**
         * Clean the SecurityContext
         */
        @Override
        public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
            SecurityContextHolder.clearContext();
            return e;
        }

    }
}
