package com.zm.gateway.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * ClassName: SecurityUserDetail <br/>
 * Function: 用户详情对象 <br/>
 * date: Aug 21, 2017 7:02:50 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
public class SecurityUserDetail implements UserDetails {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	private final String id;
	private final String userName;
	private final String password;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;
	private final Date lastPasswordResetDate;

	public SecurityUserDetail(String id, String userName, String password, String email,
			Collection<? extends GrantedAuthority> authorities, Date lastPasswordResetDate) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
