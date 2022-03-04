package org.webp.intro.jee.jpa.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Burada aynı anda birden fazla alanın kısıtlanması temsil edilmektedir. Birden fazla alan kontrol edilecek ve kısıtın geçerli olup
 * olmadığı kontrol edilecekse burası kullanılabilir.
 * Örneğin, Bir kayıt bir kişi henüz doğmadan yapılamayacak: Bu iki veya daha fazla alanın kıyaslanması ile gerçekleşitirilebilir.
 * Örnek: dateOfBirth ve dateOfRegistration alanları kıyaslanabilir
 */
public class UserClassConstraintsValidator implements ConstraintValidator<UserClassConstraints, User> {


    @Override
    public void initialize(UserClassConstraints constraintAnnotation) {
        //burada yapacak bir şey yok
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {

        if(value.getDateOfBirth() == null || value.getDateOfRegistration() == null){
            return false;
        }

        //kayıt tarihi doğum tarihinden sonra ise true yoksa false dönecektir
        return value.getDateOfRegistration().compareTo(value.getDateOfBirth()) > 0;
    }
}
