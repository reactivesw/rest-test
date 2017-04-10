package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Test sign in api
 */
class SignInTest extends Specification {
    def client = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)

    def "Test1: sign in with email and password, should return 200 ok and customer view"() {
        given: "prepare data that customer needs to sign in"
        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: signinInformation, requestContentType: "application/json")

        then: "response status should be 200, email should be equal to given signinInformation"
        with(response) {
            status == 200
            data.customerView.email == signinInformation.email
        }

    }

    def "Test2: sign in with customer that haven't sign up, should return 404 not found"() {
        given: "prepare the invalid data"
        def invalidCustomer = CustomerAuthenticationDataFactory.getSignin().invalidCustomer

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidCustomer, requestContentType: "application/json")

        then: "response status should be 404,and error code should be 10001"
        response == 404
    }

    def "Test3: sign in with wrong password, should return 401 unauthorized"() {
        given: "prepare the email and wrong password"
        def invalidPassword = CustomerAuthenticationDataFactory.getSignin().invalidPassword

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidPassword, requestContentType: "application/json")

        then: "response status should be 401"
        response == 401
    }
}