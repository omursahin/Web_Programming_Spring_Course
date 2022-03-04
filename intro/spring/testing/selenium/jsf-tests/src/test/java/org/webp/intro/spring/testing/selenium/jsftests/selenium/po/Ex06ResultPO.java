package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.webp.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class Ex06ResultPO extends TemplatePO {

    public Ex06ResultPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Yönlendirme Görüntüleme Sayfası");
    }


    public Ex06MainPO clickBackToMain(){
        clickAndWait("linkMainId");

        Ex06MainPO po = new Ex06MainPO(this);
        assertTrue(po.isOnPage());

        return po;
    }


    public int getDisplayedCounter(){
        return getInteger("counterId");
    }
}
