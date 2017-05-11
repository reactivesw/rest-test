package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 *  delete category test
 */
class DeleteCategoryTest extends Specification {

    @Shared
    CleanupMap cleanupMap = new CleanupMap()


    def "Test1: delete category with id and version, should return 200 ok"() {
        given: "prepare data"
        def category = CategoryDataFactory.getCategory().validCategory1
        def client = RestClientFactory.getClient(CategoryConfig.rootURL)
        def createCategoryResponse = client.post(body: category, requestContentType: "application/json")
        def id = createCategoryResponse.data.id
        def version = createCategoryResponse.data.version
        def requestVersion = ['version': version]

        when: "call api to delete category"
        def response = client.delete(path: id, query: requestVersion)

        then: "should return 200 ok"
        response.status == 200

    }

    def "Test2: delete category with not exist id, should return 404 not found"() {
        given: "prepare data"

        def client = RestClientFactory.getClient(CategoryConfig.rootURL)
        def wrongId = "this is a wrong id"
        def version = ['version': 2]

        when: "call api to delete category with wrong id"
        def response = client.delete(path: wrongId, query: version)

        then: "should return 404 not found"
        response == 404
    }

    def "Test3: delete category with wrong version, should return 409 conflict"() {
        given: "prepare data"
        def client = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def category = CategoryDataFactory.getCategory().validCategory2
        def createCategoryResponse = client.post(body: category)
        def id = createCategoryResponse.data.id
        def version = ['version': 123344]
        cleanupMap.addObject(createCategoryResponse.data.id, createCategoryResponse.data.version)

        when: "call api to delete category with wrong version"
        def response = client.delete(path: id, query: version)

        then: "should return 409 conflict"
        response == 409
    }

    def cleanupSpec() {
        if (!cleanupMap.isEmpty()) {
            CleanupUtil.cleanup(CategoryConfig.rootURL, cleanupMap)
        }
    }
}
