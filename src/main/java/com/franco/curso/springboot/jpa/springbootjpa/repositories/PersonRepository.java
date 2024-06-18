package com.franco.curso.springboot.jpa.springbootjpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.franco.curso.springboot.jpa.springbootjpa.dto.PersonDto;
import com.franco.curso.springboot.jpa.springbootjpa.entities.Person;
import java.util.List;
import java.util.Optional;

//DAO
// consultas personalizadas 
// para poder ver, crear, listar, eliminar, ver por id
public interface PersonRepository extends CrudRepository<Person, Long> {

    // ==============whereIn=======================
    
    // @Query("select p from Person p where p.id not in ?1")
    @Query("select p from Person p where p.id in ?1")
    public List<Person> getPersonsByIds(List<Long> ids);
    // ===============================================

    // ==============subQueries=======================
    @Query("select p.name, length(p.name) from Person p where length(p.name)=(select min(length(p.name)) from Person p)")
    public List<Object[]> getShorterName(); //obtener el nombre mas corto

    @Query("select p from Person p where p.id=(select max(p.id) from Person p)")
    public Optional<Person> getLastRegistration();
    // ===============================================================

    // ==============queriesFunctionAggregation=======================
    @Query("select min(p.id), max(p.id), sum(p.id), avg(length(p.name)), count(p.id) from Person p")
    public Object getResumeAggregationFunction();

    @Query("select min(length(p.name)) from Person p")
    public Integer getMinLengthName();

    @Query("select max(length(p.name)) from Person p")
    public Integer getMaxLengthName();

    @Query("select p.name, length(p.name) from Person p")// devuelve el [0]nomre y su [1]tamaño y asi...
    public List<Object[]> getPersonNameLength();

    @Query("select count(p) from Person p")
    Long getTotalPerson();

    @Query("select min(p.id) from Person p")
    Long getMinId();

    @Query("select max(p.id) from Person p")
    Long getMaxId();
    // ================================================================

    // ==============personalizedQueriesBetween=======================
    List<Person> findAllByOrderByNameAscLastnameDesc();
    //---------------------------------------------
    
    List<Person> findByIdBetweenOrderByNameAsc(Long id1, Long id2);
    
    @Query("select p from Person p where p.id between ?1 and ?2 order by p.name desc")
    List<Person> findAllBetweenId(Long id1, Long id2);
    
     //-----------------------------------------
    List<Person> findByNameBetweenOrderByNameDescLastnameDesc(String name1, String name2);
    
    @Query("select p from Person p where p.name between ?1 and ?2 order by p.name asc, p.lastname desc")
    List<Person> findAllBetweenName(String c1, String c2);
    // ===============================================================================
 
    // ==============personalizedQueriesConcatUpperAndLowerCase=======================
    @Query("select p.id, upper(p.name), lower(p.lastname), upper(p.programmingLanguage) from Person p")
    List<Object[]> findAllPersonDataListCase();

    @Query("select upper(p.name || ' ' || p.lastname) from Person p")
    List<String> findAllFullNameConcatUpper();

    @Query("select lower(concat(p.name, ' ', p.lastname)) from Person p")
    List<String> findAllFullNameConcatLower();

    // @Query("select CONCAT(p.name, ' ', p.lastname) from Person p")
    @Query("select p.name || ' ' || p.lastname from Person p")
    List<String> findAllFullNameConcat();
    // ==============================================================

    // ==============personalizedQueriesDistinct=======================
    @Query("select count(distinct(p.programmingLanguage)) from Person p")
    Long findAllProgrammingLanguageDistinctCount();

    @Query("select distinct(p.programmingLanguage) from Person p")
    List<String> findAllProgrammingLanguageDistinct();

    @Query("select p.name from Person p")
    List<String> findAllNames();

    @Query("select distinct(p.name) from Person p")
    List<String> findAllNamesDistinct();
    // ==================================================

    // ================personalizedQueries2====================

    @Query("select p, p.programmingLanguage from Person p")
    List<Object[]> findAllMixPerson();
    
    @Query("select new com.franco.curso.springboot.jpa.springbootjpa.dto.PersonDto(p.name, p.lastname) from Person p")
    List<PersonDto> findAllPersonDto();

    @Query("select new Person(p.name, p.lastname) from Person p")
    List<Person> findAllObjectPersonPersonalized();

    // ==================================================

    // ================personalizedQueries====================

    @Query("select p.name from Person p where p.id=?1")
    String getNameById(Long id);

    @Query("select p.id from Person p where p.id=?1")
    Long getIdById(Long id);

    @Query("select CONCAT(p.name, ' ', p.lastname) as fullname from Person p where p.id=?1")
    String getFullNameById(Long id);
    // --------------------
    @Query("select p.id, p.name, p.lastname, p.programmingLanguage from Person p")
    List<Object[]> obtenerPersonDataList();

    @Query("select p.id, p.name, p.lastname, p.programmingLanguage from Person p where p.id=?1")
    Optional<Object> obtenerPersonDataById(Long id);
    
    // ==================================================

    // ===================findOne========================

    @Query("select p from Person p where p.id=?1")
    Optional<Person> findOneId(Long id);
    
    @Query("select p from Person p where p.name like %?1%")
    Optional<Person> findOneLikeName(String name);

    // lo mismo que findOneLikeName
    Optional<Person> findByNameContaining(String name);
    
    Optional<Person> findByName(String name);

    // ==================================================

    // ====================list=========================
    
    List<Person> findByProgrammingLanguage(String programmingLanguage);
    
    // @Query("select p from Person p where p.programmingLanguage=?1 and p.name=?2")
    // List<Person> buscarByProgrammingLanguage(String programmingLanguage, String name);
    // son equivalentes up/down
    List<Person> findByProgrammingLanguageAndName(String programmingLanguage, String name);
    
    @Query("select p.name, p.programmingLanguage from Person p")
    List<Object[]> obtenerPersonData();
    
    @Query("select p.name, p.lastname from Person p where p.programmingLanguage=?1")
    List<Object[]> obtenerPersonData(String programmingLanguage);
    
    @Query("select p.name, p.programmingLanguage from Person p where p.programmingLanguage=?1 and p.name=?2")
    List<Object[]> obtenerPersonData(String programmingLanguage, String name);
    // ==================================================
    
}
