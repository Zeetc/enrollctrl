package com.catchiz.enrollctrl.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author Catch
 * @email catch@gmail.com
 * @date 2021-04-03 19:21:54
 */
@Data
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer roleId;
	/**
	 * 
	 */
	private String roleName;

}
