package com.zm.gateway;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 
 * ClassName: Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Jul 31, 2017 3:36:52 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(GateWayApplication.class).web(true).run(args);
	}
	
	@Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

	public final class AccessFilter extends ZuulFilter {

		@Override
		public String filterType() {
			return "pre";
		}

		@Override
		public int filterOrder() {
			return 0;
		}

		@Override
		public boolean shouldFilter() {
			return true;
		}

		@Override
		public Object run() {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();

			Object accessToken = request.getParameter("accessToken");
			if (accessToken == null) {

				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				return null;
			}
			return null;
		}

	}

}
