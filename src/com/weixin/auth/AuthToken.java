package com.weixin.auth;

public class AuthToken extends AccessToken {
	private String refreshToken;
	private String openId;
	private String scope;
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	@Override
	public String toString() {
		return super.toString()+"SnsToken [openId=" + openId + ", refreshToken=" + refreshToken
				+ ", scope=" + scope + "]";
	}
	
	
}	
