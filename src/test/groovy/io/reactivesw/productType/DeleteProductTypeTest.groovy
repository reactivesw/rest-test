package io.reactivesw.productType

import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

class DeleteProductTypeTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def client = RestClientFactory.getClient(ProductTypeConfig.ROOTURL)

    def "Test1: delete product type with valid id and version, should return 200 ok"() {
        given: "prepare data"
        def productType = ProductTypeDataFactory.getCreateProductType().validProductType1
        def createResponse = client.post(body: productType, requestContentType: "application/json")
        def id = createResponse.data.id
        def version = createResponse.data.version
        def parameter = ['version': version]

        when: "call function to delete product type"
        def deleteResponse = client.delete(path: id, query: parameter)

        then: "should return 200 ok"
        deleteResponse.status == 200
    }

    def "Test2: delete with invalid id, should return 404 not found"() {
        given: "prepare data"
        def invalidId = "this-is-a-invalid-id"
        def parameter = ['version': 0]

        when: "call function to delete product type"
        def response = client.delete(path: invalidId, query: parameter)

        then: "should return 404 not found"
        response == 404
    }

    def "Test3: delete with wrong version, should return 409 conflict"() {
        given: "prepare data"
        def productType = ProductTypeDataFactory.getCreateProductType().validProductType1
        def createResponse = client.post(body: productType, requestContentType: "application/json")
        def id = createResponse.data.id
        def parameter = ['version': 1214124]
        cleanupMap.addObject(createResponse.data.id, createResponse.data.version)

        when: "call function to delete product type"
        def deleteResponse = client.delete(path: id, query: parameter)

        then: "should return 409 conflict"
        deleteResponse == 409
    }

    def cleanupSpec() {
        if (!cleanupMap.isEmpty()) {
            CleanupUtil.cleanup(ProductTypeConfig.ROOTURL, cleanupMap)
        }
    }
}