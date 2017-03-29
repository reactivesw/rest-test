package io.reactivesw.util

import groovyx.net.http.RESTClient

/**
 * Created by Davis on 17/3/9.
 */
class CleanupUtil {
    static void cleanup(String url, CleanupMap cleanupMap) {
        cleanupMap.getAllObjects().each { key, value ->
            def primerEndpoint = new RESTClient(url + key)
            def deleteVersion = ['version': value]
            primerEndpoint.delete(query: deleteVersion)
        }
    }
}
