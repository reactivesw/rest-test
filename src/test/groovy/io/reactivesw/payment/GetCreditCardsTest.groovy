import io.reactivesw.util.CleanupMap
import spock.lang.Shared
import spock.lang.Specification

class GetCreditCardsTest extends Specification {

    @Shared
    def customerId
    @Shared
    def client
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

//    def setupSpec() {
//        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer
//        def signInClient = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
//        def response = signInClient.post(path: "signin", body: signinInformation, requestContentType: "application/json")
//        customerId = response.data.customerView.id
//    }

//    def "Test1: get all credit cards by customer id, should return 200 ok and credit card view list"() {
//        given: "prepare data"
//        def client = RestClientFactory.getClient(PaymentConfig.ROOTURL)
//        def requestParam = [customerId: customerId]
//
//        when: "call api to get all credit cards"
//        def response = client.get(path: "credit-cards", query: requestParam)
//
//        then: "should return 200, and credit card list (if exist)"
//        with(response) {
//            status == 200
//        }
//        if (response.data[0]) {
//            response.data[0].customerId == customerId
//        }
//    }
//
//    def "Test2: get credit cards with invalid customer id, should return 200 ok and an empty list"() {
//        given: "prepare  data"
//        def client = RestClientFactory.getClient(PaymentConfig.ROOTURL)
//        def requestParam = [customerId: "invalid-customer-id"]
//
//        when: "call api to get credit card"
//        def response = client.get(path: "credit-cards", query: requestParam)
//
//        then: "should return 200 ok"
//        response.status == 200
//    }

//    def cleanupSpec() {
//        CleanupUtil.cleanup(PaymentConfig.ROOTURL, cleanupMap)
//    }
}
