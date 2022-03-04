package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.webp.misc.testutils.selenium.PageObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ex00PO extends PageObject {

    public Ex00PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Dinamik Sayfa Örneği");
    }

    public LocalDate getDate() {
        return LocalDate.parse(getText("dateTextId"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
