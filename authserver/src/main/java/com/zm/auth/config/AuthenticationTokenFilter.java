package com.zm.auth.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zm.auth.common.JWTUtil;
import com.zm.auth.model.SecurityUserDetail;
import com.zm.auth.model.UserInfo;
import com.zm.auth.service.UserService;

import io.jsonwebtoken.Claims;

/**
 * 
 * ClassName: AuthenticationTokenFilter <br/>
 * Function: 令牌权限过滤器. <br/>
 * date: Aug 21, 2017 7:35:58 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Value("${wx_openId_secret}")
	private String WX_OPENID_SECRET;

	/**
	 * 
	 * 实现验证头部是否有令牌信息.
	 * 
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		Map<String, String> headerMap = getHeadersInfo(request);

		String authHeader = headerMap.get(header);

		if (authHeader != null && authHeader.startsWith(tokenHead)) {
			final String authToken = authHeader.substring(tokenHead.length());

			Claims claims = JWTUtil.getClaimsFromToken(authToken);

			logger.info("checking authentication " + claims);
			UserInfo user = null;
			if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				try {

					user = new UserInfo();
					Object platUserTypeStr = claims.get(JWTUtil.PLATUSERTYPE);
					if (platUserTypeStr != null) {
						user.setPlatUserType(Integer.valueOf(platUserTypeStr.toString()));
					}
					SecurityUserDetail userDetails = null;
					if (claims.containsKey(JWTUtil.PLATFORM_ID)) {
						user.setUserId((String) claims.get(JWTUtil.PLATFORM_ID));
						userDetails = (SecurityUserDetail) this.userService.loadUserByPlatId(user);
					} else if (claims.containsKey(JWTUtil.USER_NAME)) {
						user.setUserName((String) claims.get(JWTUtil.USER_NAME));
						userDetails = (SecurityUserDetail) this.userService.loadUserByUsername(user);
					} else if (claims.containsKey(JWTUtil.OPEN_ID)) {
						user.setUserName((String) claims.get(JWTUtil.OPEN_ID) + "," + WX_OPENID_SECRET);
						userDetails = (SecurityUserDetail) this.userService.loadUserByUsername(user);

						if (userDetails != null) {
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							logger.info("authenticated openId " + claims.get(JWTUtil.OPEN_ID)
									+ ", setting security context");
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
						return;

					} else if (claims.containsKey(JWTUtil.APPKEY)) {
						user.setUserName((String) claims.get(JWTUtil.APPKEY));
						userDetails = (SecurityUserDetail) this.userService.loadUserByUsername(user);
						if (userDetails != null) {
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							logger.info("authenticated appKey " + claims.get(JWTUtil.APPKEY)
									+ ", setting security context");
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
						return;
					}

					if (JWTUtil.validateToken(authToken, userDetails)) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						logger.info("authenticated user " + userDetails.getUsername() + ", setting security context");
						SecurityContextHolder.getContext().setAuthentication(authentication);
						return;
					}
				} catch (Exception e) {
					chain.doFilter(request, response);
				}
			}
		}
		chain.doFilter(request, response);
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}

}
