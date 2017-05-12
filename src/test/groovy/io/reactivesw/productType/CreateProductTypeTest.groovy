package io.reactivesw.productType

import io.reactivesw.config.ProductTypeConfig
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.ProductTypeDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

class CreateProductTypeTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()
    def client = RestClientFactory.getClient(ProductTypeConfig.ROOTURL)

    def "Test1: create product type with valid data, should return 200 ok and productTypeView."() {
        given: "prepare data"
        def productType = ProductTypeDataFactory.getCreateProductType().validProductType1

        when: "call function to create product type"
        def response = client.post(body: productType, requestContentType: "application/json")

        then: "should return 200 ok and productTypeView"
        response.status == 200
        response.data.name == productType.name
        response.data.description == productType.description
        cleanupMap.addObject(response.data.id, response.data.version)

    }

    def "Test2: create product type without name, should return 400 bad request."() {
        given: "prepare invalid data"
        def invalidProductTypeWithoutName = ProductTypeDataFactory.getCreateProductType().invalidProductTypeWithoutName

        when: "call function to create product type"
        def response = client.post(body: invalidProductTypeWithoutName, requestContentType: "application/json")

        then: "should return 400 bad request"
        response == 400
    }

    def "Test3: create product type without description, should return 400 bad reqeust."() {
        given: "prepare invalid data"
        def invalidProductTypeWithoutDescription = ProductTypeDataFactory.getCreateProductType().invalidProductTypeWithoutDescription

        when: "call function to create product type"
        def response = client.post(body: invalidProductTypeWithoutDescription, requestContentType: "application/json")

        then: "should return 400 bad request"
        response == 400
    }

    def "Test4: create product type without attribute, should return 200 ok and productTypeView."() {
        given: "prepare invalid data"
        def productTypeWithoutAttribute = ProductTypeDataFactory.getCreateProductType().validProductTypeWithoutAttributes

        when: "call function to create product type"
        def response = client.post(body: productTypeWithoutAttribute, requestContentType: "application/json")

        then: "should return 200 ok and productType"
        response.status == 200
        with(response) {
            status == 200
            data.name == productTypeWithoutAttribute.name
            data.description == productTypeWithoutAttribute.description
        }
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test5: create product type with attribute name includes invalid char, should return 400 bad request."() {
        given: "prepare invalid data"
        def invalidAttributeName = ProductTypeDataFactory.getCreateProductType().invalidAttributesName1

        when: "call function to create product type"
        def response = client.post(body: invalidAttributeName, requestContentType: "application/json")

        then: "should return 400 bad request"

    }



    def cleanupSpec() {
        if (!cleanupMap.isEmpty()) {
            def url = ProductTypeConfig.ROOTURL
            CleanupUtil.cleanup(url, cleanupMap)
        }
    }
}