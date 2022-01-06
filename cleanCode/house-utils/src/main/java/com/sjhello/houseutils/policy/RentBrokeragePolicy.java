package com.sjhello.houseutils.policy;


/*
* 임대차일때 중개 수수료를 계산해주는 클래스
* */
public class RentBrokeragePolicy implements BrokeragePolicy {

	// TODO: 가격을 받아서 중개수수료를 계산
	// 가격의 범위에 따라서 리턴하는 값이 다르다

	@Override
	public BrokerageRule createBrokerageRule(Long price) {
		BrokerageRule rule;

		if (price < 50_000_000) {
			rule = new BrokerageRule(0.5, 200_000L);
		} else if (price < 100_000_000) {
			rule = new BrokerageRule(0.4, 300_000L);
		} else if (price < 300_000_000) {
			rule = new BrokerageRule(0.3, null);
		} else if (price < 600_000_000) {
			rule = new BrokerageRule(0.4, null);
		} else {
			rule = new BrokerageRule(0.9, null);
		}

		return rule;
	}
}
