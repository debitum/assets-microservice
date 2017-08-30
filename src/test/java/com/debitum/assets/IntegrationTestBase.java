package com.debitum.assets;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AssetsApplication.class})
public abstract class IntegrationTestBase extends AbstractTransactionalJUnit4SpringContextTests {

    @Value("${spring.mail.port}")
    protected int WISE_MAIL_PORT;

    protected Wiser wiser;

    @Before
    public final void setUpOneTagCodeEmailSendingServiceTest() {
        wiser = new Wiser();
        wiser.setPort(WISE_MAIL_PORT);
        wiser.start();
    }

    @After
    public void destroyOneTagCodeEmailSendingServiceTestConfigs() {
        wiser.stop();
    }

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(ExternalSourcesStubServer.getDefaultConfig());


    @PersistenceContext
    protected EntityManager em;
    @Inject
    protected ScheduledAnnotationBeanPostProcessor scheduler;

    @Before
    public final void setUpIntegrationTestBase() {
        scheduler.destroy();
    }

    protected void flush(Runnable runnable) {
        runnable.run();
        flush();
    }

    protected void flush() {
        em.flush();
        em.clear();
    }

    protected <T> T flushAndGet(Supplier<T> supplier) {
        T result = supplier.get();
        flush();
        return result;
    }
}