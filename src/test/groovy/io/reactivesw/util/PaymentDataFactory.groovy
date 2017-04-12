package io.reactivesw.util

/**
 *
 */
class PaymentDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getValidCreditCardView() {
        return slurper.parse(new FileReader('src/test/resources/payment/addCreditCard.json'));
    }
}
