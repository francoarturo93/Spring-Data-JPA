# Api Spring Data JPA
## Descripción del proyecto

Este proyecto es una aplicación de ejemplo que utiliza Spring Boot junto con Spring Data JPA para gestionar una entidad `Persona`. 
La aplicación se conecta a una base de datos MySQL y carga automáticamente datos iniciales desde un archivo `import.sql`.
Además, utiliza `CommandLineRunner` para ejecutar consultas y mostrar los resultados en la consola.

## Entidad `Persona`
La entidad Persona tiene los siguientes atributos:

- `name`: El nombre de la persona.
- `lastname`: El apellido de la persona.
- `programmingLanguage`: El lenguaje de programación de la persona.

## Configuracion del Proyecto
### Recursos
El proyecto esta creado con las siguientes tecnologías, las primeras 3 se obtienen
al momento de crear el proyecto.

| Plugin                |
|-----------------------|
| Spring Boot DevTools  | 
| MySQL JDBC Driver     | 
| Spring Data JPA       |

## Configuracion de Base de Datos
En el archivo `src/main/resources/application.properties`, configuramos la conexión a la base de datos MySQL:
```
spring.application.name=springboot-jpa
spring.datasource.url=jdbc:mysql://localhost:3306/db_springboot
spring.datasource.username=****
spring.datasource.password=****
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```
## Archivo de Importacion de Datos
Creamos un archivo `src/main/resources/import.sql` con las primeras seis personas a insertar:
```java
INSERT INTO persons (name, lastname, programming_language) VALUES ('Franco', 'Aguilar', 'Java');
INSERT INTO persons (name, lastname, programming_language) VALUES ('Diego', 'Vargas', 'Python');
INSERT INTO persons (name, lastname, programming_language) VALUES ('Clau', 'Del Rosario', 'JavaScript');
INSERT INTO persons (name, lastname, programming_language) VALUES ('Maria', 'Cervantes', 'Php');
INSERT INTO persons (name, lastname, programming_language) VALUES ('Trinidad', 'Quispe', 'Java');
INSERT INTO persons (name, lastname, programming_language) VALUES ('Angel', 'Pinedo', 'C#');
```

## Implementacion
### Entidad `Persona`
Creamos la clase `Persona` en el paquete `com.franco.curso.springboot.jpa.springbootjpa.entities`:

```java
@Entity
@Table(name="persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    
    @Column(name = "programming_language")
    private String programmingLanguage;

    public Person() {
    }
    
    public Person(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }
    public Person(Long id, String name, String lastname, String programmingLanguage) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.programmingLanguage = programmingLanguage;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + ", lastname=" + lastname + ", programmingLanguage="
                + programmingLanguage + "]";
    }

}
```

### Repositorio `PersonRepository`
Creamos la interfaz `PersonRepository` en el paquete `com.franco.curso.springboot.jpa.springbootjpa.repositories`:
```java
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
```

### Clase Principal
Modificamos la clase principal de la aplicación para implementar `CommandLineRunner` y ejecutar consultas en la consola:
```java
@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner{

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//trabajando con la persistencia
		// list();
		// findOne();
		// create();
		// delete();
		// delete2();
		// update();
		// personalizedQueries2();
		// personalizedQueriesDistinct();
		// personalizedQueriesConcatUpperAndLowerCase();
		// personalizedQueriesBetween();
		// queriesFunctionAggregation();
		whereIn();

	}

	@Transactional(readOnly = true)
	public void whereIn() {
		System.out.println("================== consulta where in ==================");
		List<Person> persons = repository.getPersonsByIds(Arrays.asList(1L, 2L, 5L, 7L));
		persons.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void queriesFunctionAggregation() {
		
		System.out.println("================== consulta con el total de registros de la tabla persona ==================");
		Long count = repository.getTotalPerson();
		System.out.println(count);
		
		System.out.println("================== consulta con el valor minimo del id ==================");
		Long min = repository.getMinId();
		System.out.println(min);
		
		System.out.println("================== consulta con el valor maximo del id");
		Long max = repository.getMaxId();
		System.out.println(max);
		
		System.out.println("================== consulta con el nombre y su largo ==================");
		List<Object[]> regs = repository.getPersonNameLength();
		regs.forEach(reg -> {
			String name = (String) reg[0];
			Integer length = (Integer) reg[1];
			System.out.println("name=" + name + ", length=" + length);
		});
		
		System.out.println("================== consulta con el nombre mas corto ==================");
		Integer minLengthName = repository.getMinLengthName();
		System.out.println(minLengthName);
		
		System.out.println("================== consulta con el nombre mas largo ==================");
		Integer maxLengthName = repository.getMaxLengthName();
		System.out.println(maxLengthName);

		System.out.println("================== consultas resumen de funciones de agregacion min, max, sum, avg, count ==================");
		Object[] resumeReg = (Object[]) repository.getResumeAggregationFunction();
		System.out.println(
			    "min=" + resumeReg[0] +
				", max=" + resumeReg[1] +
				", sum=" + resumeReg[2] +
				", avg=" + resumeReg[3] +
		        ", count=" + resumeReg[4]);
	}

	@Transactional(readOnly=true)
	public void personalizedQueriesBetween() {
		System.out.println("================== consultas por  ==================");
		List<Person> persons2 = repository.findAllBetweenId(2L, 5L);
		persons2.forEach(System.out::println);
		System.out.println("================== consultas por rangos ==================");
		List<Person> persons = repository.findByIdBetweenOrderByNameAsc(2L, 5L);
		persons.forEach(System.out::println);
		
		persons = repository.findByNameBetweenOrderByNameDescLastnameDesc("J", "Q");
		persons.forEach(System.out::println);

		persons = repository.findAllByOrderByNameAscLastnameDesc();
		persons.forEach(System.out::println);

	}

	@Transactional(readOnly = true)
	public void personalizedQueriesConcatUpperAndLowerCase() {
		System.out.println("================== consulta nombres y apellidos de personas ==================");
		List<String> names = repository.findAllFullNameConcat();
		names.forEach(System.out::println);

		System.out.println("================== consulta nombres y apellidos mayuscula ==================");
		names = repository.findAllFullNameConcatUpper();
		names.forEach(System.out::println);

		System.out.println("================== consulta nombres y apellidos minuscula ==================");
		names = repository.findAllFullNameConcatLower();
		names.forEach(System.out::println);
		System.out.println("================== consulta personalizada persona upper y lower case ==================");
		List<Object[]> regs = repository.findAllPersonDataListCase();
		regs.forEach(reg -> System.out.println("id="+ reg[0] + ", nombre=" + reg[1] + ", apellido=" + reg[2]+ ", lenguaje="+reg[3]));

	}
	//listar valores unicos, que no se repitan
	@Transactional(readOnly = true)
	public void personalizedQueriesDistinct() {
		System.out.println("================== consultas con nombres de personas ==================");
		List<String> names = repository.findAllNames();
		names.forEach(System.out::println);

		System.out.println("==================  consultas con nombres unicos de personas ==================");
		names = repository.findAllNamesDistinct();
		names.forEach(System.out::println);

		System.out.println("================== consulta con lenguaje de programacion unicas ==================");
		List<String> languages = repository.findAllProgrammingLanguageDistinct();
		languages.forEach(System.out::println);

		System.out.println(
				"================== consulta con total de lenguajes de programacion unicas ==================");
		Long totalLanguage = repository.findAllProgrammingLanguageDistinctCount();
		System.out.println("total de lenguajes de programacion: " + totalLanguage);

	}
	
	@Transactional(readOnly = true)
	public void personalizedQueries2() {
		System.out.println("================== consulta por objeto persona y lenguaje de programacion ==================");
		List<Object[]> personsRegs = repository.findAllMixPerson();

		personsRegs.forEach(reg -> {
			System.out.println("programmingLanguage=" + reg[1] + ", person=" + reg[0]);
		});

		System.out.println("================== consulta que puebla y devuelve objeto entity de una instancia personalizada ==================");
		List<Person> persons = repository.findAllObjectPersonPersonalized();
		persons.forEach(System.out::println);

		System.out.println("================== consulta que puebla y devuelve objeto dto de una clase personalizada ==================");
		List<PersonDto> personsDto = repository.findAllPersonDto();
		personsDto.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueries() {

		Scanner scanner = new Scanner(System.in);

		System.out.println("================== consulta solo el nombre por el id ==================");
		System.out.println("Ingrese el id:");
		Long id = scanner.nextLong();
		scanner.close();

		System.out.println("===== mostrando solo el nombre =====");
		String name = repository.getNameById(id);
		System.out.println(name);

		System.out.println("===== mostrando solo el id =====");
		Long idDb = repository.getIdById(id);
		System.out.println(idDb);

		System.out.println("===== mostrando nombre completo con concat =====");
		String fullName = repository.getFullNameById(id);
		System.out.println(fullName);

		System.out.println("===== consulta por campos personalizados por el id =====");
		Optional<Object> optionalReg = repository.obtenerPersonDataById(id);
		if (optionalReg.isPresent()) {
			Object[] personReg = (Object[]) optionalReg.orElseThrow();
			System.out.println("id=" + personReg[0] + ", nombre=" + personReg[1] + ", apellido=" + personReg[2]
					+ ", lenguaje=" + personReg[3]);
		}

		System.out.println("===== consulta por campos personalizados lista ======");
		List<Object[]> regs = repository.obtenerPersonDataList();
		regs.forEach(reg -> System.out
				.println("id=" + reg[0] + ", nombre=" + reg[1] + ", apellido=" + reg[2] + ", lenguaje=" + reg[3]));
	}
	
	@Transactional
	public void delete2() {
		repository.findAll().forEach(System.out::println);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		// optionalPerson.ifPresentOrElse(repository::delete,
		// 		() -> System.out.println("Lo sentimos no existe la persona con ese id!"));

		optionalPerson.ifPresentOrElse(person -> repository.delete(person),
				() -> System.out.println("Lo sentimos no existe la persona con ese id!"));

		repository.findAll().forEach(System.out::println);
		scanner.close();
	}
	
	@Transactional
	public void delete() {
		repository.findAll().forEach(System.out::println);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona");
		Long id = scanner.nextLong();
		repository.deleteById(id);

		repository.findAll().forEach(System.out::println);
		scanner.close();
	}
	// modificando por el id
	@Transactional
	public void update() {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona:");
		Long id = scanner.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		// optionalPerson.ifPresent(person -> {
		if (optionalPerson.isPresent()) {
			Person personDB = optionalPerson.orElseThrow();	

			System.out.println(personDB);
			System.out.println("Ingrese el lenguaje de programacion:");
			String programmingLanguage = scanner.next();
			personDB.setProgrammingLanguage(programmingLanguage);
			Person personUpdated = repository.save(personDB);
			System.out.println(personUpdated);
		} else {
			System.out.println("El usuario no esta presente! no existe!");
		}

		// });

		scanner.close();
	}

	@Transactional
	public void create() {
		Scanner scanner = new Scanner(System.in);
		String name = scanner.next();
		String lastname = scanner.next();
		String programmingLanguage = scanner.next();
		scanner.close();
		Person person = new Person(null,name,lastname,programmingLanguage);
		
		Person personNew = repository.save(person);
		System.out.println(personNew);
		repository.findById(personNew.getId()).ifPresent(System.out::println);

		// System.out.println("////////////");
		// List<Person> persons = (List<Person>) repository.findAll();
		// persons.stream().forEach(System.out::println);
	}
	
	// Obtener un solo obejto
	@Transactional(readOnly = true) // una transaccion de consulta(de lectura)
	public void findOne() {
		// Person person = null;
		// Optional<Person> opcionalPerson = repository.findById(8L);
		// if (opcionalPerson.isPresent()) {
		// 	person = opcionalPerson.get();
		// }
		// System.out.println(person);
		// repository.findById(1L).ifPresent(person -> System.out.println(person));
		repository.findByName("Franco").ifPresent(System.out::println);
		repository.findOneLikeName("dad").ifPresent(System.out::println);
		repository.findByNameContaining("nco").ifPresent(System.out::println);
	}
	// Lista de objetos
	@Transactional(readOnly = true)
	public void list() {
		// List<Person> persons = (List<Person>) repository.findAll();
		// List<Person> persons = (List<Person>) repository.findByProgrammingLanguage("Javascript");
		// List<Person> persons = (List<Person>) repository.buscarByProgrammingLanguage("Java","Angel");
		List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("C#", "Angel");

		persons.stream().forEach(person -> {
			System.out.println(person);
		});

		///////////////////////////////////////////////////////////////

		List<Object[]> personsValue = repository.obtenerPersonData();
		System.out.println(personsValue.getClass());
		personsValue.stream().forEach(person -> { 
			System.out.println(person[0]+ " es experto en " + person[1]);
		});
	} 
}
```
### Conclusion
**La aplicación se conectará a la base de datos MySQL configurada, cargará los datos iniciales desde import.sql y mostrará la lista de personas en la consola.**




