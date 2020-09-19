package org.npathai.discourse.application;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;

@Controller("/hello")
public class HelloWorldController {

    @Get
    public Single<String> get() {
        return Single.just("Welcome to Discourse!");
    }
}
