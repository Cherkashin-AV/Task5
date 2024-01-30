package ru.vtb.javaCourse.Task5.Entity;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.AbstractDriverBasedDataSource;
import org.springframework.stereotype.Component;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;

@Component
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ((beanName.equals("getDataSource") || beanName.equals("dataSource")) && bean instanceof FactoryBean) {
            try {
                DataSource embeddedDataSource = ((FactoryBean<DataSource>) bean).getObject();
                Field fld = embeddedDataSource.getClass().getDeclaredField("dataSource");
                fld.setAccessible(true);
                AbstractDriverBasedDataSource dataSource = (AbstractDriverBasedDataSource) fld.get(embeddedDataSource);
                dataSource.setUrl(dataSource.getUrl()+";NON_KEYWORDS=KEY,LEVEL,VALUE,TYPE");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
