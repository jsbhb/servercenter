package com.zm.auth.model;

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
	 * serialVersionUID.
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String userName;
	private String password;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	private Date lastPasswordResetDate;

	private String token;
	private int platUserType;
	private String platId;

	public SecurityUserDetail(String id, String userName, String password, String email,
			Collection<? extends GrantedAuthority> authorities, Date lastPasswordResetDate, String platId,
			int platUserType) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
		this.lastPasswordResetDate = lastPasswordResetDate;
		this.platId = platId;
		this.platUserType = platUserType;
	}
	
	
	public SecurityUserDetail(String id, String userName, String email,
			Collection<? extends GrantedAuthority> authorities, Date lastPasswordResetDate, String platId,
			int platUserType) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.authorities = authorities;
		this.lastPasswordResetDate = lastPasswordResetDate;
		this.platId = platId;
		this.platUserType = platUserType;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public int getPlatUserType() {
		return platUserType;
	}

	public void setPlatUserType(int platUserType) {
		this.platUserType = platUserType;
	}

	public String getPlatId() {
		return platId;
	}

	public void setPlatId(String platId) {
		this.platId = platId;
	}

}
