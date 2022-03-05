package io.security.corespringsecurity.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.security.corespringsecurity.domain.Account;
import io.security.corespringsecurity.security.service.AccountContext;
import lombok.RequiredArgsConstructor;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 검증을 위한 구현
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// AuthenticationManager로 부터 받은 인증정보에서 로그인 아이디와 password를 얻는다
		String loginId = authentication.getName();
		String password = (String)authentication.getCredentials();

		// 인증정보에서 받아온 정보를 토대로 DB에서 해당 유저가 존재하는지 판단(id 검증)
		AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(loginId);
		
		// 패스워드 검증
		// accountContext에서 가져온 유저의 패스워드와 사용자가 입력한 password 정보가 일치하는지 확인
		if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
			throw new BadCredentialsException("사용자 인증 실패");
		}

		// 아이디, 패스워드 검증이 끝났다면
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null,
			accountContext.getAuthorities());

		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// 파라미터로 전달되는 인증의 타입과 이 클래스 내부에서 사용하는 토큰의	타입과 일치할때 인증이 처리된다

		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
