package com.chs.appconfiguration.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class AutowiringHelper implements ApplicationContextAware {

    private static final AutowiringHelper INSTANCE = new AutowiringHelper();
    private static ApplicationContext applicationContext;

    private AutowiringHelper() {
    }

    public static void autowire(final Object classToAutowire, final Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
            }
        }
    }

    public static AutowiringHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        AutowiringHelper.applicationContext = applicationContext;
    }
}
