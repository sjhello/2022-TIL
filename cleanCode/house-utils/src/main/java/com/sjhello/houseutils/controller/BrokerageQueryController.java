package com.sjhello.houseutils.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjhello.houseutils.constants.ActionType;
import com.sjhello.houseutils.policy.BrokeragePolicy;
import com.sjhello.houseutils.policy.BrokeragePolicyFactory;

@RestController
public class BrokerageQueryController {

	/*
	 * 타입 정의: 매매, 임대차
	 * */
	@GetMapping("/api/calc/brokerage")
	public Long calcBrokerage(@RequestParam ActionType actionType, Long price) {

		// TODO: 중개 수수료 계산하는 로직
		BrokeragePolicy policy = BrokeragePolicyFactory.of(actionType);
		return policy.calculate(price);
	}
}
