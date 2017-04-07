import io.reactivesw.customer.authentication.config.CustomerAuthenticationConfig
import io.reactivesw.payment.config.PaymentConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.CustomerAuthenticationDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

class GetCreditCardsTest extends Specification {

    @Shared
    def customerId
    @Shared
    def client
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        def signinInformation = CustomerAuthenticationDataFactory.getSignin().validCustomer
        def signInClient = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
        def response = signInClient.post(path: "signin", body: signinInformation, requestContentType: "application/json")
        customerId = response.data.customerView.id
    }

    def "Test1: get all credit cards by customer id, should return all credit information, status of response should be 200 ok"() {
        given: "prepare data"
        def client = RestClientFactory.getClient(PaymentConfig.ROOTURL)
        def requestParam = [customerId: customerId]

        when: "get credit card by calling api"
        def response = client.get(path: "credit-cards", query: requestParam)

        then: "status of response should be 200, and if credit card exists, customer id in credit card view should be equal given customer id"
        with(response) {
            status == 200
        }
        if (response.data.creditCardView) {
            response.data.creditCardView[0].id == customerId
        }
    }

    def "Test2: get credit cards with invalid customer id"() {
        given: "prepare invalid data"
        def  client=RestClientFactory.getClient(PaymentConfig.ROOTURL)
        def requestParam=[customerId: "invalid-customer-id"]

    }

    def cleanupSpec() {
        CleanupUtil.cleanup(PaymentConfig.ROOTURL, cleanupMap)
    }
}
