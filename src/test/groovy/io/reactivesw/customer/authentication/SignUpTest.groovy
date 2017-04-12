package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Test sign up api
 */
class SignUpTest extends Specification {

    def client = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)

    def "Test1: sign up with email and password, should return 200 ok"() {
        given: "prepare  data"
        def email = UUID.randomUUID().toString().replaceAll("-", "") + "@" + "gmail.com"
        def password = "p!6BN9MOxn"
        def signupInfo = ['email': email, 'password': password]

        when: "call signup api to signup"
        def response = client.post(path: "signup", body: signupInfo, requestContentType: "application/json")

        then: "should return 200 ok"
        with(response) {
            status == 200
        }
    }

    def "Test2: sign up with the same email again, should return 409 conflict"() {
        given: "prepare data"
        def duplicateSignUp = CustomerAuthenticationDataFactory.getSignup().duplicateSignUp

        when: "call signup api to signup"
        def response = client.post(path: "signup", body: duplicateSignUp, requestContentType: "application/json")

        then: "should return 409 conflict"
        response == 409
    }

    def "Test3: sign up with password which is not digit, should return 400 bad request"() {
        given: "prepare data"
        def passwordWithoutDigitSignUp = CustomerAuthenticationDataFactory.getSignup().passwordWithoutDigitSignUp

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: passwordWithoutDigitSignUp, requestContentType: "application/json")

        then: "should return 400 bad request"
        response == 400
    }

    def "Test4: sign up with password which is all upper case, should return 400 bad request"() {
        given: "prepare data"
        def passwordWithoutLowerCaseSignUp = CustomerAuthenticationDataFactory.getSignup().passwordWithoutLowerCaseSignUp

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: passwordWithoutLowerCaseSignUp, requestContentType: "application/json")

        then: "should return 400 bad request"
        response == 400
    }

    def "Test5: sign up with invalid email address, should return 400 bad request"() {
        given: "prepare data"
        def invalidEmail = CustomerAuthenticationDataFactory.getSignup().invalidEmail

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: invalidEmail, requestContentType: "application/json")

        then: "should return 400"
        response == 400
    }
}
