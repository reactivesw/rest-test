package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Davis on 17/3/9.
 */
class DeleteTest extends Specification {

    @Shared
    def id
    @Shared
    def version
    @Shared
    def primerEndpoint

    def setupSpec() {
        def category = CategoryDataFactory.getCategory()
        primerEndpoint = RestClientFactory.getJsonClient(CategoryConfig.rootURL)
        def response = primerEndpoint.post(body: category)
        id = response.data.id
        version = response.data.version
    }

    def "test 1 : delete category with id and version, should return 200 and success"() {
        given: "prepare data"
        primerEndpoint = RestClientFactory.getClient(CategoryConfig.rootURL)
        def version = ['version': version]

        when: "call api"
        def response = primerEndpoint.delete(path: id, query: version)

        then: "response status should be 200"
        response.status == 200
    }

    def "test 2 : delete category with not exist id, would fail and return 404"() {
        given: "prepare data"
        primerEndpoint = RestClientFactory.getClient(CategoryConfig.rootURL)
        def wrongId = "this is a wrong id"
        def version = ['version': version]

        when: "call api to delete category which was deleted at test 1"
        def response = primerEndpoint.delete(path: wrongId, query: version)

        then: "response status should be 404"
        response == 404
    }
}
