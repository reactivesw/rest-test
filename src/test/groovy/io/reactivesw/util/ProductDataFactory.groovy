class ProductDataFactory {
    static def slurper = new groovy.json.JsonSlurper()

    public static def getIPhone10() {
        return slurper.parse(new FileReader("src/test/resources/product/iPhone10.json"))
    }

    public static def getIphone18() {
        return slurper.parse(new FileReader("src/test/resources/product/iPhone18.json"))
    }

    public static def getLevisJean() {
        return slurper.parse(new FileReader("src/test/resources/product/Levis_Jean.json"))
    }

    public static def getMECHREVO() {
        return slurper.parse(new FileReader("src/test/resources/product/MECHREVO.json"))
    }

    public static def getMSIGT78() {
        return slurper.parse(new FileReader("src/test/resources/product/MSI_GT78.json"))
    }

    public static def getMSIGT79() {
        return slurper.parse(new FileReader("src/test/resources/product/MSI_GT79.json"))
    }

    public static def getTShirt() {
        return slurper.parse(new FileReader("src/test/resources/product/T-Shirt.json"))
    }
}