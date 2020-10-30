package api

import (
	"fmt"
	"net/http"
)

func (api *API) InitUser() {
	api.BaseRoutes.Users.HandleFunc("", GetUsers).Methods("GET")
}

func GetUsers(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	if _, err := w.Write([]byte("Hello world!")); err != nil {
		fmt.Printf("Error in writing response: %v", err)
	}
}