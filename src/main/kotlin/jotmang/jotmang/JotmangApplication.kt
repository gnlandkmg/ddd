package jotmang.jotmang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JotmangApplication

fun main(args: Array<String>) {
    runApplication<JotmangApplication>(*args)
}