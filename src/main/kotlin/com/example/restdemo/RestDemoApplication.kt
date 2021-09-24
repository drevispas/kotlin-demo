package com.example.restdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.PostConstruct
import javax.persistence.Entity
import javax.persistence.Id

@SpringBootApplication
class RestDemoApplication

fun main(args: Array<String>) {
    runApplication<RestDemoApplication>(*args)
}

/* Domain/Entity class */
// `val/var` after class name become class properties and constructor arguments at once.
//class Coffee(val id: String = UUID.randomUUID().toString(), var name: String)
//@Entity
//class Coffee(){
//    @Id
//    var id=UUID.randomUUID().toString()
//    var name: String=""
//    constructor(name:String) : this() {
//        this.name = name
//    }
//}
@Entity
// No error if every argument has a defult value.
class Coffee(@Id val id: String = UUID.randomUUID().toString(), val name:String="") {
}

/* Controller */
@RestController
@RequestMapping("/coffees")
class ApiController(val coffeeRepository: CoffeeRepository) {
    @GetMapping
    fun getCoffees() = coffeeRepository.findAll()

    @GetMapping("/{id}")
    // TODO: return default value if empty Optional
    fun getCoffeeById(@PathVariable id: String) = coffeeRepository.findById(id)

    @PostMapping
    fun postCoffee(@RequestBody coffee: Coffee) = coffeeRepository.save(coffee)

    @PutMapping("/{id}")
    fun putCoffee(@PathVariable id: String, @RequestBody coffee: Coffee) =
        coffeeRepository.existsById(id).let {
            coffeeRepository.save(coffee)
            if(it) {
                ResponseEntity(coffee,HttpStatus.OK)
            } else {
                ResponseEntity(coffee,HttpStatus.CREATED)
            }
        }

    @DeleteMapping("/{id}")
    fun deleteCoffee(@PathVariable id: String) = coffeeRepository.deleteById(id)
}

// extends => :
// {} => (omit)
interface CoffeeRepository : CrudRepository<Coffee, String>

@Component
class DataLoader(val coffeeRepository: CoffeeRepository){
    @PostConstruct
    private fun loadData() {
        coffeeRepository.saveAll(
            listOf(
                Coffee(name = "Cafe Cereza"),
                Coffee(name = "Cafe Ganador"),
                Coffee(name = "Cafe Lareno"),
                Coffee(name = "Cafe Tres Pontas")
            )
        )
    }
}