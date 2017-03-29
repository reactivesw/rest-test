package io.reactivesw.util

/**
 * Created by samray on 3/28/17.
 */
class CustomerAuthenticationDataFactory {
    static def slurper=new groovy.json.JsonSlurper()
    public static def getSignup(){
        return slurper.parse(new FileReader("src/test/resources/customerAuthentication/SignUp.json"))
    }
    public static def getSignin(){
        return slurper.parse(new FileReader("src/test/resources/customerAuthentication/SignIn.json"))
    }
}