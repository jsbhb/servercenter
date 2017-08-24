package com.zm.gateway;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

	private String url = "http://127.0.0.1:4444/authentication";

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

			// 判断是否无需进行令牌验证
			if (checkPath(request.getRequestURL().toString())) {
				return null;
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("authorization", request.getHeader("authorization"));

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);

			try {
				ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

				System.out.println(result.getBody());

				String tip = result.getBody();
				if (tip == null || tip.equals("null")) {
					ctx.setSendZuulResponse(false);
					ctx.setResponseStatusCode(401);
					ctx.set("content-type", "application/json");
					ctx.setResponseBody("无权限访问！");
					return null;
				}

			} catch (Exception e) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody(e.getMessage());
				return null;
			}

			// Object accessToken = request.getParameter("accessToken");
			// if (accessToken == null) {
			//
			// ctx.setSendZuulResponse(false);
			// ctx.setResponseStatusCode(401);
			// return null;
			// }

			return null;
		}

		private boolean checkPath(String contextPath) {

			if ((contextPath != null)
					&& (contextPath.contains("authcenter/auth/login") || contextPath.contains("authcenter/auth/refresh")
							|| contextPath.contains("authcenter/auth/register"))) {
				return true;
			}

			return false;
		}

	}

}
