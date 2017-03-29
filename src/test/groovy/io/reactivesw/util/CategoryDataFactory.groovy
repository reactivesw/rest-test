package io.reactivesw.util

/**
 * Created by Davis on 17/3/9.
 */
class CategoryDataFactory {

    static def slurper = new groovy.json.JsonSlurper()

    public static def getCategory() {

        return slurper.parse(new FileReader('src/test/resources/category/Category.json'))
    }

    public static def getCategoryWithAllParams() {
        return slurper.parse(new FileReader('src/test/resources/category/CategoryWithAllParams.json'))
    }

    public static def getSetNameAction() {
        return slurper.parse(new FileReader('src/test/resources/category/UpdateName.json'))
    }

    public static def getSetSlugAction() {
        return slurper.parse(new FileReader('src/test/resources/category/UpdateSlug.json'))
    }

    public static def getSetDescriptionAction() {
        return slurper.parse(new FileReader('src/test/resources/category/UpdateDescription.json'))
    }

    public static def getMultiUpdateAction() {
        return slurper.parse(new FileReader('src/test/resources/category/MultiUpdate.json'))
    }
}
