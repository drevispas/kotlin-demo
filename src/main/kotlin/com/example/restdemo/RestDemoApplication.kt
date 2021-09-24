package com.example.restdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class RestDemoApplication

fun main(args: Array<String>) {
    runApplication<RestDemoApplication>(*args)
}

/* Domain class */
class Coffee(val id: String = UUID.randomUUID().toString(), var name: String)

/* Controller */
@RestController
@RequestMapping("/coffees")
class ApiController {
    private val coffees:MutableList<Coffee> = ArrayList()

    init {
        coffees.addAll(
            mutableListOf(
                Coffee(name = "Cafe Cereza"),
                Coffee(name = "Cafe Ganador"),
                Coffee(name = "Cafe Lareno"),
                Coffee(name = "Cafe Tres Pontas")
            )
        )
    }

    @GetMapping
    fun getCoffees(): Iterable<Coffee> = coffees

    @GetMapping("/{id}")
    fun getCoffeeById(@PathVariable id: String) = coffees.find { it.id == id } ?: "No such coffee found"

    @PostMapping
    fun postCoffee(@RequestBody coffee: Coffee) = coffees.let {
        it.add(coffee)
        coffee
    }

    @PutMapping("/{id}")
    fun putCoffee(@PathVariable id: String, @RequestBody coffee: Coffee) =
        coffees.find { it.id == id }?.let {
            it.name = coffee.name
            ResponseEntity(coffee, HttpStatus.OK)
        } ?: ResponseEntity(postCoffee(coffee), HttpStatus.CREATED)

    @DeleteMapping("/{id}")
    fun deleteCoffee(@PathVariable id: String) = coffees.removeIf { it.id == id }
}
