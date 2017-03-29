package io.reactivesw.customer.authentication

import io.reactivesw.config.GlobalConfig
import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Created by samray on 3/28/17.
 */
class SignUpTest extends Specification {

    def client = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)

    def "test 1: sign up with email and password,response status should be 200"() {
        given: "prepare signup data"
        def email = UUID.randomUUID().toString().replaceAll("-", "") + "@" + "gmail.com"
        def password = "p!6BN9MOxn"
        def signupInfo = ['email': email, 'password': password]

        when: "call signup api to signup"
        def response = client.post(path: "signup", body: signupInfo, requestContentType: "application/json")

        then: "sign up successfully,response status should be 200"
        with(response) {
            status == 200
        }
    }

    def "test2: sign up with the same email again,the status of response should return 409"() {
        given: "prepare signup data that has been used"
        def duplicateSignUp = CustomerAuthenticationDataFactory.getSignup().duplicateSignUp

        when: "call signup api to signup"
        def response = client.post(path: "signup", body: duplicateSignUp, requestContentType: "application/json")

        then: "sign up fail and response status should be 409"
        response == 409
    }

    def "test3: sign up with password which is not digit,the status of response should return 400"() {
        given: "prepare signup data with invalid password"
        def passwordWithoutDigitSignUp = CustomerAuthenticationDataFactory.getSignup().passwordWithoutDigitSignUp

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: passwordWithoutDigitSignUp, requestContentType: "application/json")

        then: "sign up fail,and response status should be 400->bad request"
        response == 400
    }

    def "test4: sign up with password which is all upper case,the status of response should return 400"() {
        given: "prepare signup data with invalid password"
        def passwordWithoutLowerCaseSignUp = CustomerAuthenticationDataFactory.getSignup().passwordWithoutLowerCaseSignUp

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: passwordWithoutLowerCaseSignUp, requestContentType: "application/json")

        then: "sign up fail,and response status should be 400 "
        response == 400
    }

    def "test5: sign up with invalid email address,the status of response should return 400"() {
        given: "prepare signup data with invalid email"
        def invalidEmail = CustomerAuthenticationDataFactory.getSignup().invalidEmail

        when: "call signup api to sign up"
        def response = client.post(path: "signup", body: invalidEmail, requestContentType: "application/json")

        then: "sign up fail,and response status should be 400"
        response == 400
    }
}
