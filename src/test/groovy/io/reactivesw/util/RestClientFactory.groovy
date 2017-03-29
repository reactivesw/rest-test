package io.reactivesw.util

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import net.sf.json.JSON

/**
 * Created by Davis on 17/3/9.
 */
class RestClientFactory {
    public static def getJsonClient(String url) {
        def client = new RESTClient(url)
        client.contentType = ContentType.JSON
        client.handler.failure = { resp ->
            resp.status
        }
        return client
    }

    public static def getClient(String url) {
        def client = new RESTClient(url)
        client.handler.failure = { resp ->
            resp.status
        }
        return client
    }
}
