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
        given: "prepare data "
        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer
        def signupResponse = client.post(path: "signup", body: signinInformation, requestContentType: "application/json")

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: signinInformation, requestContentType: "application/json")

        then: "should return 200 and customer view"
        with(response) {
            status == 200
            data.customerView.email == signinInformation.email
        }

    }

    def "Test2: sign in with customer that haven't sign up, should return 404 not found"() {
        given: "prepare data"
        def invalidCustomer = CustomerAuthenticationDataFactory.getSignin().invalidCustomer

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidCustomer, requestContentType: "application/json")

        then: "should return 404 not found"
        response == 404
    }

    def "Test3: sign in with wrong password, should return 401 unauthorized"() {
        given: "prepare data"
        def invalidPassword = CustomerAuthenticationDataFactory.getSignin().invalidPassword

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidPassword, requestContentType: "application/json")

        then: "should return 401 unauthorized"
        response == 401
    }
}