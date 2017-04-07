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
        def client = RestClientFactory.getClient(PaymentConfig.ROOTURL)
        def signInClient = RestClientFactory.getClient(CustomerAuthenticationConfig.ROOTURL)
        def response = signInClient.post(path: "signin", body: signinInformation, requestContentType: "application/json")
        customerId = response.data.customerView.id
    }

    def "test1: get all credit cards by customer id ,should return all credit informat,status of response should be 200"() {
        given: "prepare data"
        def creditCardsPath = "credit-cards" + "/" + customerId
        def path = [path: creditCardsPath]

        when: "get credit card by calling api"
        def response = client.get(path)

        then: "status of response should be 200,and customer id in response should be equal to given customer id "
        with(response) {
            status == 200
        }
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(PaymentConfig.ROOTURL, cleanupMap)
    }
}
