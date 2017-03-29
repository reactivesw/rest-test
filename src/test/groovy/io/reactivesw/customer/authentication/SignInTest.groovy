package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Created by samray on 3/28/17.
 */
class SignInTest extends Specification {
    def client = RestClientFactory.getJsonClient(CustomerAuthenticationConfig.ROOTURL)

    def "test 1: sign in with email and password should return customerView and the status of response should be 200"() {
        given: "prepare data that customer needs to sign in"
        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: signinInformation)

        then: "response status should be 200,and email should be equal to given signinInformation"
        with(response) {
            status == 200
            data.customerView.email == signinInformation.email
        }

    }

    def "test 2: sign in with customer that haven't sign up,response status should return 404"() {
        given: "prepare the invalid data"
        def invalidCustomer = CustomerAuthenticationDataFactory.getSignin().invalidCustomer

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidCustomer)

        then: "response status should be 404,and error code should be 10001"
        response == 404
    }

    def "test3: sign in with wrong password,response should be 401 "() {
        given: "prepare the email and wrong password"
        def invalidPassword = CustomerAuthenticationDataFactory.getSignin().invalidPassword

        when: "call signin api to sign in"
        def response = client.post(path: "signin", body: invalidPassword)

        then: "response status should be 401"
        response == 401
    }
}