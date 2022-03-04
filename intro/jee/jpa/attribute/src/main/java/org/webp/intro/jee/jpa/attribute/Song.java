package org.webp.intro.jee.jpa.attribute;


import javax.persistence.*;

@Entity
public class Song {

    private String title;
    private String author;

    /*
        Veritabanından bir şarkı yüklenmek istendiğinde lazy fetch kullanılırsa kullanıcı erişmek isteyene kadar veri yüklenmez.
        Erişmek için getData() gibi bir fonksiyon kullanılmalıdır.
        Peki neden kullanılır? Cevap: Performans
        Eğer yalnızca title/author'a ihtiyaç varsa veritabanındaki 4MB büyüklüğünde bir MP3 verisine ihtiyaç duyulmayabilir.
      */
    @Basic(fetch = FetchType.LAZY)
    @Lob //'L'arge 'ob'jects (büyük objeler) için ihtiyaç duyulur
    private byte[] data;

    /*
        Her bir ENUM bir integer değere sahiptir ve bu değer 0'dan başlar. Her bir ENUM değerin
        sırasını belirtmektedir (ör: WAV=0, MP3=1 vs.). Varsayılan olarak, JPA bu değerleri kullanmaktadır.
        Bu yöntem daha yüksek performans sağlar (ör: daha az alan) ancak yeni bir veri eklenmek istendiğinde veya veritabanına
        başka bir sistem tarafından erişilmek istendiğinde gerçek verilere ne olur?
        Bu yüzden gerçek STRING gösterimleri saklamak iyi olabilir.
     */
    @Enumerated(EnumType.STRING)
    private MusicFormat format;

    /*
        Herhangi bir sebep ile veritabanında maplenmeyecek yani veritabanında bulunmayacak bir sutun
        varsa @Transient kullanılabilir.
     */
    @Transient
    private Object somethingWeDoNotWantToStoreInTheDatabase;

}
