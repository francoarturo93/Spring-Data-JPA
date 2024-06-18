package com.franco.curso.springboot.jpa.springbootjpa.dto;
// es una clase personalizada y simplificada de una clase entitie
// que vamos a entregar al usuario final 
public class PersonDto {
    private String name;
    private String lastname;
 
    public PersonDto(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    @Override
    public String toString() {
        return "PersonDto [name=" + name + ", lastname=" + lastname + "]";
    }

    
}
