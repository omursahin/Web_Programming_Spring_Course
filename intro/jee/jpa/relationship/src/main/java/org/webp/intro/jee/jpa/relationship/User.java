package org.webp.intro.jee.jpa.relationship;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    @OneToOne(fetch = FetchType.EAGER) //OneToOne icin Eager varsayilandir o yuzden belirtmeye gerek yok.
    //Address kendi tablosuna sahip oldugundan User icerisinde bir foreign key olarak tutacagiz.
    //Not: Address'ten User'a bir baglanti yoktur bu tur iliskilere unidirectional denir
    private Address address;


    //Bu OneToOne cift yonludur (bidirectional), burada ozel bir ayarlama yapmaya gerek yoktur
    @OneToOne
    private AddressWithUserLink addressWithUserLink;


    @OneToMany(fetch = FetchType.LAZY) //Burada Lazy varsayilandir cunku veri yapisi buyuk olabilir
    private List<Message> sentMessages;


    //Cift yonlu. Bu kullanici uzerindeki butun islemler (update gibi) butun mesajlara kademeli olarak aktarilir
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.ALL)
    private List<MessageWithUserLink> sentMessagesWithSenderLink;

    @ManyToMany
    /*
        Burada bir liste olmasi en iyisidir ancak bazen bir collection da olabilir
     */
    private Map<Long, GroupAssignment> assignments;

    //String gibi raw-data tipi varsa bu annotation'in kullanimina ihtiyac bulunmaktadir
    @ElementCollection
    private List<String> roles;

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Long, GroupAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<Long, GroupAssignment> assignments) {
        this.assignments = assignments;
    }

    public List<MessageWithUserLink> getSentMessagesWithSenderLink() {
        return sentMessagesWithSenderLink;
    }

    public void setSentMessagesWithSenderLink(List<MessageWithUserLink> sentMessagesWithSenderLink) {
        this.sentMessagesWithSenderLink = sentMessagesWithSenderLink;
    }


    public AddressWithUserLink getAddressWithUserLink() {
        return addressWithUserLink;
    }

    public void setAddressWithUserLink(AddressWithUserLink addressWithUserLink) {
        this.addressWithUserLink = addressWithUserLink;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
