package org.webp.intro.spring.security.framework.selenium.po;

import org.webp.misc.testutils.selenium.PageObject;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ProtectedPO extends PageObject {

    public ProtectedPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Korunan Kaynak");
    }

    public IndexPO doLogout(){

        clickAndWait("logoutBtnId");
        IndexPO po = new IndexPO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
