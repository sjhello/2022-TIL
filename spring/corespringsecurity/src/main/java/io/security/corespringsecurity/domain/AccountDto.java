package io.security.corespringsecurity.domain;

import lombok.Data;

@Data
public class AccountDto {

	private String userName;
	private String password;
	private String age;
	private String email;
	private String role;
}
