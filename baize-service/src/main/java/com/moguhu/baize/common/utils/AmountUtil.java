package com.moguhu.baize.common.utils;

import java.math.BigDecimal;

public class AmountUtil {

	/**
	 * 基数100
	 */
	private static final BigDecimal EXCHANGE = new BigDecimal("100");

	public static final double toRiMingBi(long amount) {

		return BigDecimal.valueOf(amount).divide(EXCHANGE, 2, BigDecimal.ROUND_DOWN).doubleValue();
	}

	public static final BigDecimal toRMB(long amount) {

		return BigDecimal.valueOf(amount).divide(EXCHANGE, 2, BigDecimal.ROUND_DOWN);
	}

	
	public static final long toBaoBi(double amount) {

		return BigDecimal.valueOf(amount).multiply(EXCHANGE).longValue();
	}
	
	public static void main(String[] args) {
		
		System.out.println(AmountUtil.toRMB(1001l));
		
	}

}
