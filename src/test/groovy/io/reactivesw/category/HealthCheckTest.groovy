package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Created by Davis on 17/3/9.
 */
class HealthCheckTest extends Specification {
    def "Test1: health check should return 200 ok"() {
        given: "prepare path"
        def client = RestClientFactory.getClient(CategoryConfig.rootURL)
        def path = [path: 'health']

        when: "call api"
        def response = client.get(path)

        then: "status of response should be 200 ok"
        response.status == 200
    }
}
