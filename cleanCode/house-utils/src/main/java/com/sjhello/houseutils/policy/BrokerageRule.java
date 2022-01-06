package com.sjhello.houseutils.policy;

import lombok.AllArgsConstructor;

/*
* 가격이 특정 범위 일때 상한효율과 상한금액을 가지는 클래스
* */
@AllArgsConstructor
public class BrokerageRule {

	private Double brokeragePercent;
	private Long limitAmount;

	public Long calculateMaxBrokerage(Long price) {
		if (limitAmount == null) {
			// 상한 효율과 상한 금액을 곱하는 값을 리턴한다
			return multiplyPercent(price);
		}

		// 한도 금액이 있다면
		return Math.min(multiplyPercent(price), limitAmount);
	}

	private Long multiplyPercent(Long price) {
		return Double.valueOf(Math.floor(brokeragePercent / 100 * price)).longValue();
	}
}
