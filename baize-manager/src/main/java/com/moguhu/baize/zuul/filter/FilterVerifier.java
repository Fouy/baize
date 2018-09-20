package com.moguhu.baize.zuul.filter;

import com.moguhu.baize.common.constants.Constants;
import com.moguhu.baize.zuul.common.FilterInfo;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Zuul 过滤器校验
 */
public class FilterVerifier {

    private static final FilterVerifier instance = new FilterVerifier();

    public static FilterVerifier getInstance() {
        return instance;
    }

    /**
     * verifies compilation, instanciation and that it is a ZuulFilter
     *
     * @param sFilterCode
     * @return a FilterInfo object representing that code
     * @throws org.codehaus.groovy.control.CompilationFailedException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public FilterInfo verifyFilter(String sFilterCode) throws CompilationFailedException, IllegalAccessException, InstantiationException {
        Class groovyClass = compileGroovy(sFilterCode);
        Object instance = instanciateClass(groovyClass);
        checkZuulFilterInstance(instance);
        ZuulFilter filter = (ZuulFilter) instance;

        String compCode = FilterInfo.buildFilterId(Constants.ZUUL, filter.filterType(), groovyClass.getSimpleName());
        FilterInfo filterInfo = new FilterInfo(compCode, filter.filterType(), filter.filterOrder());
        filterInfo.setGroovyCode(sFilterCode);
        filterInfo.setGroovyFileName(groovyClass.getSimpleName());

        return filterInfo;
    }

    Object instanciateClass(Class groovyClass) throws InstantiationException, IllegalAccessException {
        return groovyClass.newInstance();
    }

    void checkZuulFilterInstance(Object zuulFilter) throws InstantiationException {
        if (!(zuulFilter instanceof ZuulFilter)) {
            throw new InstantiationException("Code is not a ZuulFilter Class ");
        }
    }

    /**
     * compiles the Groovy source code
     *
     * @param sFilterCode
     * @return
     * @throws org.codehaus.groovy.control.CompilationFailedException
     */
    public Class compileGroovy(String sFilterCode) throws CompilationFailedException {
        GroovyClassLoader loader = new GroovyClassLoader();
        return loader.parseClass(sFilterCode);
    }

}
