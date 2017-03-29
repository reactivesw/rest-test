# Rest Test
This repo is for microservice REST API testing
## how to run test
`
./gradlew build
`
## Test Framework
we use spock to write test case instead of junit because spock is more elegant 
## Test Case
A test case should contains the following properties
1. description: The description of test case,because of feature provided by spock ,so you can write your description into your function name 
2. requestData: Include url,apiName,parameter,method
3. configuration: An array of setups or configuration use for test case
4. expectedResult: The expected response of the request.The required result is 
http code,if a `data` is presented,the data should satisfy expectation
5.result: pass or failure
## Test module
Test case should be organized into test module which is dependent on what kind of service need to test
you could name a test module with the service you want to test.for example ,if you want to test
 `customer-authentication` service,you could create a test module named `customer-authentication`

# The file structure
```
 src
    └── test //directory of test case
        ├── groovy
        │   └── io
        │       └── reactivesw
        │           ├── category //module needs to test
        │           │   └── config //configuration of module
        │           ├── config  //global configuration
        │           ├── customer //module needs to test
        │           │   └── authentication
        │           │       └── config //configuration of module
        │           └── util //some useful function
        └── resources //resource of test case
            ├── category // resource of module which module needs to test
            └── customer //resource of  module which needs to test
                └── authentication

```

