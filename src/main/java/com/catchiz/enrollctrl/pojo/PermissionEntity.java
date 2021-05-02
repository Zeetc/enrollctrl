package com.catchiz.enrollctrl.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
public class PermissionEntity implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer permissionId;
	/**
	 * 
	 */
	private String permissionName;

	@Override
	public String getAuthority() {
		return permissionName;
	}
}
