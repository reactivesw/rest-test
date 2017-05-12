import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.util.RestClientFactory
import spock.lang.Specification

class HealthCheckTest extends Specification {
    def "Test1: health check should return 200 ok"() {
        given: "prepare data"
        def client = RestClientFactory.getClient(ProductTypeConfig.ROOTURL)
        def path = [path: "health"]

        when: "call function to check health status"
        def response = client.get(path)

        then: "should return 200 ok"
        response.status == 200
    }

}