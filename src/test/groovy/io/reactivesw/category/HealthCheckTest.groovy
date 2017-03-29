package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Created by Davis on 17/3/9.
 */
class HealthCheckTest extends Specification {
    def "Health Check should return 200 status code"() {
        given:

        def primerEndpoint = RestClientFactory.getClient(CategoryConfig.rootURL)
        def path = [path : 'health']

        when:
        def response = primerEndpoint.get(path)

        then:
        response.status == 200
    }
}
