package org.webp.intro.spring.testing.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceA {

    //burada annotation yok
    private final ServiceB serviceB;

    /*
        burada "field" injection yerine "constructor" injection yapıyoruz
     */

    public ServiceA(@Autowired ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String check(long id){

        boolean ok = serviceB.isOK(id);

        if(ok){
            return "OK";
        } else {
            return "NOT OK";
        }
    }
}
