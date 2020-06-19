package org.npathai;

import io.micronaut.http.client.annotation.Client;

@Client(id = "short-url-generator")
public interface ShortUrlClient extends UrlShortenerOperations, RedirectionOperations {

}
