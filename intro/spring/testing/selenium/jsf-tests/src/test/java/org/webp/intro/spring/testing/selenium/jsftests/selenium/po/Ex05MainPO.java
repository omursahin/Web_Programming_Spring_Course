package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.webp.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class Ex05MainPO extends TemplatePO{

    public Ex05MainPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("URL Parametre Örneği");
    }

    public Ex05ParamsPO clickOnNoParamsLink(){
        clickAndWait("linkNoParamsId");

        Ex05ParamsPO po = new Ex05ParamsPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex05ParamsPO clickOnOneParamLink(){
        clickAndWait("linkOneParamId");

        Ex05ParamsPO po = new Ex05ParamsPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex05ParamsPO clickOnParamNoValueLink(){
        clickAndWait("linkOneParamNoValueId");

        Ex05ParamsPO po = new Ex05ParamsPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex05ParamsPO clickOnMultiParamsLink(){
        clickAndWait("linkMultiParamsId");

        Ex05ParamsPO po = new Ex05ParamsPO(this);
        assertTrue(po.isOnPage());

        return po;
    }


}
