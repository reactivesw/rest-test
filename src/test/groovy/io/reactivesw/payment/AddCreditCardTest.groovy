package io.reactivesw.payment

import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.payment.config.PaymentConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * add creait card test
 */
class AddCreditCardTest extends Specification {

    @Shared
    def customerId
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

//    def setupSpec() {
//        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer
//        def signInClient = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
//        def response = signInClient.post(path: "signin", body: signinInformation, requestContentType: "application/json")
//        customerId = response.data.customerView.id
//    }

//    def "Test1: add credit card with credit card draft, should return 200 ok and credit card view list"() {
//        given: "prepare data"
//        def client = RestClientFactory.getClient(PaymentConfig.ROOTURL)
//        def creditCardDraft = PaymentDataFactory.getValidCreditCardView().validCreditCardDraft
//        creditCardDraft['customerId'] = customerId
//
//        when: "call api to add credit card"
//        def response = client.post(path: "credit-cards", body: creditCardDraft, requestContentType: "application/json")
//
//        then: "should return 200 and credit card view list"
//        with(response) {
//            status == 200
//            data[0].customerId == customerId
//        }
//        def lengthOfCreditCardList = response.data.size()
//        println lengthOfCreditCardList
//        cleanupMap.addObject(response.data[lengthOfCreditCardList - 1].id, response.data[lengthOfCreditCardList - 1].version)
//    }

//    def cleanupSpec() {
//        CleanupUtil.cleanup(PaymentConfig.ROOTURL + "credit-cards", cleanupMap)
//    }
}
