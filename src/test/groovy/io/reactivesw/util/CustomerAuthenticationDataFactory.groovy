package io.reactivesw.util

/**
 * DataFactory of customer authentication service
 */
class CustomerAuthenticationDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getSignup() {
        return slurper.parse(new FileReader("src/test/resources/customer/authentication/SignUp.json"))
    }

    public static def getSignin() {
        return slurper.parse(new FileReader("src/test/resources/customer/authentication/SignIn.json"))
    }
}