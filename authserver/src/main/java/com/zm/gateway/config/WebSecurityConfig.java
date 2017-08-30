package com.zm.gateway.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.zm.gateway.auth.AuthenticationTokenFilter;
import com.zm.gateway.service.UserService;

/**
 * @author
 * @version 1.0 2016/10/10
 * @description
 */
@Configuration
@EnableWebSecurity
// 用于@PreAuthorize的生效,基于方法的权限控制
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 覆盖默认的spring security提供的配置
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// http
		// // 禁用CSRF保护
		// .csrf().disable()
		// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		//
		// .authorizeRequests()
		// // 任何访问都必须授权
		// .anyRequest().fullyAuthenticated()
		// // 配置那些路径可以不用权限访问
		// .mvcMatchers("/auth/**").permitAll().and().formLogin()
		// // 登陆成功后的处理，因为是API的形式所以不用跳转页面
		// .successHandler(new RestAuthenticationSuccessHandler())
		// // 登陆失败后的处理
		// .failureHandler(new SimpleUrlAuthenticationFailureHandler()).and()
		// // 登出后的处理
		// .logout().logoutSuccessHandler(new RestLogoutSuccessHandler()).and()
		// // 认证不通过后的处理
		// .exceptionHandling().authenticationEntryPoint(new
		// RestAuthenticationEntryPoint());

		// 由于使用的是JWT，我们这里不需要csrf
		httpSecurity.csrf().disable()
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				// 对于获取token的rest api要允许匿名访问
				.antMatchers("/auth/**").permitAll()
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated();

		// 禁用缓存
		httpSecurity.headers().cacheControl();

		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("configure authenticationmanagerbuilder");
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// 密码加密
		return new BCryptPasswordEncoder(16);
	}

	/**
	 * 登陆成功后的处理
	 */
	public static class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			System.out.println("success!!!!");
			clearAuthenticationAttributes(request);
			System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
			PrintWriter writer = response.getWriter();
			writer.write("success");
			writer.close();
			writer.flush();
		}
	}

	/**
	 * 登出成功后的处理
	 */
	public static class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			// Do nothing!
		}
	}

	/**
	 * 权限不通过的处理
	 */
	public static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException {
			System.out.println("error!!!!!!!!!!!!!!!");

			System.out.println(SecurityContextHolder.getContext());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "认证失败" + authException.getMessage());
		}
	}

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		return new AuthenticationTokenFilter();
	}

}
