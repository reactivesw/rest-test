package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Test get anonymous token api
 */
class GetAnonymousTokenTest extends Specification {
    def "Test1: get anonymous token test should return a token and response status should be 200 ok"() {
        given:
        def client = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
        def path = [path: 'anonymous']

        when:
        def response = client.get(path)

        then:
        response.status == 200
        response.responseData != null
    }
}
