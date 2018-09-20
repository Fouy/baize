package com.moguhu.baize.zuul.filter;

/**
 * Temp ZuulFilter TODO
 * <p>
 * 生产时需要 Zuul 单独提供出相关的core jar 引用即可
 */
public abstract class ZuulFilter {

    abstract public String filterType();

    abstract public int filterOrder();

}
