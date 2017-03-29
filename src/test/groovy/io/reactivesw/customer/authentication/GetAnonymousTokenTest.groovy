package io.reactivesw.customer.authentication

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

/**
 * Created by samray on 3/28/17.
 */
class GetAnonymousTokenTest extends Specification{
    def "test1: get anonymous token test should return a token and response status should be 200"(){
        given:
        def client=RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
        def path=[path:'anonymous']

        when:
        def response=client.get(path)

        then:
        response.status==200
        response.responseData!=null
    }
}
