package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * get category test
 */
class GetTest extends Specification {
    @Shared
    def id
    @Shared
    def version
    @Shared
    def category
    @Shared
    def client
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        category = CategoryDataFactory.getCategory().validCategory1
        client = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def response = client.post(body: category)
        id = response.data.id
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test1: get category by id, should return 200 ok and category view"() {
        when: "call api to get category with id"
        def response = client.get(path: id)

        then: "should return 200 ok and category view"
        response.status == 200
        category.name == response.data.name
        category.slug == response.data.slug
    }

    def "Test2: get all category with root url, should return 200 ok and category view list"() {
        when: "call api to get all category"
        def response = client.get(path: null)

        then: "should return 200 and result count should be more than 1"
        response.status == 200
        response.data.count >= 1
    }

    def "Test3: get category with not exist id, should return 404 not found"() {
        given: "prepare data"
        def wrongId = "this is a wrong id"

        when: "call api to get category with id"
        def response = client.get(path: wrongId)

        then: "should return 404 not found"
        response == 404
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, cleanupMap)
    }
}
