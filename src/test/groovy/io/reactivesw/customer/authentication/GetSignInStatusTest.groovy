package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Test get sign in api
 */
class GetSignInStatusTest extends Specification {
    def client = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)

    def "Test1: get sign in status with token, should return customer id and 200 ok"() {
        given: "prepare data"
        def validCustomer = CustomerAuthenticationDataFactory.getSignin().validCustomer
        def res = client.post(path: "signin", body: validCustomer, requestContentType: "application/json")
        def token = ['token': res.data.token]

        when: "call get sign in status api"
        def response = client.get(path: "status", query: token)

        then: "response status should be 200 and response should be equal to customer id"
        with(response) {
            status == 200
            data.str == res.data.customerView.id
        }
    }

    def "Test2: get sign in status with invalid token, response status should be 400 bad request"() {
        given: "prepare data"
        def invalidToken = "invalid_token"
        def token = ['token': invalidToken]

        when: "call get sign in status api"
        def response = client.get(path: "status", query: token, requestContentType: "application/json")

        then: "response status should be 400"
        response == 401
    }

    def "Test3: get sign in status with anonymous token, response status should return 200 ok"() {
        given: "prepare anonymous token"
        def res = client.get(path: "anonymous")
        def anonymousToken = ['token': res.data.str]

        when: "call get sign in status api"
        def response = client.get(path: "status", query: anonymousToken, requestContentType: "application/json")

        then: "response status should be 200"
        response.status == 200
    }
}