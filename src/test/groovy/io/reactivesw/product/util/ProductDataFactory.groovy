package io.reactivesw.product.util

/**
 * Product data factory, parse json file to object.
 */
class ProductDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/Product.json'))
    }

    public static def getLackRequiredAttributeProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/LackRequiredAttributeProduct.json'))
    }

    public static def getNotUniqueAttributeProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/NotUniqueAttributeProduct.json'))
    }

    public static def getNotCombinationUniqueAttributeProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/NotCombinationUniqueAttributeProduct.json'))
    }

    public static def getNotSameForAllAttributeProductt() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/NotSameForAllAttributeProduct.json'))
    }

    public static def getSameSkuNameProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/SameSkuNameProduct.json'))
    }

    public static def getExistSlugProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/ExistSlugProduct.json'))
    }

    public static def getExistSkuProduct() {
        return slurper.parse(new FileReader('src/test/resources/product/creation/ExistSkuProduct.json'))
    }
}
