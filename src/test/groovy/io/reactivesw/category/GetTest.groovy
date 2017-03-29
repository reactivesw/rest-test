package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Davis on 17/3/10.
 */
class GetTest extends Specification {
    @Shared
    def id
    @Shared
    def version
    @Shared
    def category
    @Shared
    def primerEndpoint
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def setupSpec() {
        category = CategoryDataFactory.getCategory()
        primerEndpoint = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def response = primerEndpoint.post(body: category)
        id = response.data.id
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "test 1 : get category by id"() {
        when: "call api to get category with id"
        def response = primerEndpoint.get(path: id)

        then: "success, return 200 and new category equal to given category"
        response.status == 200
        category.name == response.data.name
        category.slug == response.data.slug
    }

    def "test 2 : get all category with root url"() {
        when: "call api to get all category"
        def response = primerEndpoint.get(path: null)

        then: "return 200, and result count should be more than 1"
        response.status == 200
        response.data.count >= 1
    }

    def "test 3 : get category with not exist id"() {
        given: "prepare a not exist id"
        def wrongId = "this is a wrong id"

        when: "call api to get category with id"
        def response = primerEndpoint.get(path: wrongId)

        then: "fail and return 404"
        response == 404
    }

    def cleanupSpec() {
        CleanupUtil.cleanup(CategoryConfig.rootURL, cleanupMap)
    }
}
