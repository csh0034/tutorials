package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/sample'
        body([
                "id": "ASk"
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status OK()
        body([
                "id": "ASk!!!"
        ])
        headers {
            contentType('application/json')
        }
    }
}
