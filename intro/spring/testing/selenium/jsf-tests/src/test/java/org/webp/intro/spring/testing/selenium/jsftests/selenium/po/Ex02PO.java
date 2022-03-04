package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.webp.misc.testutils.selenium.PageObject;

public class Ex02PO  extends TemplatePO{

    public Ex02PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Temel layout örneği");
    }
}
