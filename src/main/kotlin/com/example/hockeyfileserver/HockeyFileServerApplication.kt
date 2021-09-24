package com.example.hockeyfileserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HockeyFileServerApplication

fun main(args: Array<String>) {
    runApplication<HockeyFileServerApplication>(*args)
}
