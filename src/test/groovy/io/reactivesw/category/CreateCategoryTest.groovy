package io.reactivesw.category

import io.reactivesw.category.config.CategoryConfig
import io.reactivesw.util.CategoryDataFactory
import io.reactivesw.util.CleanupMap
import io.reactivesw.util.CleanupUtil
import io.reactivesw.util.RestClientFactory
import spock.lang.Shared
import spock.lang.Specification

/**
 * create category test.
 */
class CreateCategoryTest extends Specification {
    @Shared
    CleanupMap cleanupMap = new CleanupMap()

    def client = RestClientFactory.getJsonClient(CategoryConfig.rootURL)

    def "Test1: create category with name and slug, should return 200 ok and category view"() {
        given: "prepare  data"
        def category = CategoryDataFactory.getCategory().validCategory1

        when: "call category api to create category"
        def response = client.post(body: category)

        then: "should return 200 ok and category view"
        response.status == 200
        response.data.name == category.name
        response.data.slug == category.slug
        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test2: create category with all parameter, should return 200 ok and category view"() {
        given: "prepare data"
        def category = CategoryDataFactory.getCategoryWithAllParams()

        when: "call category api to create category"
        def response = client.post(body: category, requestContentType: "application/json")

        then: "should return 200 ok and category view"
        response.status == 200
        def newCategory = response.data
        category.name == newCategory.name
        category.slug == newCategory.slug
        category.description == newCategory.description
        category.externalId == newCategory.externalId
        category.metaDescription == newCategory.metaDescription
        category.metaKeywords == newCategory.metaKeywords
        category.metaTitle == newCategory.metaTitle

        cleanupMap.addObject(response.data.id, response.data.version)
    }

    def "Test3: create category with invalid char in slug, should return 400 bad request"() {
        given: "prepare data"
        def includeInvalidCharCategory = CategoryDataFactory.getCategory().includeInvalidCharCategory

        when: "call category api to create category"
        def response = client.post(body: includeInvalidCharCategory)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test4: create category with 1-char length(less than minimum size) slug, should return 400 bad request"() {
        given: "prepare data"
        def lessThanMinimumSizeCategory = CategoryDataFactory.getCategory().lessThanMinimumSizeCategory

        when: "call category api to create category"
        def response = client.post(body: lessThanMinimumSizeCategory)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test5: create category with 277-char length(great than maximum size) slug, should return 400 bad request"() {
        given: "prepare data"
        def greatThanMaximumSizeCategory = CategoryDataFactory.getCategory().greatThanMaximumSizeCategory

        when: "call category api to create category"
        def response = client.post(body: greatThanMaximumSizeCategory)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test6: create category without slug, should return 400 bad request"() {
        given: "prepare data"
        def withoutSlugCategory = CategoryDataFactory.getCategory().withoutSlugCategory

        when: "call category api to create category"
        def response = client.post(body: withoutSlugCategory)

        then: "should return 400 bad request"
        response == 400
    }

    def "Test7: create category without name, should return 400 bad request"() {
        given: "prepare data"
        def withoutNameCategory = CategoryDataFactory.getCategory().withoutNameCategory

        when: "call category api to create category"
        def response = client.post(body: withoutNameCategory)

        then: "should return 400 bad request"
        response == 400
    }

    def cleanupSpec() {
        def url = CategoryConfig.rootURL
        CleanupUtil.cleanup(url, cleanupMap)
    }
}
