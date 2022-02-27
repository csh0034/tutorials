package contracts.sampleController

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/name'
        body([
                "id": "ASk"
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status OK()
        body("name...")
        headers {
            contentType('text/plain')
        }
    }
}
