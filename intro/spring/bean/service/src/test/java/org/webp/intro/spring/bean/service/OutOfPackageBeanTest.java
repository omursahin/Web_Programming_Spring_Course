package org.webp.intro.spring.bean.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.intro.spring.bean.service.root.Application;
import org.webp.intro.spring.bean.service.root.BaseSingleton;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(SpringExtension.class)
//Application iç pakette olduğundan SpringBoot giriş noktasını açıkça belirtmek gerekir
@SpringBootTest(classes = Application.class)
public class OutOfPackageBeanTest {

    @Autowired
    private BaseSingleton inPackage;

    /*
        Bu bean bulunamayacak çünkü Application neredeyse oradan taramaya başlayacak ve alt paketleri
        tarayacak

        "required=false" anlamı Spring bu bean'i bulamasa da çalışmaya devam etmesini söylemektir
        Yoksa Spring çalışma zamanında yalnızca exception fırlatacaktır
     */
    @Autowired(required = false)
    private OutOfPackageBean outOfPackageBean;

    @Test
    public void testBeanScanning(){

        //bean bulunamadı bu yüzden bağlı (wired) değil
        assertNull(outOfPackageBean);

        //bu bean bulundu ve bağlı (wired)
        assertNotNull(inPackage);
    }
}