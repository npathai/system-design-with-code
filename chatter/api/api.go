package api

import (
	"fmt"
	"net/http"
)

func InitApi() {
	Srv.Router.HandleFunc("/hello", HandleHello)
}

func HandleHello(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	if _, err := w.Write([]byte("Hello world!")); err != nil {
		fmt.Printf("Error in writing response: %v", err)
	}
}
