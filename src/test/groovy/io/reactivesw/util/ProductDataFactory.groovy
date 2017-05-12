package io.reactivesw.util

/**
 * Product data factory, parse json file to object.
 */
class ProductDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/Product.json'))
    }
}
