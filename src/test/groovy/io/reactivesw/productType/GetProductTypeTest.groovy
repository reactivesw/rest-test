import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

class GetProductTypeTest extends Specification {
    @Shared
    def id
    @Shared
    def version
    @Shared
    def productType
    @Shared
    def client
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        productType = ProductTypeDataFactory.getCreateProductType().validProductType1
        client = RestClientFactory.getClient(ProductTypeConfig.ROOTURL)
        def response = client.post(body: productType, requestContentType: "application/json")
        id = response.data.id
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test1: get product type by id, should return 200 ok and productTypeView"() {
        when: "call function to get product type with id"
        def response = client.get(path: id)

        then: "should return 200 ok and productTypeView"
        with(response) {
            status == 200
            data.name == productType.name
            data.description == productType.description
        }
    }

    def "Test2: get all product type with root url, should return 200 ok and productTypeView list"() {
        when: "prepare data"
        def response = client.get(path: null)

        then: "should return 200 ok and productTypeView count should be more than 1"
        response.status == 200
        response.data.total >= 1

    }

    def "Test3: get product type with not exist, should return 404 not found"() {
        given: "prepare data"
        def invalidId = "this-is-a-invalid-id"

        when: "call function to get product type"
        def response = client.get(path: invalidId)

        then: "should return 404 not found"
        response == 404
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, cleanupMap)
    }
}