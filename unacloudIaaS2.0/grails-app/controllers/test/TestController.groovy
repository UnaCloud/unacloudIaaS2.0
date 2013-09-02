package test

class TestController {

    def index() {
		printf(params.token)
		[string: "hola mundo"]
	}
}
