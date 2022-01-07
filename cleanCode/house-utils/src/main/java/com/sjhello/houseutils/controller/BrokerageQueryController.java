package com.sjhello.houseutils.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjhello.houseutils.constants.ActionType;
import com.sjhello.houseutils.policy.BrokeragePolicy;
import com.sjhello.houseutils.policy.BrokeragePolicyFactory;


/**
 * @author sjhello
 *
 * 중개 수수료가 얼마인지 조회하는 Controller
 * */
@RestController
public class BrokerageQueryController {

	@GetMapping("/api/calc/brokerage")
	public Long calcBrokerage(@RequestParam ActionType actionType, Long price) {

		BrokeragePolicy policy = BrokeragePolicyFactory.of(actionType);
		return policy.calculate(price);
	}
}
