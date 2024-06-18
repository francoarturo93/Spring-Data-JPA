package com.franco.curso.springboot.jpa.springbootjpa;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
// import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.franco.curso.springboot.jpa.springbootjpa.repositories.PersonRepository;
import com.franco.curso.springboot.jpa.springbootjpa.dto.PersonDto;
import com.franco.curso.springboot.jpa.springbootjpa.entities.Person;

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